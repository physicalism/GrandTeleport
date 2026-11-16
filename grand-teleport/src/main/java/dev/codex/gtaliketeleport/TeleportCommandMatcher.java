package dev.codex.gtaliketeleport;

import java.util.Locale;

final class TeleportCommandMatcher {
   private TeleportCommandMatcher() {
   }

   static boolean isTeleportCommand(String command) {
      return getArgumentString(command) != null;
   }

   static String getArgumentString(String command) {
      String normalized = command.stripLeading();
      if (normalized.startsWith("/")) {
         normalized = normalized.substring(1).stripLeading();
      }

      String lowerCase = normalized.toLowerCase(Locale.ROOT);
      String[] var3 = new String[]{"minecraft:teleport", "minecraft:tp", "teleport", "tp"};
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String name = var3[var5];
         if (lowerCase.equals(name)) {
            return "";
         }

         if (lowerCase.startsWith(name + " ")) {
            return normalized.substring(name.length()).stripLeading();
         }
      }

      if (lowerCase.startsWith("execute ")) {
         int runIndex = lowerCase.indexOf(" run ");
         if (runIndex >= 0) {
            return getArgumentString(normalized.substring(runIndex + 5).stripLeading());
         }
      }

      return null;
   }
}
