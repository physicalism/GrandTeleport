package dev.codex.gtaliketeleport.mixin;

import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.textures.GpuSampler;
import dev.codex.gtaliketeleport.TeleportTransitionController;
import java.lang.reflect.Field;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import net.caffeinemc.mods.sodium.client.gpu.device.batch.MultiDrawBatch;
import net.caffeinemc.mods.sodium.client.render.SodiumWorldRenderer;
import net.caffeinemc.mods.sodium.client.render.chunk.ChunkRenderMatrices;
import net.caffeinemc.mods.sodium.client.render.chunk.LocalSectionIndex;
import net.caffeinemc.mods.sodium.client.render.chunk.UniformBufferManager;
import net.caffeinemc.mods.sodium.client.render.chunk.data.SectionRenderDataStorage;
import net.caffeinemc.mods.sodium.client.render.chunk.data.SectionRenderDataUnsafe;
import net.caffeinemc.mods.sodium.client.render.chunk.lists.ChunkRenderList;
import net.caffeinemc.mods.sodium.client.render.chunk.lists.ChunkRenderListIterable;
import net.caffeinemc.mods.sodium.client.render.chunk.region.RenderRegion;
import net.caffeinemc.mods.sodium.client.render.chunk.terrain.TerrainRenderPass;
import net.caffeinemc.mods.sodium.client.render.viewport.CameraTransform;
import net.caffeinemc.mods.sodium.client.util.FogParameters;
import net.caffeinemc.mods.sodium.client.util.iterator.ByteIterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.oit.OitStage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
   targets = {"net.caffeinemc.mods.sodium.client.render.chunk.DefaultChunkRenderer"},
   remap = false
)
public abstract class SodiumDefaultChunkRendererMixin {
   @Unique
   private static final int MIN_CHUNK_SECTION_FADE_MILLIS = 150;
   @Unique
   private static RenderRegion gtalikeTeleport$currentCommandRegion;
   @Unique
   private static boolean gtalikeTeleport$skipCurrentCommandSection;
   @Unique
   private static final Map<TerrainRenderPass, Boolean> gtalikeTeleport$lastHardCutCullingStates = new IdentityHashMap();
   @Unique
   private static final Map<TerrainRenderPass, Integer> gtalikeTeleport$lastCullingInvalidationTicks = new IdentityHashMap();
   @Unique
   private static Field gtalikeTeleport$uniformBufferManagerField;

   @Inject(
      method = {"render"},
      at = {@At("HEAD")},
      cancellable = true,
      require = 0
   )
   private void gtalikeTeleport$writeFallbackTerrainFade(ChunkRenderMatrices matrices, ChunkRenderListIterable renderLists, TerrainRenderPass renderPass, CameraTransform camera, FogParameters fog, boolean useTranslucentSort, RenderPass pass, GpuSampler sampler, GpuBufferSlice uniformBuffer, GpuBuffer sectionTimeInfo, OitStage oitStage, CallbackInfo ci) {
      if (TeleportTransitionController.shouldPreferDistantHorizonsOnlyTerrain()) {
         gtalikeTeleport$lastHardCutCullingStates.clear();
         gtalikeTeleport$lastCullingInvalidationTicks.clear();
         ci.cancel();
      } else if (!TeleportTransitionController.shouldApplyVanillaFallbackTerrainVisibility()) {
         gtalikeTeleport$lastHardCutCullingStates.clear();
         gtalikeTeleport$lastCullingInvalidationTicks.clear();
      } else {
         UniformBufferManager uniformBufferManager = getUniformBufferManager();
         if (uniformBufferManager != null) {
            int fadeDurationMillis = getChunkSectionFadeMillis();
            boolean hardCutCulling = TeleportTransitionController.shouldUseHardCutFallbackTerrainCulling();
            boolean recoverHardCutBatches = TeleportTransitionController.shouldRecoverHardCutFallbackTerrain();
            boolean invalidateHardCutBatches = shouldInvalidateCachedBatchesForHardCut(renderPass, hardCutCulling);
            Iterator<ChunkRenderList> iterator = renderLists.iterator(renderPass.isTranslucent());

            while(iterator.hasNext()) {
               ChunkRenderList renderList = (ChunkRenderList)iterator.next();
               RenderRegion region = renderList.getRegion();
               if (region != null && region.getResources() != null) {
                  writeSectionFadeTimes(renderList, region, uniformBufferManager, renderPass, fadeDurationMillis);
                  if (invalidateHardCutBatches) {
                     invalidateCachedBatchForHardCut(region, renderPass, hardCutCulling);
                  } else if (recoverHardCutBatches) {
                     recoverEmptyCachedBatchForHardCut(region, renderPass);
                  }
               }
            }

         }
      }
   }

