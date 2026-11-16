package dev.codex.gtaliketeleport;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

final class DistantHorizonsCompat {
   private static final String CLIENT_CONFIG_CLASS = "com.seibel.distanthorizons.core.config.Config$Client";
   private static final String CONFIG_ENTRY_CLASS = "com.seibel.distanthorizons.core.config.types.ConfigEntry";

   private DistantHorizonsCompat() {
   }

   static boolean isRenderingEnabled() {
      try {
         Class<?> configClass = Class.forName("com.seibel.distanthorizons.core.config.Config$Client");
         Field field = configClass.getField("quickEnableRendering");
         Object entry = field.get((Object)null);
         if (entry == null) {
            return false;
         } else {
            Method get = Class.forName("com.seibel.distanthorizons.core.config.types.ConfigEntry").getMethod("get");
            return Boolean.TRUE.equals(get.invoke(entry));
         }
      } catch (LinkageError | ReflectiveOperationException var4) {
         return true;
      }
   }
}
