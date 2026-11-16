package dev.codex.gtaliketeleport;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

final class GtaLikeTeleportNetworkPayloads {
   static final int SOURCE_EXTERNAL = 1;
   static final int SOURCE_WARP_PLATE = 2;
   private static boolean registered;

   private GtaLikeTeleportNetworkPayloads() {
   }

   static synchronized void register() {
      if (!registered) {
         PayloadTypeRegistry.clientboundPlay().register(GtaLikeTeleportNetworkPayloads.StartServerTeleportPayload.TYPE, GtaLikeTeleportNetworkPayloads.StartServerTeleportPayload.CODEC);
         PayloadTypeRegistry.serverboundPlay().register(GtaLikeTeleportNetworkPayloads.ServerTeleportAckPayload.TYPE, GtaLikeTeleportNetworkPayloads.ServerTeleportAckPayload.CODEC);
         PayloadTypeRegistry.serverboundPlay().register(GtaLikeTeleportNetworkPayloads.BypassNextServerTeleportPayload.TYPE, GtaLikeTeleportNetworkPayloads.BypassNextServerTeleportPayload.CODEC);
         registered = true;
      }
   }

   private static Identifier id(String path) {
      return Identifier.fromNamespaceAndPath("gtalike_teleport", path);
   }

   static record StartServerTeleportPayload(long requestId, int source, double x, double y, double z, String dimension) implements CustomPacketPayload {
      static final CustomPacketPayload.Type<StartServerTeleportPayload> TYPE = new CustomPacketPayload.Type(GtaLikeTeleportNetworkPayloads.id("start_server_teleport"));
      static final StreamCodec<RegistryFriendlyByteBuf, StartServerTeleportPayload> CODEC = StreamCodec.ofMember(StartServerTeleportPayload::write, StartServerTeleportPayload::read);

      StartServerTeleportPayload(long requestId, int source, double x, double y, double z, String dimension) {
         this.requestId = requestId;
         this.source = source;
         this.x = x;
         this.y = y;
         this.z = z;
         this.dimension = dimension;
      }

      private void write(RegistryFriendlyByteBuf buffer) {
         buffer.writeLong(this.requestId);
         buffer.writeInt(this.source);
         buffer.writeDouble(this.x);
         buffer.writeDouble(this.y);
         buffer.writeDouble(this.z);
         buffer.writeUtf(this.dimension);
      }

      private static StartServerTeleportPayload read(RegistryFriendlyByteBuf buffer) {
         return new StartServerTeleportPayload(buffer.readLong(), buffer.readInt(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readUtf());
      }

      public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
         return TYPE;
      }

      public long requestId() {
         return this.requestId;
      }

      public int source() {
         return this.source;
      }

      public double x() {
         return this.x;
      }

      public double y() {
         return this.y;
      }

      public double z() {
         return this.z;
      }

      public String dimension() {
         return this.dimension;
      }
   }

   static record ServerTeleportAckPayload(long requestId) implements CustomPacketPayload {
      static final CustomPacketPayload.Type<ServerTeleportAckPayload> TYPE = new CustomPacketPayload.Type(GtaLikeTeleportNetworkPayloads.id("server_teleport_ack"));
      static final StreamCodec<RegistryFriendlyByteBuf, ServerTeleportAckPayload> CODEC = StreamCodec.ofMember(ServerTeleportAckPayload::write, ServerTeleportAckPayload::read);

      ServerTeleportAckPayload(long requestId) {
         this.requestId = requestId;
      }

      private void write(RegistryFriendlyByteBuf buffer) {
         buffer.writeLong(this.requestId);
      }

      private static ServerTeleportAckPayload read(RegistryFriendlyByteBuf buffer) {
         return new ServerTeleportAckPayload(buffer.readLong());
      }

      public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
         return TYPE;
      }

      public long requestId() {
         return this.requestId;
      }
   }

   static record BypassNextServerTeleportPayload() implements CustomPacketPayload {
      static final CustomPacketPayload.Type<BypassNextServerTeleportPayload> TYPE = new CustomPacketPayload.Type(GtaLikeTeleportNetworkPayloads.id("bypass_next_server_teleport"));
      static final StreamCodec<RegistryFriendlyByteBuf, BypassNextServerTeleportPayload> CODEC = StreamCodec.unit(new BypassNextServerTeleportPayload());

      public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
         return TYPE;
      }
   }
}
