package dev.codex.gtaliketeleport;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;

final class GtaLikeTeleportClientNetworking {
    private GtaLikeTeleportClientNetworking() {
    }

    static boolean isServerSideTeleportAvailable() { return false; }
    static void sendServerTeleportAck(long requestId) { }
    static void sendBypassNextServerTeleport() { }
    static Vec3 targetFeet(GtaLikeTeleportNetworkPayloads.StartServerTeleportPayload payload) {
        return new Vec3(payload.x(), payload.y(), payload.z());
    }
    static String targetDimensionId(GtaLikeTeleportNetworkPayloads.StartServerTeleportPayload payload) {
        return DimensionIds.normalize(payload.dimension());
    }
}
