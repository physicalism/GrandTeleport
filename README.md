# Realms Beyond Teleport

Source for the two client mods installed on the realm:

| Subproject | Mod id | Version | Reference JAR |
|---|---|---|---|
| `grand-teleport/` | `gtalike_teleport` | `26.3-build252-rb-cinematic-terrain` | `GrandTeleport-26.3-build252-RB-CinematicTerrain.jar` (sha256 `15468c5e…`) |
| `dh-bridge/` | `gtp_dh_bridge` | `0.3.0+rendering-handoff` | `GrandTeleport-DHBridge-v0.3.0-FastBuffers.jar` (sha256 `bc1a435e…`) |

Minecraft 26.3, Fabric Loader 0.20.1, Fabric API 0.166.2+26.3, Java 25.

## Where this source came from

The 14 November archives (`RealmsBeyond-Teleport-Suite-26.3-v0.3.0-FINAL.zip`,
`RealmsBeyond-Teleport-DH-Rendering-Fix-v0.3.0.zip`) were not found on this Mac on 16
November 2026. The only existing source, `~/Desktop/Project/grand-teleport-26.3/src/mc26_3`,
is the older client-only `1.0.0+26.3` port: it lacks the server entrypoint, server networking,
`ServerPlayerMixin` and the Waystones warp-plate support, and nearly every shared class differs
from build252.

The source here was therefore reconstructed by decompiling the installed JARs with Fabric
Fernflower 2.0.0. Minecraft 26.3 is unobfuscated and the JARs were not remapped, so all
class, method and field names are original; local-variable names are not (`var1`, `var2` …),
and comments are lost. Resources (lang, sounds, icon, mixin config, `fabric.mod.json`) are
copied byte-for-byte from the JARs, with only the version string replaced by `${version}`.

Verification done when this was set up:

- `dh-bridge`: recompiled (javac 21) and compared with `javap`: identical class set and
  identical field/method signatures. Bytecode differs only in local-variable slots and jump
  offsets.
- `grand-teleport`: decompiled with no decompiler failure markers; class set matches the JAR
  (38 top-level classes, including 15 client and 2 common mixins). **Not yet compiled**: no build machine
  had Minecraft 26.3 and JDK 25 available. Run the check below.

## Building

### Offline (recommended first; same approach as the old `build-port.py`)

Needs JDK 25 (Temurin) and the Modrinth App's installed 26.3 files.

```sh
python3 build-offline.py --verify \
  "$HOME/Library/Application Support/ModrinthApp/profiles/emotivist_s server/mods/GrandTeleport-26.3-build252-RB-CinematicTerrain.jar" \
  "$HOME/Library/Application Support/ModrinthApp/profiles/emotivist_s server/mods/GrandTeleport-DHBridge-v0.3.0-FastBuffers.jar"
```

JARs go to `build/offline/`. `--verify` compares every class's fields and method signatures
and every resource's hash against the installed JARs and prints `MATCH` or the differences.
Builds are reproducible: fixed timestamps and sorted entries, so the same inputs give the
same SHA-256.

### Gradle

```sh
./gradlew build          # JARs in */build/libs/
```

Uses Loom 1.19.4 (`net.fabricmc.fabric-loom`, the non-remapping plugin for unobfuscated
versions) and Gradle 9.2.0. Sodium and Mod Menu are compile-only and are taken from the
installed mods folder (`rb_mods_dir` in `gradle.properties`, override with
`-Prb_mods_dir=…`), because their exact Maven coordinates for these builds are unknown.
This Gradle setup has not been run yet.

## Not to be confused with

`~/Desktop/Project/grand-teleport-26.3` is the upstream multi-version Grand Teleport repo
(1.20.1–26.3) with the older client-only port. It is kept as it was.
