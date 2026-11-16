package dev.codex.gtaliketeleport;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import java.lang.reflect.Field;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.FogRenderer;
import net.minecraft.client.renderer.state.GameRenderState;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3x2f;
import org.joml.Matrix3x2fc;
import org.joml.Vector3fc;
import org.joml.Vector4f;

public final class TeleportStepEffectRenderer {
   private static final double DEFAULT_FOV_DEGREES = 70.0;
   private static final double FALLBACK_MASK_CAMERA_HEIGHT = 150.0;
   private static final double CHUNK_SCREEN_MARGIN = 32.0;
   private static final int START_COLOR_CAPTURE_TICK = 30;
   private static final float SKY_TRANSITION_FOG_BLEND = 0.86F;
   private static Field fogRendererField;
   private static Field guiRenderStateField;
   private static boolean guiRenderStateUnavailable;
   private static int[] capturedStartMaskColor;

   private TeleportStepEffectRenderer() {
   }

   public static void render(GuiGraphicsExtractor context, DeltaTracker deltaTracker) {
      updateStartMaskColorCapture(deltaTracker);
      float tickProgress = deltaTracker.getGameTimeDeltaPartialTick(false);
      boolean shaderScreenMaskOnly = TeleportTransitionController.shouldUseShaderScreenMaskOnly();
      float maskIntensity = TeleportTransitionController.getShaderScreenMaskIntensity(tickProgress);
      if (maskIntensity > 0.0F) {
         renderChunkScreenMask(context, deltaTracker, tickProgress, maskIntensity);
      }

      float intensity;
      if (shaderScreenMaskOnly) {
         intensity = TeleportTransitionController.getCameraMotionStepEffectIntensity(tickProgress);
      } else {
         intensity = TeleportTransitionController.getStepEffectIntensity(tickProgress);
         intensity = Math.max(intensity, TeleportTransitionController.getHudFadeOverlayIntensity(tickProgress));
      }

      renderStepFlash(context, intensity);
   }

   private static void renderStepFlash(GuiGraphicsExtractor context, float intensity) {
      if (!(intensity <= 0.0F)) {
         int flashAlpha = (int)(68.0F * intensity);
         context.fill(0, 0, context.guiWidth(), context.guiHeight(), argb(flashAlpha, 245, 245, 235));
      }
   }

   private static void updateStartMaskColorCapture(DeltaTracker deltaTracker) {
      if (!TeleportTransitionController.isRunning()) {
         capturedStartMaskColor = null;
      } else if (capturedStartMaskColor == null && TeleportTransitionController.shouldUseShaderScreenMaskOnly() && TeleportTransitionController.getTicks() >= 30) {
         capturedStartMaskColor = getSkyMaskColor(deltaTracker);
      }
   }

   private static int[] getSkyMaskColor(DeltaTracker deltaTracker) {
      int[] horizonColor = getRenderStateSkyMaskColor();
      if (horizonColor != null) {
         return horizonColor;
      } else {
         Minecraft client = Minecraft.getInstance();
         if (client.level != null && client.gameRenderer != null) {
            try {
               Object value = getFogRendererField().get(client.gameRenderer);
               if (value instanceof FogRenderer) {
                  FogRenderer fogRenderer = (FogRenderer)value;
                  FogData fogData = fogRenderer.setupFog(client.gameRenderer.mainCamera(), getFogViewDistance(client), deltaTracker, 0.0F, client.level);
                  return softenMaskColor(fogData.color);
               }
            } catch (LinkageError | ReflectiveOperationException var6) {
            }
         }

         return new int[]{188, 197, 202};
      }
   }

