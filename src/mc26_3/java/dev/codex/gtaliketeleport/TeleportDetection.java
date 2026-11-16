package dev.codex.gtaliketeleport;

/** Position corrections have no vanilla reason flag: use a conservative 8-block threshold. */
public final class TeleportDetection {
    private TeleportDetection() { }
    public static boolean shouldAnimate(int playerAge, double distanceSquared, boolean dead) {
        return playerAge >= 40 && !dead && Double.isFinite(distanceSquared) && distanceSquared >= 64.0;
    }
}
