package dev.codex.gtaliketeleport;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public final class GtaLikeTeleportServer implements ModInitializer {
   private static final int BYPASS_TICKS = 20;
   private static final int ACK_TIMEOUT_TICKS = 200;
   /** Teleport LOD bridge: longest a confirmed teleport waits for its destination to load (4 s). */
   private static final int MAX_DESTINATION_HOLD_TICKS = 80;
   private static final AtomicLong NEXT_REQUEST_ID = new AtomicLong(1L);
   private static final Map<UUID, Integer> bypassNextTeleportTicks = new HashMap();
   private static final Map<Long, PendingTeleport> pendingTeleports = new HashMap();
   private static final Set<UUID> pendingPlayers = new HashSet();
   private static final ThreadLocal<Boolean> executingDelayedTeleport = ThreadLocal.withInitial(() -> {
      return false;
   });

   public void onInitialize() {
      GtaLikeTeleportConfig.load();
      GtaLikeTeleportNetworkPayloads.register();
      GtaLikeTeleportServerNetworking.registerReceivers();
      ServerTickEvents.END_SERVER_TICK.register(GtaLikeTeleportServer::tick);
   }

   public static boolean tryDelayExternalTeleport(ServerPlayer player, ServerLevel level, double x, double y, double z, Set<Relative> relatives, float yaw, float pitch, boolean resetCamera) {
      if (consumeServerTeleportBypass(player)) {
         return false;
      } else if (pendingPlayers.contains(player.getUUID())) {
         return true;
      } else if (!shouldStartServerTransition(player, 1)) {
         return false;
      } else {
         Vec3 targetFeet = resolveTargetFeet(player, x, y, z, relatives);
         return scheduleServerTransition(player, 1, targetFeet, level.dimension(), () -> {
            player.teleportTo(level, x, y, z, relatives, yaw, pitch, resetCamera);
         });
      }
   }

   private static Vec3 resolveTargetFeet(ServerPlayer player, double x, double y, double z, Set<Relative> relatives) {
      return new Vec3(resolveCoordinate(player.getX(), x, relatives, Relative.X), resolveCoordinate(player.getY(), y, relatives, Relative.Y), resolveCoordinate(player.getZ(), z, relatives, Relative.Z));
   }

   private static double resolveCoordinate(double current, double target, Set<Relative> relatives, Relative axis) {
      return relatives != null && relatives.contains(axis) ? current + target : target;
   }

   static boolean scheduleServerTransition(ServerPlayer player, int source, Vec3 targetFeet, Runnable action) {
      return scheduleServerTransition(player, source, targetFeet, player == null ? null : player.level().dimension(), action);
   }

   static boolean scheduleServerTransition(ServerPlayer player, int source, Vec3 targetFeet, ResourceKey<Level> targetDimension, Runnable action) {
      if (consumeServerTeleportBypass(player)) {
         return false;
      } else if (pendingPlayers.contains(player.getUUID())) {
         return true;
      } else if (!shouldStartServerTransition(player, source)) {
         return false;
      } else {
         long requestId = NEXT_REQUEST_ID.getAndIncrement();
         PendingTeleport pending = new PendingTeleport(requestId, player, source, action);
         ServerLevel targetLevel = targetDimension == null ? player.level() : player.level().getServer().getLevel(targetDimension);
         pending.preload = DestinationPreloader.start(player, targetLevel, targetFeet);
         pendingTeleports.put(requestId, pending);
         pendingPlayers.add(player.getUUID());
         GtaLikeTeleportServerNetworking.sendStart(player, requestId, source, targetFeet, targetDimension);
         return true;
      }
   }

   static void handleTeleportAck(ServerPlayer player, long requestId) {
      PendingTeleport pending = (PendingTeleport)pendingTeleports.get(requestId);
      if (pending != null && pending.player.getUUID().equals(player.getUUID())) {
         if (pending.preload != null && !pending.preload.isReady()) {
            // Teleport LOD bridge: the client has finished its zoom-out, but the destination is
            // still loading. Hold the move; tickPendingTeleports commits it (capped).
            pending.acked = true;
            return;
         }

         pendingTeleports.remove(requestId);
         pendingPlayers.remove(player.getUUID());
         runPendingTeleport(pending);
      }
   }

   static void markNextServerTeleportBypassed(ServerPlayer player) {
      if (player != null) {
         bypassNextTeleportTicks.put(player.getUUID(), 20);
      }

   }

   static void runWithServerTeleportBypass(ServerPlayer player, Runnable action) {
      markNextServerTeleportBypassed(player);
      executingDelayedTeleport.set(true);

      try {
         action.run();
      } finally {
         executingDelayedTeleport.set(false);
      }

   }

   private static boolean shouldStartServerTransition(ServerPlayer player, int source) {
      if (player != null && !player.hasDisconnected() && player.isAlive()) {
         if (!GtaLikeTeleportConfig.isEffectEnabled()) {
            return false;
         } else if (source == 1 && !GtaLikeTeleportConfig.isExternalTeleportTransitionsEnabled()) {
            return false;
         } else {
            return source == 2 && !GtaLikeTeleportConfig.isWarpPlateTransitionsEnabled() ? false : GtaLikeTeleportServerNetworking.canSendStart(player);
         }
      } else {
         return false;
      }
   }

   private static boolean consumeServerTeleportBypass(ServerPlayer player) {
      if (Boolean.TRUE.equals(executingDelayedTeleport.get())) {
         return true;
      } else if (player == null) {
         return false;
      } else {
         Integer ticks = (Integer)bypassNextTeleportTicks.remove(player.getUUID());
         return ticks != null && ticks > 0;
      }
   }

   private static void tick(MinecraftServer server) {
      tickBypassEntries();
      tickPendingTeleports();
   }

   private static void tickBypassEntries() {
      Iterator<Map.Entry<UUID, Integer>> iterator = bypassNextTeleportTicks.entrySet().iterator();

      while(iterator.hasNext()) {
         Map.Entry<UUID, Integer> entry = (Map.Entry)iterator.next();
         int ticks = (Integer)entry.getValue() - 1;
         if (ticks <= 0) {
            iterator.remove();
         } else {
            entry.setValue(ticks);
         }
      }

   }

   private static void tickPendingTeleports() {
      Iterator<Map.Entry<Long, PendingTeleport>> iterator = pendingTeleports.entrySet().iterator();

      while(iterator.hasNext()) {
         PendingTeleport pending = (PendingTeleport)((Map.Entry)iterator.next()).getValue();
         ++pending.age;
         if (pending.acked) {
            ++pending.ackWait;
         }

         boolean destinationHoldOver = pending.acked && (pending.preload == null || pending.preload.isReady() || pending.ackWait >= MAX_DESTINATION_HOLD_TICKS);
         if (destinationHoldOver || pending.age > 200) {
            if (pending.acked) {
               System.out.println("[Grand Teleport] Held teleport of " + pending.player.getScoreboardName() + " for " + pending.ackWait
                  + " ticks waiting for destination chunks" + (pending.preload != null && pending.preload.isReady() ? "" : " (gave up, not ready)"));
            }

            iterator.remove();
            pendingPlayers.remove(pending.player.getUUID());
            runPendingTeleport(pending);
         }
      }

   }

   private static void runPendingTeleport(PendingTeleport pending) {
      if (!pending.player.hasDisconnected() && pending.player.isAlive()) {
         runWithServerTeleportBypass(pending.player, pending.action);
      }
   }

   private static final class PendingTeleport {
      private final long requestId;
      private final ServerPlayer player;
      private final int source;
      private final Runnable action;
      private int age;
      private DestinationPreloader.Preload preload;
      private boolean acked;
      private int ackWait;

      private PendingTeleport(long requestId, ServerPlayer player, int source, Runnable action) {
         this.requestId = requestId;
         this.player = player;
         this.source = source;
         this.action = action;
      }
   }
}
