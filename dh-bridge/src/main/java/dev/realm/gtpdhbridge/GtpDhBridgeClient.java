package dev.realm.gtpdhbridge;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.Event;

public final class GtpDhBridgeClient implements ClientModInitializer {
   private static final String CONTROLLER = "dev.codex.gtaliketeleport.TeleportTransitionController";
   private static final String CULLING = "com.seibel.distanthorizons.core.config.Config$Client$Advanced$Graphics$Culling";
   private static final String QUALITY = "com.seibel.distanthorizons.core.config.Config$Client$Advanced$Graphics$Quality";
   private static final String CONFIG_ENTRY = "com.seibel.distanthorizons.core.config.types.ConfigEntry";
   private static final String TICKS = "net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents";
   private static final String END_TICK = "net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents$EndTick";
   private Method isRunning;
   private Method getValue;
   private Method setWithoutSaving;
   private Object overdraw;
   private Object cameraQuality;
   private Float originalOverdraw;
   private Boolean originalCameraQuality;
   private boolean patched;
   private int releaseTicks;
   private boolean failed;

   public void onInitializeClient() {
      try {
         Class var1 = Class.forName("net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents$EndTick");
         Class var2 = Class.forName("net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents");
         Object var3 = var2.getField("END_CLIENT_TICK").get((Object)null);
         InvocationHandler var4 = (var1x, var2x, var3x) -> {
            if (var2x.getDeclaringClass() == Object.class) {
               Object var10000;
               switch (var2x.getName()) {
                  case "toString":
                     var10000 = "GtpDhBridgeTickListener";
                     break;
                  case "hashCode":
                     var10000 = System.identityHashCode(var1x);
                     break;
                  case "equals":
                     var10000 = var1x == var3x[0];
                     break;
                  default:
                     var10000 = null;
               }

               return var10000;
            } else {
               this.onTick();
               return null;
            }
         };
         Object var5 = Proxy.newProxyInstance(var1.getClassLoader(), new Class[]{var1}, var4);
         Method var6 = Event.class.getMethod("register", Object.class);
         var6.invoke(var3, var5);
         System.out.println("[GTP-DH Bridge] Registered client tick listener.");
      } catch (Throwable var7) {
         this.failed = true;
         System.err.println("[GTP-DH Bridge] Could not register: " + String.valueOf(var7));
      }

   }

   private void resolve() throws ReflectiveOperationException {
      if (this.isRunning == null) {
         Class var1 = Class.forName("dev.codex.gtaliketeleport.TeleportTransitionController");
         this.isRunning = var1.getDeclaredMethod("isRunning");
         this.isRunning.setAccessible(true);
         Class var2 = Class.forName("com.seibel.distanthorizons.core.config.types.ConfigEntry");
         this.getValue = var2.getMethod("get");
         this.setWithoutSaving = var2.getMethod("setWithoutSaving", Object.class);
         this.overdraw = getStatic("com.seibel.distanthorizons.core.config.Config$Client$Advanced$Graphics$Culling", "overdrawPrevention");
         this.cameraQuality = getStatic("com.seibel.distanthorizons.core.config.Config$Client$Advanced$Graphics$Quality", "useCameraPositionForQualityDropOff");
         if (this.overdraw == null || this.cameraQuality == null) {
            throw new IllegalStateException("A Distant Horizons setting is not initialised");
         }
      }
   }

   private static Object getStatic(String var0, String var1) throws ReflectiveOperationException {
      Field var2 = Class.forName(var0).getField(var1);
      return var2.get((Object)null);
   }

   private void onTick() {
      SmartBufferBoost.onTick();
      if (!this.failed) {
         try {
            this.resolve();
            boolean var1 = Boolean.TRUE.equals(this.isRunning.invoke((Object)null));
            if (var1) {
               this.releaseTicks = 0;
               if (!this.patched) {
                  this.originalOverdraw = ((Number)this.getValue.invoke(this.overdraw)).floatValue();
                  this.originalCameraQuality = (Boolean)this.getValue.invoke(this.cameraQuality);
                  this.patched = true;
                  if (Float.compare(this.originalOverdraw, 0.0F) != 0) {
                     this.setWithoutSaving.invoke(this.overdraw, 0.0F);
                  }

                  if (!this.originalCameraQuality) {
                     this.setWithoutSaving.invoke(this.cameraQuality, Boolean.TRUE);
                  }

                  System.out.println("[GTP-DH Bridge] Temporary DH overdraw=0; camera-relative LODs enabled.");
               }
            } else if (this.patched && ++this.releaseTicks >= 20) {
               this.restore();
            }
         } catch (Throwable var4) {
            System.err.println("[GTP-DH Bridge] Disabling bridge after error: " + String.valueOf(var4));

            try {
               this.restore();
            } catch (Throwable var3) {
            }

            this.failed = true;
         }

      }
   }

   private void restore() throws ReflectiveOperationException {
      if (this.patched) {
         if (this.originalOverdraw != null && Float.compare(((Number)this.getValue.invoke(this.overdraw)).floatValue(), this.originalOverdraw) != 0) {
            this.setWithoutSaving.invoke(this.overdraw, this.originalOverdraw);
         }

         if (this.originalCameraQuality != null && !this.originalCameraQuality.equals(this.getValue.invoke(this.cameraQuality))) {
            this.setWithoutSaving.invoke(this.cameraQuality, this.originalCameraQuality);
         }

         this.patched = false;
         this.releaseTicks = 0;
         System.out.println("[GTP-DH Bridge] Restored user's DH settings.");
      }
   }
}
