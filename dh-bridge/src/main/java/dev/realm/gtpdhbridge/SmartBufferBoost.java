package dev.realm.gtpdhbridge;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

public final class SmartBufferBoost {
   private static Method isRunning;
   private static Method get;
   private static Method setWithoutSaving;
   private static Object gpuUploadDelay;
   private static Object original;
   private static Object installed;
   private static boolean touched;
   private static boolean active;
   private static boolean disabled;
   private static boolean available;
   private static int releaseTicks;
   private static final int RESTORE_AFTER_TICKS = 20;

   private SmartBufferBoost() {
   }

   private static void resolve() throws ReflectiveOperationException {
      if (isRunning == null) {
         Class var0 = Class.forName("dev.codex.gtaliketeleport.TeleportTransitionController");
         Method var1 = var0.getDeclaredMethod("isRunning");
         var1.setAccessible(true);
         Class var2 = Class.forName("com.seibel.distanthorizons.core.config.types.ConfigEntry");
         Method var3 = var2.getMethod("get");
         Method var4 = var2.getMethod("setWithoutSaving", Object.class);
         Object var5 = null;

         try {
            Class var6 = Class.forName("com.seibel.distanthorizons.core.config.Config$Client$Advanced$Buffers");
            Field var7 = var6.getField("gpuUploadPerMegabyteInMilliseconds");
            var5 = var7.get((Object)null);
         } catch (NoSuchFieldException | ClassNotFoundException var8) {
         }

         isRunning = var1;
         get = var3;
         setWithoutSaving = var4;
         gpuUploadDelay = var5;
         available = var5 != null;
      }
   }

   public static void onTick() {
      if (!disabled) {
         try {
            resolve();
            boolean var0 = Boolean.TRUE.equals(isRunning.invoke((Object)null));
            if (var0) {
               releaseTicks = 0;
               if (!active) {
                  active = true;
                  original = null;
                  installed = null;
                  touched = false;
                  if (available) {
                     original = get.invoke(gpuUploadDelay);
                     installed = zeroLike(original);
                     if (installed != null && !Objects.equals(original, installed)) {
                        setWithoutSaving.invoke(gpuUploadDelay, installed);
                        touched = true;
                        System.out.println("[GTP-DH Bridge] Temporarily removed DH GPU-upload delay; quality settings unchanged.");
                     }
                  }
               }
            } else if (active && ++releaseTicks >= 20) {
               restore();
            }
         } catch (Throwable var3) {
            System.err.println("[GTP-DH Bridge] Optional buffer tuning disabled: " + String.valueOf(var3));

            try {
               restore();
            } catch (Throwable var2) {
               System.err.println("[GTP-DH Bridge] Buffer setting restoration failed: " + String.valueOf(var2));
            }

            disabled = true;
            available = false;
            gpuUploadDelay = null;
            active = false;
         }

      }
   }

   private static Object zeroLike(Object var0) {
      if (var0 instanceof Integer) {
         return 0;
      } else if (var0 instanceof Long) {
         return 0L;
      } else if (var0 instanceof Float) {
         return 0.0F;
      } else {
         return var0 instanceof Double ? 0.0 : null;
      }
   }

   private static void restore() throws ReflectiveOperationException {
      if (touched && gpuUploadDelay != null) {
         Object var0 = get.invoke(gpuUploadDelay);
         if (Objects.equals(var0, installed)) {
            setWithoutSaving.invoke(gpuUploadDelay, original);
            System.out.println("[GTP-DH Bridge] Restored DH GPU-upload delay.");
         }
      }

      touched = false;
      active = false;
      releaseTicks = 0;
      original = null;
      installed = null;
   }
}