   private static int[] getRenderStateSkyMaskColor() {
      Minecraft client = Minecraft.getInstance();
      if (client.level != null && client.gameRenderer != null) {
         try {
            GameRenderState gameState = client.gameRenderer.gameRenderState();
            LevelRenderState levelState = gameState.levelRenderState;
            Vector3fc skyVec = levelState.skyRenderState.skyColor;
            int skyColor = ARGB.colorFromFloat(1.0F, skyVec.x(), skyVec.y(), skyVec.z());
            int fogColor = skyColor;
            CameraRenderState cameraState = levelState.cameraRenderState;
            if (cameraState != null && cameraState.fogData != null) {
               fogColor = opaqueColor(cameraState.fogData.color);
            }

            float transitionAmount = levelState.skyRenderState.sunriseAndSunsetColor.w();
            int color = blendSkyTowardTransitionColor(skyColor, fogColor, transitionAmount);
            return neutralMaskColor(color);
         } catch (LinkageError | RuntimeException var9) {
            return null;
         }
      } else {
         return null;
      }
   }

   private static int blendSkyTowardTransitionColor(int skyColor, int fogColor, float transitionAmount) {
      if (transitionAmount <= 0.001F) {
         return skyColor;
      } else {
         int transitionBase = lessSaturatedColor(skyColor, fogColor);
         float blend = Mth.clamp(transitionAmount * 0.86F, 0.0F, 0.86F);
         return ARGB.srgbLerp(blend, skyColor, transitionBase);
      }
   }

   private static int lessSaturatedColor(int first, int second) {
      return saturation(first) <= saturation(second) ? first : second;
   }

   private static int[] neutralMaskColor(int color) {
      int value = clamp(Math.round((float)ARGB.red(color) * 0.2126F + (float)ARGB.green(color) * 0.7152F + (float)ARGB.blue(color) * 0.0722F));
      return new int[]{value, value, value};
   }

   private static float saturation(int color) {
      float red = ARGB.redFloat(color);
      float green = ARGB.greenFloat(color);
      float blue = ARGB.blueFloat(color);
      float max = Math.max(red, Math.max(green, blue));
      float min = Math.min(red, Math.min(green, blue));
      return max - min;
   }

   private static int opaqueColor(Vector4f color) {
      return ARGB.colorFromFloat(1.0F, Mth.clamp(color.x, 0.0F, 1.0F), Mth.clamp(color.y, 0.0F, 1.0F), Mth.clamp(color.z, 0.0F, 1.0F));
   }

