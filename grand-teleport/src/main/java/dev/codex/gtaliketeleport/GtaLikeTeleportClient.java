package dev.codex.gtaliketeleport;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContextBuilder;
import io.netty.channel.ChannelFutureListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public final class GtaLikeTeleportClient implements ClientModInitializer {
   private static final String[] COMMAND_ALIASES = new String[]{"grandtp", "gtp"};
   private static final String USAGE_MESSAGE = "Usage: /gtp or /grandtp on|off|status|player_freeze <on|off|status>";
   private static boolean bypassNextCommand;
   private static boolean bypassNextPacket;
   private static boolean bypassNextJourneyMapTeleport;

   public void onInitializeClient() {
      GtaLikeTeleportConfig.load();
      GtaLikeTeleportNetworkPayloads.register();
      GtaLikeTeleportClientNetworking.registerReceivers();
      registerGtaTeleportCommand();
      ClientSendMessageEvents.ALLOW_COMMAND.register(GtaLikeTeleportClient::interceptOutgoingCommand);
      ClientTickEvents.END_CLIENT_TICK.register(TeleportTransitionController::tick);
   }

   public static boolean interceptOutgoingCommand(String command) {
      if (bypassNextCommand) {
         return true;
      } else {
         Minecraft client = Minecraft.getInstance();
         if (handleGtaTeleportCommand(client, command)) {
            return false;
         } else if (!GtaLikeTeleportConfig.isEffectEnabled()) {
            return true;
         } else if (TeleportCommandMatcher.isTeleportCommand(command) && client.player != null && client.getConnection() != null) {
            if (!canExecuteServerCommand(client, command)) {
               return true;
            } else if (TeleportTransitionController.isRunning()) {
               return true;
            } else {
               TeleportTransitionController.start(client, command);
               return false;
            }
         } else {
            return true;
         }
      }
   }

   public static boolean interceptOutgoingPacket(Connection connection, Packet<?> packet, ChannelFutureListener listener, boolean flush) {
      if (bypassNextPacket) {
         return true;
      } else {
         PacketTeleportTarget teleportTarget = getTeleportPacketTarget(packet);
         if (teleportTarget == null) {
            return true;
         } else {
            Minecraft client = Minecraft.getInstance();
            if (GtaLikeTeleportConfig.isEffectEnabled() && client.player != null && client.level != null && client.getConnection() != null) {
               if (TeleportTransitionController.isRunning()) {
                  return true;
               } else {
                  TeleportTransitionController.start(client, teleportTarget.targetFeet(), teleportTarget.targetDimensionId(), () -> {
                     sendDeferredPacket(connection, packet, listener, flush);
                  }, !teleportTarget.keepMenuOpen());
                  return false;
               }
            } else {
               return true;
            }
         }
      }
   }

   public static boolean interceptJourneyMapTeleport(Vec3 targetFeet, Runnable action) {
      return interceptJourneyMapTeleport(targetFeet, (String)null, action);
   }

   public static boolean interceptJourneyMapTeleport(Vec3 targetFeet, String targetDimensionId, Runnable action) {
      if (bypassNextJourneyMapTeleport) {
         return true;
      } else {
         Minecraft client = Minecraft.getInstance();
         if (GtaLikeTeleportConfig.isEffectEnabled() && client.player != null && client.level != null && client.getConnection() != null) {
            if (TeleportTransitionController.isRunning()) {
               return true;
            } else {
               TeleportTransitionController.start(client, targetFeet, targetDimensionId, () -> {
                  sendDeferredJourneyMapTeleport(action);
               });
               return false;
            }
         } else {
            return true;
         }
      }
   }

   static void handleServerTeleportRequest(GtaLikeTeleportNetworkPayloads.StartServerTeleportPayload payload) {
      Minecraft client = Minecraft.getInstance();
      if (!shouldPlayServerTeleportTransition(client, payload.source())) {
         GtaLikeTeleportClientNetworking.sendServerTeleportAck(payload.requestId());
      } else {
         TeleportTransitionController.start(client, GtaLikeTeleportClientNetworking.targetFeet(payload), GtaLikeTeleportClientNetworking.targetDimensionId(payload), () -> {
            GtaLikeTeleportClientNetworking.sendServerTeleportAck(payload.requestId());
         });
      }
   }

   private static boolean shouldPlayServerTeleportTransition(Minecraft client, int source) {
      if (GtaLikeTeleportConfig.isEffectEnabled() && client.player != null && client.level != null && client.getConnection() != null) {
         if (TeleportTransitionController.isRunning()) {
            return false;
         } else {
            return source == 2 ? GtaLikeTeleportConfig.isWarpPlateTransitionsEnabled() : GtaLikeTeleportConfig.isExternalTeleportTransitionsEnabled();
         }
      } else {
         return false;
      }
   }

   static void sendDeferredCommand(String command) {
      Minecraft client = Minecraft.getInstance();
      if (client.getConnection() != null) {
         GtaLikeTeleportClientNetworking.sendBypassNextServerTeleport();
         bypassNextCommand = true;

         try {
            client.getConnection().sendCommand(command);
         } finally {
            bypassNextCommand = false;
         }

      }
   }

   private static void sendDeferredPacket(Connection connection, Packet<?> packet, ChannelFutureListener listener, boolean flush) {
      GtaLikeTeleportClientNetworking.sendBypassNextServerTeleport();
      bypassNextPacket = true;

      try {
         connection.send(packet, listener, flush);
      } finally {
         bypassNextPacket = false;
      }

   }

   private static void sendDeferredJourneyMapTeleport(Runnable action) {
      GtaLikeTeleportClientNetworking.sendBypassNextServerTeleport();
      bypassNextJourneyMapTeleport = true;

      try {
         action.run();
      } finally {
         bypassNextJourneyMapTeleport = false;
      }

   }

   private static PacketTeleportTarget getTeleportPacketTarget(Packet<?> packet) {
      if (packet instanceof ServerboundCustomPayloadPacket customPayloadPacket) {
         CustomPacketPayload payload = customPayloadPacket.payload();
         Identifier id = payload.type().id();
         PacketTeleportTarget journeyMapTarget = getJourneyMapTeleportTarget(payload, id);
         return journeyMapTarget != null ? journeyMapTarget : getWaystonesTeleportTarget(payload, id);
      } else {
         return null;
      }
   }

   private static PacketTeleportTarget getJourneyMapTeleportTarget(CustomPacketPayload payload, Identifier id) {
      if (id.getNamespace().equals("journeymap") && id.getPath().equals("teleport_req")) {
         try {
            Vec3 targetFeet = new Vec3(readDouble(payload, "getX"), readDouble(payload, "getY"), readDouble(payload, "getZ"));
            return new PacketTeleportTarget(targetFeet, readOptionalDimensionId(payload, "getDimension"), false);
         } catch (InvocationTargetException | NoSuchMethodException | ClassCastException | IllegalAccessException var3) {
            return null;
         }
      } else {
         return null;
      }
   }

   private static PacketTeleportTarget getWaystonesTeleportTarget(CustomPacketPayload payload, Identifier id) {
      if (!id.getNamespace().equals("waystones")) {
         return null;
      } else {
         WaystoneTarget target;
         if (id.getPath().equals("select_waystone")) {
            target = getWaystonesSelectedTarget(payload);
            return target == null ? null : new PacketTeleportTarget(target.targetFeet(), target.targetDimensionId(), true);
         } else if (id.getPath().equals("inventory_button")) {
            target = getWaystonesInventoryButtonTarget();
            return target == null ? null : new PacketTeleportTarget(target.targetFeet(), target.targetDimensionId(), false);
         } else {
            return null;
         }
      }
   }

   private static WaystoneTarget getWaystonesSelectedTarget(CustomPacketPayload payload) {
      try {
         UUID waystoneUid = readUuid(payload, "waystoneUid");
         Minecraft client = Minecraft.getInstance();
         Object menu = client.player == null ? null : client.player.containerMenu;
         WaystoneTarget menuTarget = findWaystoneTargetInMenu(menu, waystoneUid);
         return menuTarget != null ? menuTarget : findWaystoneTargetInStore(waystoneUid);
      } catch (InvocationTargetException | NoSuchMethodException | ClassNotFoundException | ClassCastException | IllegalAccessException var5) {
         return null;
      }
   }

   private static WaystoneTarget getWaystonesInventoryButtonTarget() {
      Minecraft client = Minecraft.getInstance();
      if (client.player == null) {
         return null;
      } else {
         try {
            Class<?> managerClass = Class.forName("net.blay09.mods.waystones.core.PlayerWaystoneManager");
            Method method = managerClass.getMethod("getInventoryButtonTarget", Player.class);
            Object result = method.invoke((Object)null, client.player);
            if (result instanceof Optional) {
               Optional<?> optional = (Optional)result;
               if (!optional.isEmpty()) {
                  return getWaystoneTarget(optional.get());
               }
            }

            return null;
         } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassCastException | ClassNotFoundException var5) {
            return null;
         }
      }
   }

   private static WaystoneTarget findWaystoneTargetInMenu(Object menu, UUID waystoneUid) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
      if (menu == null) {
         return null;
      } else {
         Method method = menu.getClass().getMethod("getWaystones");
         Object result = method.invoke(menu);
         if (result instanceof Collection) {
            Collection<?> waystones = (Collection)result;
            Iterator var5 = waystones.iterator();

            Object waystone;
            do {
               if (!var5.hasNext()) {
                  return null;
               }

               waystone = var5.next();
            } while(!waystoneUid.equals(readUuid(waystone, "getWaystoneUid")));

            return getWaystoneTarget(waystone);
         } else {
            return null;
         }
      }
   }

   private static WaystoneTarget findWaystoneTargetInStore(UUID waystoneUid) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
      Class<?> clientClass = Class.forName("net.blay09.mods.waystones.client.WaystonesClient");
      Object store = clientClass.getMethod("getWaystonesStore").invoke((Object)null);
      Object result = store.getClass().getMethod("getWaystoneById", UUID.class).invoke(store, waystoneUid);
      if (result instanceof Optional<?> optional) {
         if (!optional.isEmpty()) {
            return getWaystoneTarget(optional.get());
         }
      }

      return null;
   }

   private static WaystoneTarget getWaystoneTarget(Object waystone) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
      Object result = waystone.getClass().getMethod("getPos").invoke(waystone);
      if (result instanceof BlockPos pos) {
         Vec3 targetFeet = new Vec3((double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5);
         return new WaystoneTarget(targetFeet, readOptionalDimensionId(waystone, "getDimension"));
      } else {
         return null;
      }
   }

   private static UUID readUuid(Object target, String methodName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
      Method method = target.getClass().getMethod(methodName);
      return (UUID)method.invoke(target);
   }

   private static double readDouble(Object target, String methodName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
      Method method = target.getClass().getMethod(methodName);
      return ((Number)method.invoke(target)).doubleValue();
   }

   private static String readOptionalDimensionId(Object target, String methodName) {
      try {
         Method method = target.getClass().getMethod(methodName);
         Object result = method.invoke(target);
         if (result instanceof ResourceKey<?> key) {
            return DimensionIds.fromResourceKey(key);
         } else {
            return DimensionIds.normalize(result == null ? null : result.toString());
         }
      } catch (InvocationTargetException | NoSuchMethodException | ClassCastException | IllegalAccessException var5) {
         return null;
      }
   }

   private static void registerGtaTeleportCommand() {
      ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
         String[] arr$ = COMMAND_ALIASES;
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String commandName = arr$[i$];
            dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)ClientCommands.literal(commandName).executes((context) -> {
               return sendStatusFeedback((FabricClientCommandSource)context.getSource());
            })).then(ClientCommands.literal("on").executes((context) -> {
               return setEffectEnabled((FabricClientCommandSource)context.getSource(), true);
            }))).then(ClientCommands.literal("off").executes((context) -> {
               return setEffectEnabled((FabricClientCommandSource)context.getSource(), false);
            }))).then(ClientCommands.literal("status").executes((context) -> {
               return sendStatusFeedback((FabricClientCommandSource)context.getSource());
            }))).then(((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)ClientCommands.literal("player_freeze").executes((context) -> {
               return sendPlayerFreezeStatusFeedback((FabricClientCommandSource)context.getSource());
            })).then(ClientCommands.literal("on").executes((context) -> {
               return setPlayerFreezeEnabled((FabricClientCommandSource)context.getSource(), true);
            }))).then(ClientCommands.literal("off").executes((context) -> {
               return setPlayerFreezeEnabled((FabricClientCommandSource)context.getSource(), false);
            }))).then(ClientCommands.literal("status").executes((context) -> {
               return sendPlayerFreezeStatusFeedback((FabricClientCommandSource)context.getSource());
            })))).then(ClientCommands.argument("value", StringArgumentType.word()).executes((context) -> {
               return handleCommandArgument((FabricClientCommandSource)context.getSource(), StringArgumentType.getString(context, "value"));
            })));
         }

      });
   }

   private static int handleCommandArgument(FabricClientCommandSource source, String argument) {
      String lowerArgument = argument.toLowerCase(Locale.ROOT);
      if (lowerArgument.equals("on")) {
         return setEffectEnabled(source, true);
      } else if (lowerArgument.equals("off")) {
         return setEffectEnabled(source, false);
      } else if (lowerArgument.equals("status")) {
         return sendStatusFeedback(source);
      } else if (lowerArgument.equals("player_freeze")) {
         return sendPlayerFreezeStatusFeedback(source);
      } else {
         source.sendError(Component.literal("Usage: /gtp or /grandtp on|off|status|player_freeze <on|off|status>"));
         return 0;
      }
   }

   private static int setEffectEnabled(FabricClientCommandSource source, boolean enabled) {
      boolean saved = GtaLikeTeleportConfig.setEffectEnabled(enabled);
      source.sendFeedback(createStateFeedback(enabled, saved, saved ? ChatFormatting.GREEN : ChatFormatting.YELLOW));
      return 1;
   }

   private static int sendStatusFeedback(FabricClientCommandSource source) {
      source.sendFeedback(createStateFeedback(GtaLikeTeleportConfig.isEffectEnabled(), true, ChatFormatting.GRAY));
      return 1;
   }

   private static int setPlayerFreezeEnabled(FabricClientCommandSource source, boolean enabled) {
      boolean saved = GtaLikeTeleportConfig.setPlayerFreezeEnabled(enabled);
      source.sendFeedback(createPlayerFreezeStateFeedback(enabled, saved, saved ? ChatFormatting.GREEN : ChatFormatting.YELLOW));
      return 1;
   }

   private static int sendPlayerFreezeStatusFeedback(FabricClientCommandSource source) {
      source.sendFeedback(createPlayerFreezeStateFeedback(GtaLikeTeleportConfig.isPlayerFreezeEnabled(), true, ChatFormatting.GRAY));
      return 1;
   }

   private static boolean canExecuteServerCommand(Minecraft client, String command) {
      ClientPacketListener networkHandler = client.getConnection();
      if (networkHandler == null) {
         return false;
      } else {
         String normalized = normalizeCommand(command);
         if (normalized.isEmpty()) {
            return false;
         } else {
            ParseResults<ClientSuggestionProvider> parseResults = networkHandler.getCommands().parse(normalized, networkHandler.getSuggestionsProvider());
            return !parseResults.getReader().canRead() && hasExecutableCommand(parseResults.getContext());
         }
      }
   }

   private static boolean hasExecutableCommand(CommandContextBuilder<ClientSuggestionProvider> context) {
      for(CommandContextBuilder<ClientSuggestionProvider> current = context; current != null; current = current.getChild()) {
         if (current.getCommand() != null) {
            return true;
         }
      }

      return false;
   }

   private static String normalizeCommand(String command) {
      String normalized = command.strip();
      if (normalized.startsWith("/")) {
         normalized = normalized.substring(1).stripLeading();
      }

      return normalized;
   }

   private static String getLocalCommandName(String normalized) {
      int end;
      for(end = 0; end < normalized.length() && !Character.isWhitespace(normalized.charAt(end)); ++end) {
      }

      String commandName = normalized.substring(0, end).toLowerCase(Locale.ROOT);
      String[] var3 = COMMAND_ALIASES;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String alias = var3[var5];
         if (commandName.equals(alias)) {
            return normalized.substring(0, end);
         }
      }

      return null;
   }

   private static boolean handleGtaTeleportCommand(Minecraft client, String command) {
      String normalized = command.stripLeading();
      if (normalized.startsWith("/")) {
         normalized = normalized.substring(1).stripLeading();
      }

      String commandName = getLocalCommandName(normalized);
      if (commandName == null) {
         return false;
      } else {
         String argument = normalized.length() == commandName.length() ? "" : normalized.substring(commandName.length()).strip();
         String lowerArgument = argument.toLowerCase(Locale.ROOT);
         boolean saved;
         if (lowerArgument.equals("on")) {
            saved = GtaLikeTeleportConfig.setEffectEnabled(true);
            sendCommandFeedback(client, true, saved);
            return true;
         } else if (lowerArgument.equals("off")) {
            saved = GtaLikeTeleportConfig.setEffectEnabled(false);
            sendCommandFeedback(client, false, saved);
            return true;
         } else if (!lowerArgument.equals("status") && !lowerArgument.isEmpty()) {
            if (!lowerArgument.equals("player_freeze") && !lowerArgument.equals("player_freeze status")) {
               if (lowerArgument.equals("player_freeze on")) {
                  saved = GtaLikeTeleportConfig.setPlayerFreezeEnabled(true);
                  sendFeedback(client, createPlayerFreezeStateFeedback(true, saved, saved ? ChatFormatting.GREEN : ChatFormatting.YELLOW));
                  return true;
               } else if (lowerArgument.equals("player_freeze off")) {
                  saved = GtaLikeTeleportConfig.setPlayerFreezeEnabled(false);
                  sendFeedback(client, createPlayerFreezeStateFeedback(false, saved, saved ? ChatFormatting.GREEN : ChatFormatting.YELLOW));
                  return true;
               } else {
                  sendFeedback(client, Component.literal("Usage: /gtp or /grandtp on|off|status|player_freeze <on|off|status>").withStyle(ChatFormatting.RED));
                  return true;
               }
            } else {
               sendFeedback(client, createPlayerFreezeStateFeedback(GtaLikeTeleportConfig.isPlayerFreezeEnabled(), true, ChatFormatting.GRAY));
               return true;
            }
         } else {
            sendFeedback(client, createStateFeedback(GtaLikeTeleportConfig.isEffectEnabled(), true, ChatFormatting.GRAY));
            return true;
         }
      }
   }

   private static void sendCommandFeedback(Minecraft client, boolean enabled, boolean saved) {
      sendFeedback(client, createStateFeedback(enabled, saved, saved ? ChatFormatting.GREEN : ChatFormatting.YELLOW));
   }

   private static Component createStateFeedback(boolean enabled, boolean saved, ChatFormatting formatting) {
      String state = enabled ? "ON" : "OFF";
      String message = "Grand Teleport:" + state + (saved ? "" : " (save failed)");
      return Component.literal(message).withStyle(formatting);
   }

   private static Component createPlayerFreezeStateFeedback(boolean enabled, boolean saved, ChatFormatting formatting) {
      String state = enabled ? "ON" : "OFF";
      String message = "Grand Teleport player_freeze:" + state + (saved ? "" : " (save failed)");
      return Component.literal(message).withStyle(formatting);
   }

   private static void sendFeedback(Minecraft client, Component message) {
      if (client.player != null) {
         client.player.sendSystemMessage(message);
      }

   }

   private static record PacketTeleportTarget(Vec3 targetFeet, String targetDimensionId, boolean keepMenuOpen) {
      private PacketTeleportTarget(Vec3 targetFeet, String targetDimensionId, boolean keepMenuOpen) {
         this.targetFeet = targetFeet;
         this.targetDimensionId = targetDimensionId;
         this.keepMenuOpen = keepMenuOpen;
      }

      public Vec3 targetFeet() {
         return this.targetFeet;
      }

      public String targetDimensionId() {
         return this.targetDimensionId;
      }

      public boolean keepMenuOpen() {
         return this.keepMenuOpen;
      }
   }

   private static record WaystoneTarget(Vec3 targetFeet, String targetDimensionId) {
      private WaystoneTarget(Vec3 targetFeet, String targetDimensionId) {
         this.targetFeet = targetFeet;
         this.targetDimensionId = targetDimensionId;
      }

      public Vec3 targetFeet() {
         return this.targetFeet;
      }

      public String targetDimensionId() {
         return this.targetDimensionId;
      }
   }
}
