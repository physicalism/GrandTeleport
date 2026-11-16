package dev.codex.gtaliketeleport;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public final class WaystonesWarpPlateHandler {
   private WaystonesWarpPlateHandler() {
   }

   public static boolean handleWarpPlateTeleport(Object warpPlate, Entity entity, Object target, ItemStack stack) {
      if (entity instanceof ServerPlayer player) {
         if (warpPlate != null && target != null && isValidWaystone(target)) {
            Method teleportMethod = findTeleportToTargetMethod(warpPlate, player, target, stack);
            BlockPos pos = getWaystonePos(target);
            if (teleportMethod != null && pos != null) {
               if (!GtaLikeTeleportConfig.isWarpPlateTransitionsEnabled()) {
                  GtaLikeTeleportServer.markNextServerTeleportBypassed(player);
                  return false;
               }

               Vec3 targetFeet = new Vec3((double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5);
               return GtaLikeTeleportServer.scheduleServerTransition(player, 2, targetFeet, getWaystoneDimension(player, target), () -> {
                  runWarpPlateTeleport(warpPlate, teleportMethod, player, target, stack);
               });
            }

            return false;
         }
      }

      return false;
   }

   private static boolean isValidWaystone(Object target) {
      try {
         Object result = target.getClass().getMethod("isValid").invoke(target);
         boolean var10000;
         if (result instanceof Boolean valid) {
            if (!valid) {
               var10000 = false;
               return var10000;
            }
         }

         var10000 = true;
         return var10000;
      } catch (ReflectiveOperationException var3) {
         return true;
      }
   }

   private static BlockPos getWaystonePos(Object target) {
      try {
         Object result = target.getClass().getMethod("getPos").invoke(target);
         BlockPos var10000;
         if (result instanceof BlockPos pos) {
            var10000 = pos;
         } else {
            var10000 = null;
         }

         return var10000;
      } catch (ReflectiveOperationException var3) {
         return null;
      }
   }

   private static ResourceKey<Level> getWaystoneDimension(ServerPlayer player, Object target) {
      try {
         Object result = target.getClass().getMethod("getDimension").invoke(target);
         if (result instanceof ResourceKey<?> key) {
            return key;
         }
      } catch (ClassCastException | ReflectiveOperationException var5) {
      }

      return player.level().dimension();
   }

   private static Method findTeleportToTargetMethod(Object warpPlate, ServerPlayer player, Object target, ItemStack stack) {
      for(Class<?> current = warpPlate.getClass(); current != null; current = current.getSuperclass()) {
         Method[] var5 = current.getDeclaredMethods();
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            Method method = var5[var7];
            if (method.getName().equals("teleportToTarget") && method.getParameterCount() == 3) {
               Class<?>[] parameters = method.getParameterTypes();
               if (parameters[0].isInstance(player) && parameters[1].isInstance(target) && parameters[2].isInstance(stack)) {
                  method.setAccessible(true);
                  return method;
               }
            }
         }
      }

      return null;
   }

   private static void runWarpPlateTeleport(Object warpPlate, Method teleportMethod, ServerPlayer player, Object target, ItemStack stack) {
      GtaLikeTeleportServer.runWithServerTeleportBypass(player, () -> {
         try {
            teleportMethod.invoke(warpPlate, player, target, stack);
         } catch (InvocationTargetException | IllegalAccessException var6) {
         }

      });
   }
}