   private static void renderChunkScreenMask(GuiGraphicsExtractor context, DeltaTracker deltaTracker, float tickProgress, float maskIntensity) {
      TeleportTransitionController.CameraFrame frame = TeleportTransitionController.getCameraFrame(tickProgress);
      if (frame != null) {
         Minecraft client = Minecraft.getInstance();
         double cameraY = 150.0;
         double worldHalfHeight = Math.tan(Math.toRadians(70.0) * 0.5) * cameraY;
         double worldHalfWidth = worldHalfHeight * (double)context.guiWidth() / Math.max(1.0, (double)context.guiHeight());
         double scaleX = (double)context.guiWidth() / (worldHalfWidth * 2.0);
         double scaleY = (double)context.guiHeight() / (worldHalfHeight * 2.0);
         double yawRadians = Math.toRadians((double)frame.yaw());
         double rightX = Math.cos(yawRadians);
         double rightZ = Math.sin(yawRadians);
         double forwardX = -Math.sin(yawRadians);
         double forwardZ = Math.cos(yawRadians);
         GuiRenderState guiRenderState = getGuiRenderState(context);
         double minWorldX = 1.0 / 0.0;
         double maxWorldX = -1.0 / 0.0;
         double minWorldZ = 1.0 / 0.0;
         double maxWorldZ = -1.0 / 0.0;
         double[] screenWorldXs = new double[]{-worldHalfWidth - 32.0, worldHalfWidth + 32.0, worldHalfWidth + 32.0, -worldHalfWidth - 32.0};
         double[] screenWorldYs = new double[]{-worldHalfHeight - 32.0, -worldHalfHeight - 32.0, worldHalfHeight + 32.0, worldHalfHeight + 32.0};

         int minChunkX;
         for(minChunkX = 0; minChunkX < 4; ++minChunkX) {
            double worldX = frame.pos().x + rightX * screenWorldXs[minChunkX] + forwardX * screenWorldYs[minChunkX];
            double worldZ = frame.pos().z + rightZ * screenWorldXs[minChunkX] + forwardZ * screenWorldYs[minChunkX];
            minWorldX = Math.min(minWorldX, worldX);
            maxWorldX = Math.max(maxWorldX, worldX);
            minWorldZ = Math.min(minWorldZ, worldZ);
            maxWorldZ = Math.max(maxWorldZ, worldZ);
         }

         minChunkX = Mth.floor(minWorldX / 16.0) - 1;
         int maxChunkX = Mth.floor(maxWorldX / 16.0) + 1;
         int minChunkZ = Mth.floor(minWorldZ / 16.0) - 1;
         int maxChunkZ = Mth.floor(maxWorldZ / 16.0) + 1;
         int[] color = getSceneMaskColor(deltaTracker);
         boolean shaderScreenMaskOnly = TeleportTransitionController.shouldUseShaderScreenMaskOnly();

         for(int chunkX = minChunkX; chunkX <= maxChunkX; ++chunkX) {
            for(int chunkZ = minChunkZ; chunkZ <= maxChunkZ; ++chunkZ) {
               double chunkMinWorldX = (double)chunkX * 16.0;
               double chunkMinWorldZ = (double)chunkZ * 16.0;
               float sectionOpacity;
               float opacity;
               if (shaderScreenMaskOnly) {
                  sectionOpacity = TeleportTransitionController.getShaderScreenMaskSectionOpacity(chunkMinWorldX + 8.0, frame.pos().y, chunkMinWorldZ + 8.0);
               } else {
                  opacity = TeleportTransitionController.getFallbackTerrainSectionVisibility(chunkMinWorldX + 8.0, frame.pos().y, chunkMinWorldZ + 8.0);
                  sectionOpacity = 1.0F - opacity;
               }

               opacity = sectionOpacity * maskIntensity;
               if (!(opacity <= 0.001F)) {
                  int alpha = (int)(252.0F * opacity);
                  drawProjectedChunkQuad(context, guiRenderState, frame.pos(), chunkMinWorldX, chunkMinWorldZ, rightX, rightZ, forwardX, forwardZ, scaleX, scaleY, alpha, color);
               }
            }
         }

      }
   }

   private static void drawProjectedChunkQuad(GuiGraphicsExtractor context, GuiRenderState guiRenderState, Vec3 cameraPos, double minWorldX, double minWorldZ, double rightX, double rightZ, double forwardX, double forwardZ, double scaleX, double scaleY, int alpha, int[] color) {
      double maxWorldX = minWorldX + 16.0;
      double maxWorldZ = minWorldZ + 16.0;
      double[] worldXs = new double[]{minWorldX, maxWorldX, maxWorldX, minWorldX};
      double[] worldZs = new double[]{minWorldZ, minWorldZ, maxWorldZ, maxWorldZ};
      double[] screenXs = new double[4];
      double[] screenYs = new double[4];

      for(int i = 0; i < 4; ++i) {
         double dx = worldXs[i] - cameraPos.x;
         double dz = worldZs[i] - cameraPos.z;
         double screenWorldX = dx * rightX + dz * rightZ;
         double screenWorldY = dx * forwardX + dz * forwardZ;
         screenXs[i] = (double)context.guiWidth() * 0.5 + screenWorldX * scaleX;
         screenYs[i] = (double)context.guiHeight() * 0.5 + screenWorldY * scaleY;
      }

      ScreenRectangle bounds = getScreenBounds(screenXs, screenYs, context.guiWidth(), context.guiHeight());
      if (bounds != null) {
         if (guiRenderState != null) {
            int packedColor = argb(alpha, color[0], color[1], color[2]);
            guiRenderState.addGuiElement(new ProjectedQuadRenderState(RenderPipelines.GUI, TextureSetup.noTexture(), new Matrix3x2f(context.pose()), (float)screenXs[0], (float)screenYs[0], (float)screenXs[1], (float)screenYs[1], (float)screenXs[2], (float)screenYs[2], (float)screenXs[3], (float)screenYs[3], packedColor, (ScreenRectangle)null, bounds));
         } else {
            drawProjectedChunkScanlines(context, screenXs, screenYs, alpha, color);
         }
      }
   }

