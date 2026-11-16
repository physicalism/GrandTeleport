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
- `grand-teleport`: five decompiler artefacts fixed by hand (see Git history; the
  snap-offset fix was checked against the original bytecode).
- 16 November 2026, on Henry's Mac (JDK 25): `build-offline.py --verify` printed `MATCH` for
  both mods (56 and 2 classes). That compares every class, field and method signature and
  every resource hash. Method bodies are recompiled, not byte-identical.

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

## Teleport LOD bridge (16 November 2026, untested in game)

Goal: the destination a Grand Teleport cinematic shows should be loaded before the camera zooms
in. All teleports, all players, with at most 4 s of extra wait.

- **Server** (`DestinationPreloader`, `GtaLikeTeleportServer`): when a server-side teleport is
  intercepted (`/tp` run by the server, warps, Waystones plates), the destination's chunks
  start loading immediately with the vanilla `PORTAL` ticket (radius 4, expires by itself after
  300 ticks). When the client confirms its zoom-out, the move is held until the load finishes,
  for at most 80 ticks; the existing 200-tick safety timeout still applies.
- **Client-started teleports** (typed commands, which bypass the server hold): the client sends
  `gtalike_teleport:prepare_destination` at the start of the zoom-out. The server rate-limits
  this per player (2 s for gamemasters, 5 s for others) and caps it at 8 preloads in flight
  server-wide.
- **Client** (`TeleportTransitionController`): before the zoom-in, the camera keeps hovering
  until the player has arrived and the chunks within 3 of the arrival point have been present
  for 6 ticks, for at most 80 ticks.
- **lodbridge.2**: the hover before the zoom-in also waits for Distant Horizons to settle.
  `DistantHorizonsCompat.getLodLoadBacklog()` reads, without changing anything, DH 3.3.4's
  `LodQuadTree` retrieval queue, its sections waiting to reload, and GPU uploads in flight; at 4
  or fewer queued items it counts as settled. If the probe can't find those internals it switches
  itself off and only the chunk check applies. The same 80-tick cap still applies. Every
  teleport logs `[Grand Teleport] Zoom-in after N/80 hold ticks …` (client) and
  `Held teleport of … for N ticks …` (server).
- Both directions are backward-compatible: either side without this build simply skips it.
- Distant Horizons: DH 3.3.4 has no API to prioritise a region or push LODs to a client.
  Clients request LODs around their own position and the server rejects out-of-range requests,
  so DH detail at the destination still loads after arrival. Preloading the chunks should give
  DH's server data a head start, but that's unverified. DH's own `/dh pregen`
  (`PregenManager`) is the way to pre-build server LOD data for the play area.

Test build: `build/offline/GrandTeleport-26.3-build252-rb-cinematic-terrain+lodbridge.1.jar`
(version set at build time; `gradle.properties` unchanged).

## GitHub history

Hosted on the `realms-beyond-26.3` branch of the
[Grand Teleport fork](https://github.com/physicalism/GrandTeleport).
The original reconstruction commits remain intact. This branch has a separate
root history from upstream; no history rewrite or synthetic merge was performed.
The older client-only port is on `realms-client-26.3`.