   @Inject(
      method = {"fillCommandBuffer"},
      at = {@At("HEAD")},
      require = 0
   )
   private static void gtalikeTeleport$beginFallbackTerrainCommandBuffer(MultiDrawBatch batch, RenderRegion region, SectionRenderDataStorage storage, ChunkRenderList renderList, CameraTransform camera, TerrainRenderPass renderPass, boolean useBlockFaceCulling, boolean useIndexedTessellation, CallbackInfo ci) {
      gtalikeTeleport$currentCommandRegion = region;
      gtalikeTeleport$skipCurrentCommandSection = false;
   }

   @Inject(
      method = {"fillCommandBuffer"},
      at = {@At("RETURN")},
      require = 0
   )
   private static void gtalikeTeleport$endFallbackTerrainCommandBuffer(MultiDrawBatch batch, RenderRegion region, SectionRenderDataStorage storage, ChunkRenderList renderList, CameraTransform camera, TerrainRenderPass renderPass, boolean useBlockFaceCulling, boolean useIndexedTessellation, CallbackInfo ci) {
      gtalikeTeleport$currentCommandRegion = null;
      gtalikeTeleport$skipCurrentCommandSection = false;
   }

   @Redirect(
      method = {"fillCommandBuffer"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/caffeinemc/mods/sodium/client/render/chunk/data/SectionRenderDataStorage;getDataPointer(I)J"
)},
      require = 0
   )
   private static long gtalikeTeleport$captureFallbackTerrainCommandSection(SectionRenderDataStorage storage, int sectionId) {
      gtalikeTeleport$skipCurrentCommandSection = shouldHardCutSection(gtalikeTeleport$currentCommandRegion, sectionId);
      return storage.getDataPointer(sectionId);
   }

   @Redirect(
      method = {"fillCommandBuffer"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/caffeinemc/mods/sodium/client/render/chunk/data/SectionRenderDataUnsafe;getSliceMask(J)I"
)},
      require = 0
   )
   private static int gtalikeTeleport$hideFallbackTerrainCommandSection(long dataPointer) {
      return gtalikeTeleport$skipCurrentCommandSection ? 0 : SectionRenderDataUnsafe.getSliceMask(dataPointer);
   }

   private static void writeSectionFadeTimes(ChunkRenderList renderList, RenderRegion region, UniformBufferManager uniformBufferManager, TerrainRenderPass renderPass, int fadeDurationMillis) {
      int regionId = region.getId();
      if (regionId >= 0) {
         ByteIterator sectionIterator = renderList.sectionsWithGeometryIterator(renderPass.isTranslucent());
         if (sectionIterator != null) {
            int relativeNow = getRelativeRegionTime(region);

            while(sectionIterator.hasNext()) {
               int sectionId = sectionIterator.nextByteAsInt();
               float visibility = getSectionVisibility(region, sectionId);
               int fadeTime = getFadeTime(relativeNow, fadeDurationMillis, visibility);

               try {
                  uniformBufferManager.writeMeshTimes(regionId, sectionId, fadeTime);
               } catch (IllegalStateException var12) {
                  return;
               }
            }

         }
      }
   }

   private static float getSectionVisibility(RenderRegion region, int sectionId) {
      int centerX = (region.getChunkX() + LocalSectionIndex.unpackX(sectionId)) * 16 + 8;
      int centerY = (region.getChunkY() + LocalSectionIndex.unpackY(sectionId)) * 16 + 8;
      int centerZ = (region.getChunkZ() + LocalSectionIndex.unpackZ(sectionId)) * 16 + 8;
      return TeleportTransitionController.getFallbackTerrainSectionVisibility((double)centerX, (double)centerY, (double)centerZ);
   }

   private static int getFadeTime(int relativeNow, int fadeDurationMillis, float visibility) {
      if (visibility >= 0.999F) {
         return relativeNow - fadeDurationMillis;
      } else {
         return visibility <= 0.001F ? relativeNow : relativeNow - Math.round((float)fadeDurationMillis * visibility);
      }
   }

   private static int getRelativeRegionTime(RenderRegion region) {
      long relativeNow = System.currentTimeMillis() - region.getCreationTime();
      if (relativeNow <= -2147483648L) {
         return -2147483648;
      } else {
         return relativeNow >= 2147483647L ? 2147483647 : (int)relativeNow;
      }
   }

   private static boolean shouldInvalidateCachedBatchesForHardCut(TerrainRenderPass renderPass, boolean hardCutCulling) {
      if (!TeleportTransitionController.shouldHardCutFallbackTerrain()) {
         gtalikeTeleport$lastHardCutCullingStates.clear();
         gtalikeTeleport$lastCullingInvalidationTicks.clear();
         return false;
      } else {
         Boolean previousHardCutCulling = (Boolean)gtalikeTeleport$lastHardCutCullingStates.get(renderPass);
         if (previousHardCutCulling == null) {
            gtalikeTeleport$lastHardCutCullingStates.put(renderPass, hardCutCulling);
            gtalikeTeleport$lastCullingInvalidationTicks.remove(renderPass);
            return hardCutCulling;
         } else if (previousHardCutCulling != hardCutCulling) {
            gtalikeTeleport$lastHardCutCullingStates.put(renderPass, hardCutCulling);
            gtalikeTeleport$lastCullingInvalidationTicks.remove(renderPass);
            return true;
         } else if (!hardCutCulling) {
            return false;
         } else {
            int tick = TeleportTransitionController.getTicks();
            Integer previousTick = (Integer)gtalikeTeleport$lastCullingInvalidationTicks.get(renderPass);
            if (previousTick != null && previousTick == tick) {
               return false;
            } else {
               gtalikeTeleport$lastCullingInvalidationTicks.put(renderPass, tick);
               return true;
            }
         }
      }
   }

   private static void invalidateCachedBatchForHardCut(RenderRegion region, TerrainRenderPass renderPass, boolean hardCutCulling) {
      if (region != null) {
         MultiDrawBatch batch = region.getCachedBatch(renderPass);
         if (batch != null) {
            if (hardCutCulling && batch.isEmpty()) {
               return;
            }

            batch.clear();
         }

      }
   }

   private static void recoverEmptyCachedBatchForHardCut(RenderRegion region, TerrainRenderPass renderPass) {
      if (region != null) {
         MultiDrawBatch batch = region.getCachedBatch(renderPass);
         if (batch != null && batch.isFilled && batch.isEmpty()) {
            batch.clear();
         }

      }
   }

   private static boolean shouldHardCutSection(RenderRegion region, int sectionId) {
      if (region != null && TeleportTransitionController.shouldHardCutFallbackTerrain() && !TeleportTransitionController.shouldUseShaderScreenMaskOnly()) {
         int centerX = (region.getChunkX() + LocalSectionIndex.unpackX(sectionId)) * 16 + 8;
         int centerY = (region.getChunkY() + LocalSectionIndex.unpackY(sectionId)) * 16 + 8;
         int centerZ = (region.getChunkZ() + LocalSectionIndex.unpackZ(sectionId)) * 16 + 8;
         return TeleportTransitionController.shouldCullFallbackTerrainSection((double)centerX, (double)centerY, (double)centerZ);
      } else {
         return false;
      }
   }

   private static int getChunkSectionFadeMillis() {
      double seconds = (Double)Minecraft.getInstance().options.chunkSectionFadeInTime().get();
      return Math.max(150, (int)Math.round(seconds * 1000.0));
   }

   private static UniformBufferManager getUniformBufferManager() {
      SodiumWorldRenderer renderer;
      try {
         renderer = SodiumWorldRenderer.instanceNullable();
      } catch (LinkageError var5) {
         return null;
      }

      if (renderer == null) {
         return null;
      } else {
         try {
            Field field = getUniformBufferManagerField();
            Object value = field.get(renderer);
            if (value instanceof UniformBufferManager) {
               UniformBufferManager uniformBufferManager = (UniformBufferManager)value;
               return uniformBufferManager;
            } else {
               return null;
            }
         } catch (LinkageError | ReflectiveOperationException var4) {
            return null;
         }
      }
   }

   private static Field getUniformBufferManagerField() throws ReflectiveOperationException {
      if (gtalikeTeleport$uniformBufferManagerField == null) {
         Field field = SodiumWorldRenderer.class.getDeclaredField("uniformBufferManager");
         field.setAccessible(true);
         gtalikeTeleport$uniformBufferManagerField = field;
      }

      return gtalikeTeleport$uniformBufferManagerField;
   }
}