   private static ScreenRectangle getScreenBounds(double[] xs, double[] ys, int screenWidth, int screenHeight) {
      double minScreenX = 1.0 / 0.0;
      double maxScreenX = -1.0 / 0.0;
      double minScreenY = 1.0 / 0.0;
      double maxScreenY = -1.0 / 0.0;

      int x1;
      for(x1 = 0; x1 < 4; ++x1) {
         minScreenX = Math.min(minScreenX, xs[x1]);
         maxScreenX = Math.max(maxScreenX, xs[x1]);
         minScreenY = Math.min(minScreenY, ys[x1]);
         maxScreenY = Math.max(maxScreenY, ys[x1]);
      }

      x1 = Math.max(0, Mth.floor(minScreenX));
      int y1 = Math.max(0, Mth.floor(minScreenY));
      int x2 = Math.min(screenWidth, Mth.ceil(maxScreenX));
      int y2 = Math.min(screenHeight, Mth.ceil(maxScreenY));
      return x2 > x1 && y2 > y1 ? new ScreenRectangle(x1, y1, x2 - x1, y2 - y1) : null;
   }

   private static void drawProjectedChunkScanlines(GuiGraphicsExtractor context, double[] xs, double[] ys, int alpha, int[] color) {
      double minScreenY = Math.min(Math.min(ys[0], ys[1]), Math.min(ys[2], ys[3]));
      double maxScreenY = Math.max(Math.max(ys[0], ys[1]), Math.max(ys[2], ys[3]));
      int y1 = Math.max(0, Mth.floor(minScreenY));
      int y2 = Math.min(context.guiHeight(), Mth.ceil(maxScreenY));
      if (y2 > y1) {
         int fillColor = argb(alpha, color[0], color[1], color[2]);

         for(int y = y1; y < y2; y += 2) {
            double scanY = Math.min((double)y2 - 0.5, (double)y + 1.0);
            double[] intersections = new double[4];
            int count = 0;

            double right;
            for(int i = 0; i < 4; ++i) {
               int next = i + 1 & 3;
               right = ys[i];
               double edgeY2 = ys[next];
               if (right <= scanY && edgeY2 > scanY || edgeY2 <= scanY && right > scanY) {
                  double t = (scanY - right) / (edgeY2 - right);
                  intersections[count++] = xs[i] + (xs[next] - xs[i]) * t;
               }
            }

            if (count >= 2) {
               double left = Math.min(intersections[0], intersections[1]);
               right = Math.max(intersections[0], intersections[1]);
               int x1 = Math.max(0, Mth.floor(left));
               int x2 = Math.min(context.guiWidth(), Mth.ceil(right));
               if (x2 > x1) {
                  context.fill(x1, y, x2, Math.min(y + 2, y2), fillColor);
               }
            }
         }

      }
   }

   private static Field getFogRendererField() throws NoSuchFieldException {
      if (fogRendererField == null) {
         fogRendererField = GameRenderer.class.getDeclaredField("fogRenderer");
         fogRendererField.setAccessible(true);
      }

      return fogRendererField;
   }

   private static GuiRenderState getGuiRenderState(GuiGraphicsExtractor context) {
      if (guiRenderStateUnavailable) {
         return null;
      } else {
         try {
            Object value = getGuiRenderStateField().get(context);
            if (value instanceof GuiRenderState) {
               GuiRenderState guiRenderState = (GuiRenderState)value;
               return guiRenderState;
            }
         } catch (LinkageError | ReflectiveOperationException var3) {
            guiRenderStateUnavailable = true;
         }

         return null;
      }
   }

   private static Field getGuiRenderStateField() throws NoSuchFieldException {
      if (guiRenderStateField == null) {
         guiRenderStateField = GuiGraphicsExtractor.class.getDeclaredField("guiRenderState");
         guiRenderStateField.setAccessible(true);
      }

      return guiRenderStateField;
   }

