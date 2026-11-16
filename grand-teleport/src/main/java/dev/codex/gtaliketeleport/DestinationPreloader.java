package dev.codex.gtaliketeleport;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * Teleport LOD bridge, server side: starts loading the chunks around a teleport destination
 * while the client is still playing the zoom-out, so they are ready when the player arrives.
 *
 * Uses the vanilla PORTAL ticket (the one Minecraft itself uses to preload a portal's
 * destination). It expires on its own after 300 ticks, so nothing is ever left loaded.
 * Runs on the server thread only.
 */
final class DestinationPreloader {
   /** Chunk radius loaded around the destination (vanilla portals use 3). */
   static final int RADIUS = 4;
   /** Most preloads allowed in flight at once, server-wide. */
   private static final int MAX_ACTIVE = 8;
   /** Per-player spacing between client-requested preloads. */
   private static final int OP_COOLDOWN_TICKS = 40;
   private static final int PLAYER_COOLDOWN_TICKS = 100;
   /** Forget finished entries after this long. */
   private static final int FORGET_AFTER_TICKS = 600;
   /** Reuse an existing preload of the same spot only well inside the PORTAL ticket's 300-tick life. */
   private static final int REUSE_WITHIN_TICKS = 200;
   private static final double MAX_HORIZONTAL = 29_999_984.0;

   private static final Map<UUID, Preload> byPlayer = new HashMap<>();
   private static final Map<UUID, Integer> lastClientRequestTick = new HashMap<>();

   private DestinationPreloader() {
   }

   static final class Preload {
      private final ResourceKey<Level> dimension;
      private final ChunkPos centre;
      private final CompletableFuture<?> future;
      private final int startTick;

      private Preload(ResourceKey<Level> dimension, ChunkPos centre, CompletableFuture<?> future, int startTick) {
         this.dimension = dimension;
         this.centre = centre;
         this.future = future;
         this.startTick = startTick;
      }

      boolean isReady() {
         return this.future.isDone();
      }
   }

   /** Starts (or reuses) a preload for this player's destination. Returns null if it could not start. */
   static Preload start(ServerPlayer player, ServerLevel level, Vec3 feet) {
      if (player == null || level == null || !isSaneTarget(feet)) {
         return null;
      }

      MinecraftServer server = level.getServer();
      int now = server.getTickCount();
      prune(now);
      ChunkPos centre = ChunkPos.containing(BlockPos.containing(feet));
      Preload existing = byPlayer.get(player.getUUID());
      if (existing != null && existing.dimension.equals(level.dimension()) && existing.centre.equals(centre)
         && now - existing.startTick < REUSE_WITHIN_TICKS) {
         return existing;
      }

      if (countActive() >= MAX_ACTIVE) {
         return null;
      }

      CompletableFuture<?> future;
      try {
         future = level.getChunkSource().addTicketAndLoadWithRadius(TicketType.PORTAL, centre, RADIUS);
      } catch (RuntimeException e) {
         System.err.println("[Grand Teleport] Destination preload failed: " + e);
         return null;
      }

      Preload preload = new Preload(level.dimension(), centre, future, now);
      byPlayer.put(player.getUUID(), preload);
      return preload;
   }

   /** A client announced the destination of a teleport it is about to send (typed /tp etc.). */
   static void handleClientRequest(ServerPlayer player, double x, double y, double z, String dimensionId) {
      if (player == null || player.hasDisconnected()) {
         return;
      }

      MinecraftServer server = player.level().getServer();
      int now = server.getTickCount();
      boolean gamemaster = player.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER);
      int cooldown = gamemaster ? OP_COOLDOWN_TICKS : PLAYER_COOLDOWN_TICKS;
      Integer last = lastClientRequestTick.get(player.getUUID());
      if (last != null && now - last < cooldown) {
         return;
      }

      ServerLevel level = resolveLevel(server, player.level(), dimensionId);
      if (level == null) {
         return;
      }

      lastClientRequestTick.put(player.getUUID(), now);
      start(player, level, new Vec3(x, y, z));
   }

   static ServerLevel resolveLevel(MinecraftServer server, ServerLevel fallback, String dimensionId) {
      String wanted = DimensionIds.normalize(dimensionId);
      if (wanted == null || wanted.isEmpty()) {
         return fallback;
      }

      for (ServerLevel level : server.getAllLevels()) {
         if (wanted.equals(DimensionIds.fromResourceKey(level.dimension()))) {
            return level;
         }
      }

      return null;
   }

   private static boolean isSaneTarget(Vec3 feet) {
      return feet != null && Double.isFinite(feet.x) && Double.isFinite(feet.y) && Double.isFinite(feet.z)
         && Math.abs(feet.x) <= MAX_HORIZONTAL && Math.abs(feet.z) <= MAX_HORIZONTAL;
   }

   private static int countActive() {
      int active = 0;
      for (Preload preload : byPlayer.values()) {
         if (!preload.isReady()) {
            ++active;
         }
      }

      return active;
   }

   private static void prune(int now) {
      Iterator<Preload> preloads = byPlayer.values().iterator();
      while (preloads.hasNext()) {
         if (now - preloads.next().startTick > FORGET_AFTER_TICKS) {
            preloads.remove();
         }
      }

      lastClientRequestTick.values().removeIf(tick -> now - tick > FORGET_AFTER_TICKS);
   }
}
