package dev.codex.gtaliketeleport;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;
import net.minecraft.resources.ResourceKey;

final class DimensionIds {
   static final String OVERWORLD = "minecraft:overworld";
   static final String NETHER = "minecraft:the_nether";
   static final String END = "minecraft:the_end";

   private DimensionIds() {
   }

   static String fromResourceKey(ResourceKey<?> key) {
      if (key == null) {
         return null;
      } else {
         String[] var1 = new String[]{"location", "getValue", "getLocation"};
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            String methodName = var1[var3];

            try {
               Method method = key.getClass().getMethod(methodName);
               Object result = method.invoke(key);
               String normalized = normalize(result == null ? null : result.toString());
               if (normalized != null) {
                  return normalized;
               }
            } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException var8) {
            }
         }

         return normalize(key.toString());
      }
   }

   static String normalize(String raw) {
      if (raw == null) {
         return null;
      } else {
         String value = raw.trim().toLowerCase(Locale.ROOT);
         if (value.isEmpty()) {
            return null;
         } else if (value.contains("minecraft:overworld")) {
            return "minecraft:overworld";
         } else if (value.contains("minecraft:the_nether")) {
            return "minecraft:the_nether";
         } else if (value.contains("minecraft:the_end")) {
            return "minecraft:the_end";
         } else {
            String var10000;
            switch (value) {
               case "overworld":
               case "minecraft:overworld":
                  var10000 = "minecraft:overworld";
                  break;
               case "nether":
               case "the_nether":
               case "minecraft:nether":
               case "minecraft:the_nether":
                  var10000 = "minecraft:the_nether";
                  break;
               case "end":
               case "the_end":
               case "minecraft:end":
               case "minecraft:the_end":
                  var10000 = "minecraft:the_end";
                  break;
               default:
                  var10000 = value;
            }

            return var10000;
         }
      }
   }
}
