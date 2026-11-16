package dev.codex.gtaliketeleport.mixin;

import java.util.List;
import java.util.Set;
import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public final class GtaLikeTeleportMixinPlugin implements IMixinConfigPlugin {
   private static final String JOURNEYMAP_MIXIN = "dev.codex.gtaliketeleport.mixin.JourneyMapClientNetworkDispatcherMixin";
   private static final String VOXY_CLIENT_MIXIN = "dev.codex.gtaliketeleport.mixin.VoxyClientMixin";
   private static final String SODIUM_CHUNK_RENDERER_MIXIN = "dev.codex.gtaliketeleport.mixin.SodiumDefaultChunkRendererMixin";
   private static final String WAYSTONES_WARP_PLATE_MIXIN = "dev.codex.gtaliketeleport.mixin.WaystonesWarpPlateBlockEntityMixin";

   public void onLoad(String mixinPackage) {
   }

   public String getRefMapperConfig() {
      return null;
   }

   public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
      if (mixinClassName.equals("dev.codex.gtaliketeleport.mixin.JourneyMapClientNetworkDispatcherMixin")) {
         return FabricLoader.getInstance().isModLoaded("journeymap");
      } else if (mixinClassName.equals("dev.codex.gtaliketeleport.mixin.VoxyClientMixin")) {
         return FabricLoader.getInstance().isModLoaded("voxy");
      } else if (mixinClassName.equals("dev.codex.gtaliketeleport.mixin.SodiumDefaultChunkRendererMixin")) {
         return FabricLoader.getInstance().isModLoaded("sodium");
      } else {
         return mixinClassName.equals("dev.codex.gtaliketeleport.mixin.WaystonesWarpPlateBlockEntityMixin") ? FabricLoader.getInstance().isModLoaded("waystones") : true;
      }
   }

   public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
   }

   public List<String> getMixins() {
      return null;
   }

   public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
   }

   public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
   }
}
