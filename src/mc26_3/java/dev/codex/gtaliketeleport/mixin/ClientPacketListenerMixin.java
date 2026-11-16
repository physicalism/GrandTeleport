package dev.codex.gtaliketeleport.mixin;

import dev.codex.gtaliketeleport.GtaLikeTeleportClient;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {
    @Inject(method = "handleMovePlayer", at = @At("HEAD"))
    private void gtalikeTeleport$detectServerTeleport(net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket packet, CallbackInfo ci) {
        net.minecraft.client.Minecraft client = net.minecraft.client.Minecraft.getInstance();
        // HEAD also runs once on the network thread before vanilla schedules the handler.
        if (!client.isSameThread() || client.player == null) return;
        net.minecraft.world.entity.PositionMoveRotation absolute = net.minecraft.world.entity.PositionMoveRotation.calculateAbsolute(
                net.minecraft.world.entity.PositionMoveRotation.of(client.player), packet.change(), packet.relatives());
        dev.codex.gtaliketeleport.TeleportTransitionController.observeServerPosition(client, absolute.position());
    }

    @Inject(method = "handleRespawn", at = @At("HEAD"))
    private void gtalikeTeleport$resetDimension(net.minecraft.network.protocol.game.ClientboundRespawnPacket packet, CallbackInfo ci) {
        net.minecraft.client.Minecraft client = net.minecraft.client.Minecraft.getInstance();
        if (client.isSameThread()) dev.codex.gtaliketeleport.TeleportTransitionController.cancel(client);
    }

    @Inject(method = "sendCommand", at = @At("HEAD"), cancellable = true)
    private void gtalikeTeleport$interceptCommand(String command, CallbackInfo ci) {
        if (!GtaLikeTeleportClient.interceptOutgoingCommand(command)) {
            ci.cancel();
        }
    }

    @Inject(method = "sendUnattendedCommand", at = @At("HEAD"), cancellable = true, require = 0)
    private void gtalikeTeleport$interceptUnattendedCommand(String command, Screen screen, CallbackInfo ci) {
        if (!GtaLikeTeleportClient.interceptOutgoingCommand(command)) {
            ci.cancel();
        }
    }

}
