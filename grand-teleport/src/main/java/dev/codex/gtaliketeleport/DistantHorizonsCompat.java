package dev.codex.gtaliketeleport;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicReference;

final class DistantHorizonsCompat {
   private static final String CLIENT_CONFIG_CLASS = "com.seibel.distanthorizons.core.config.Config$Client";
   private static final String CONFIG_ENTRY_CLASS = "com.seibel.distanthorizons.core.config.types.ConfigEntry";
   private static final String SHARED_API_CLASS = "com.seibel.distanthorizons.core.api.internal.SharedApi";
   private static final String QUAD_TREE_CLASS = "com.seibel.distanthorizons.core.render.QuadTree.LodQuadTree";
   private static final String RENDER_SECTION_CLASS = "com.seibel.distanthorizons.core.render.QuadTree.LodRenderSection";

   private static boolean backlogProbeResolved;
   private static boolean backlogProbeBroken;
   private static Method tryGetDhClientWorld;
   private static Field retrievalQueueField;
   private static Field sectionsToReloadField;
   private static Field enabledSectionsField;
   private static Method gpuUploadInProgress;

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

   /**
    * Teleport LOD bridge: how much LOD work DH's client still has queued, summed over loaded levels.
    * Counts sections waiting to be read from DH's database or rebuilt, plus GPU uploads in flight.
    * Returns -1 when unknown (DH missing or its internals differ); callers treat that as "ready".
    * Written against DH 3.3.4 internals. It only reads them, by reflection, and changes nothing.
    */
   static int getLodLoadBacklog() {
      if (backlogProbeBroken) {
         return -1;
      }

      try {
         resolveBacklogProbe();
         Object world = tryGetDhClientWorld.invoke(null);
         if (world == null) {
            return -1;
         }

         Object levels = world.getClass().getMethod("getAllLoadedLevels").invoke(world);
         if (!(levels instanceof Iterable<?> iterable)) {
            return -1;
         }

         int backlog = 0;
         boolean sawQuadTree = false;
         for (Object level : iterable) {
            Object quadTree = quadTreeOf(level);
            if (quadTree == null) {
               continue;
            }

            sawQuadTree = true;
            ThreadPoolExecutor retrieval = (ThreadPoolExecutor)retrievalQueueField.get(quadTree);
            if (retrieval != null) {
               backlog += retrieval.getQueue().size() + retrieval.getActiveCount();
            }

            Collection<?> toReload = (Collection<?>)sectionsToReloadField.get(quadTree);
            if (toReload != null) {
               backlog += toReload.size();
            }

            List<?> enabled = (List<?>)enabledSectionsField.get(quadTree);
            if (enabled != null) {
               for (Object section : enabled.toArray()) {
                  if (section != null && Boolean.TRUE.equals(gpuUploadInProgress.invoke(section))) {
                     ++backlog;
                  }
               }
            }
         }

         return sawQuadTree ? backlog : -1;
      } catch (LinkageError | ReflectiveOperationException | ClassCastException e) {
         backlogProbeBroken = true;
         System.err.println("[Grand Teleport] Distant Horizons load probe unavailable, not waiting for DH: " + e);
         return -1;
      } catch (RuntimeException e) {
         // A list changed under us on one of DH's threads; try again next tick.
         return -1;
      }
   }

   private static Object quadTreeOf(Object level) throws ReflectiveOperationException {
      Field clientside;
      try {
         clientside = level.getClass().getField("clientside");
      } catch (NoSuchFieldException e) {
         return null;
      }

      Object module = clientside.get(level);
      if (module == null) {
         return null;
      }

      Object stateRef = module.getClass().getField("ClientRenderStateRef").get(module);
      Object state = stateRef instanceof AtomicReference<?> ref ? ref.get() : null;
      return state == null ? null : state.getClass().getField("quadtree").get(state);
   }

   private static void resolveBacklogProbe() throws ReflectiveOperationException {
      if (backlogProbeResolved) {
         return;
      }

      tryGetDhClientWorld = Class.forName(SHARED_API_CLASS).getMethod("tryGetDhClientWorld");
      Class<?> quadTree = Class.forName(QUAD_TREE_CLASS);
      retrievalQueueField = quadTree.getDeclaredField("fullDataRetrievalQueueThread");
      sectionsToReloadField = quadTree.getDeclaredField("sectionsToReload");
      enabledSectionsField = quadTree.getDeclaredField("enabledSections");
      retrievalQueueField.setAccessible(true);
      sectionsToReloadField.setAccessible(true);
      enabledSectionsField.setAccessible(true);
      gpuUploadInProgress = Class.forName(RENDER_SECTION_CLASS).getMethod("gpuUploadInProgress");
      backlogProbeResolved = true;
   }
}