   private static int getFogViewDistance(Minecraft client) {
      return Math.max(2, client.options == null ? 12 : client.options.getEffectiveRenderDistance());
   }

   private static int[] getSceneMaskColor(DeltaTracker deltaTracker) {
      return capturedStartMaskColor == null ? getSkyMaskColor(deltaTracker) : capturedStartMaskColor;
   }

   private static int[] softenMaskColor(int color) {
      return softenMaskColor(new Vector4f(ARGB.redFloat(color), ARGB.greenFloat(color), ARGB.blueFloat(color), 1.0F));
   }

   private static int[] softenMaskColor(Vector4f color) {
      float red = Mth.clamp(color.x, 0.0F, 1.0F);
      float green = Mth.clamp(color.y, 0.0F, 1.0F);
      float blue = Mth.clamp(color.z, 0.0F, 1.0F);
      float luma = red * 0.2126F + green * 0.7152F + blue * 0.0722F;
      red = red * 0.62F + luma * 0.38F;
      green = green * 0.62F + luma * 0.38F;
      blue = blue * 0.62F + luma * 0.38F;
      red = red * 0.78F + 0.2F;
      green = green * 0.78F + 0.2F;
      blue = blue * 0.78F + 0.2F;
      return new int[]{(int)(Mth.clamp(red, 0.0F, 1.0F) * 255.0F), (int)(Mth.clamp(green, 0.0F, 1.0F) * 255.0F), (int)(Mth.clamp(blue, 0.0F, 1.0F) * 255.0F)};
   }

   private static int argb(int alpha, int red, int green, int blue) {
      return clamp(alpha) << 24 | clamp(red) << 16 | clamp(green) << 8 | clamp(blue);
   }

   private static int clamp(int value) {
      return Math.max(0, Math.min(255, value));
   }

   private static record ProjectedQuadRenderState(RenderPipeline pipeline, TextureSetup textureSetup, Matrix3x2fc pose, float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3, int color, ScreenRectangle scissorArea, ScreenRectangle bounds) implements GuiElementRenderState {
      private ProjectedQuadRenderState(RenderPipeline pipeline, TextureSetup textureSetup, Matrix3x2fc pose, float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3, int color, ScreenRectangle scissorArea, ScreenRectangle bounds) {
         this.pipeline = pipeline;
         this.textureSetup = textureSetup;
         this.pose = pose;
         this.x0 = x0;
         this.y0 = y0;
         this.x1 = x1;
         this.y1 = y1;
         this.x2 = x2;
         this.y2 = y2;
         this.x3 = x3;
         this.y3 = y3;
         this.color = color;
         this.scissorArea = scissorArea;
         this.bounds = bounds;
      }

      public void buildVertices(VertexConsumer vertexConsumer) {
         vertexConsumer.addVertexWith2DPose(this.pose, this.x0, this.y0).setColor(this.color);
         vertexConsumer.addVertexWith2DPose(this.pose, this.x3, this.y3).setColor(this.color);
         vertexConsumer.addVertexWith2DPose(this.pose, this.x2, this.y2).setColor(this.color);
         vertexConsumer.addVertexWith2DPose(this.pose, this.x1, this.y1).setColor(this.color);
      }

      public RenderPipeline pipeline() {
         return this.pipeline;
      }

      public TextureSetup textureSetup() {
         return this.textureSetup;
      }

      public Matrix3x2fc pose() {
         return this.pose;
      }

      public float x0() {
         return this.x0;
      }

      public float y0() {
         return this.y0;
      }

      public float x1() {
         return this.x1;
      }

      public float y1() {
         return this.y1;
      }

      public float x2() {
         return this.x2;
      }

      public float y2() {
         return this.y2;
      }

      public float x3() {
         return this.x3;
      }

      public float y3() {
         return this.y3;
      }

      public int color() {
         return this.color;
      }

      public ScreenRectangle scissorArea() {
         return this.scissorArea;
      }

      public ScreenRectangle bounds() {
         return this.bounds;
      }
   }
}
