#!/usr/bin/env python3
"""Launch an isolated test client using installed libraries; no account tokens."""
import json
import os
from pathlib import Path
import shutil
import subprocess
import sys
import zipfile

ROOT = Path(__file__).resolve().parents[1]
DATA = Path.home() / 'Library/Application Support/ModrinthApp'
META = DATA / 'meta'
RUN = ROOT / 'test-run'
RUN.mkdir(exist_ok=True)
(RUN / 'mods').mkdir(exist_ok=True)
shutil.copyfile(ROOT / 'build/libs/grand-teleport-client-26.3-1.0.0.jar', RUN / 'mods/grand-teleport.jar')
for pattern in ('fabric-api-*.jar', 'modmenu-*.jar', 'iris-fabric-*.jar'):
    for source in (DATA / 'profiles/Fabric Realm/mods').glob(pattern):
        target = RUN / 'mods' / source.name
        if not target.exists():
            target.symlink_to(source)
for old in (RUN / 'mods').glob('sodium*.jar'):
    old.unlink()
shutil.copyfile(RUN / 'sodium-fabric-0.9.2+mc26.3.jar', RUN / 'mods/sodium-fabric-0.9.2+mc26.3.jar')
(RUN / 'shaderpacks').mkdir(exist_ok=True)
shader = DATA / 'profiles/Fabric Realm/shaderpacks/ComplementaryReimagined_r5.9.3.zip'
shutil.copyfile(shader, RUN / 'shaderpacks' / shader.name)
(RUN / 'config').mkdir(exist_ok=True)
(RUN / 'config/iris.properties').write_text('enableShaders=true\nshaderPack=ComplementaryReimagined_r5.9.3.zip\n')
version = '26.3-0.20.1' 
metadata = json.loads((META / f'versions/{version}/{version}.json').read_text())
classpath = []
for library in metadata['libraries']:
    name = library['name'].split(':')
    artifact = library.get('downloads', {}).get('artifact', {}).get('path')
    if artifact is None:
        artifact = f'{name[0].replace(".", "/")}/{name[1]}/{name[2]}/{name[1]}-{name[2]}.jar'
    path = META / 'libraries' / artifact
    if path.is_file():
        classpath.append(str(path))
classpath.append(str(META / f'versions/{version}/{version}.jar'))
if '--transform-only' in sys.argv:
    # Force the target through the real Fabric/Mixin pipeline before the native
    # window is created. This probe is separate from the distributable mod.
    probe = RUN / 'probe'
    probe.mkdir(exist_ok=True)
    source = probe / 'TransformProbe.java'
    source.write_text('''
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
public class TransformProbe implements PreLaunchEntrypoint {
    public void onPreLaunch() {
        try {
            String[][] targets = {
                {"net.minecraft.client.Camera", "overrideCamera"},
                {"net.minecraft.client.renderer.entity.EntityRenderer", "gtalikeTeleport"},
                {"net.minecraft.client.renderer.entity.LivingEntityRenderer", "gtalikeTeleport"},
                {"net.minecraft.client.renderer.chunk.SectionRenderDispatcher$RenderSection", "gtalikeTeleport"},
                {"net.minecraft.client.multiplayer.ClientPacketListener", "detectServerTeleport"},
                {"net.minecraft.client.MouseHandler", "blockMouseLook"},
                {"net.minecraft.client.gui.Gui", "suppressGameMenu"},
                {"net.minecraft.client.renderer.SkyRenderer", "hideFallbackSkyCelestials"},
                {"net.caffeinemc.mods.sodium.client.render.chunk.DefaultChunkRenderer", "writeFallbackTerrainFade"}
            };
            for (String[] target : targets) {
                Class<?> type = Class.forName(target[0], false, Thread.currentThread().getContextClassLoader());
                if (!java.util.Arrays.stream(type.getDeclaredMethods()).anyMatch(m -> m.getName().contains(target[1])))
                    throw new AssertionError("Missing injection: " + target[0] + " " + target[1]);
                System.out.println("TRANSFORM_PASS " + target[0]);
            }
            System.exit(0);
        } catch (Throwable error) { error.printStackTrace(); System.exit(1); }
    }
}
''')
    subprocess.run(['javac', '--release', '25', '-proc:none', '-cp', os.pathsep.join(classpath), '-d', str(probe), str(source)], check=True)
    with zipfile.ZipFile(RUN / 'mods/transform-probe.jar', 'w') as archive:
        archive.write(probe / 'TransformProbe.class', 'TransformProbe.class')
        archive.writestr('fabric.mod.json', json.dumps({'schemaVersion': 1, 'id': 'loader_test', 'version': '1.0.0', 'environment': 'client', 'entrypoints': {'preLaunch': ['TransformProbe']}}))
else:
    (RUN / 'mods/transform-probe.jar').unlink(missing_ok=True)
natives = RUN / 'natives'
natives.mkdir(exist_ok=True)
command = [
    'java', '-XstartOnFirstThread', '-Xmx2G', '--enable-native-access=ALL-UNNAMED',
    '--add-exports=java.base/jdk.internal.misc=ALL-UNNAMED',
    '-Dmixin.debug.export=true', '-Dmixin.debug.verbose=true',
    f'-Djava.library.path={natives}/java',
    f'-Djna.tmpdir={natives}/jna',
    f'-Dorg.lwjgl.system.SharedLibraryExtractPath={natives}/lwjgl',
    '-cp', os.pathsep.join(classpath), metadata['mainClass'],
    '--username', 'LoaderTest', '--version', '26.3',
    '--gameDir', str(RUN), '--assetsDir', str(META / 'assets'),
    '--assetIndex', metadata['assetIndex']['id'], '--accessToken', '0',
    '--userType', 'msa', '--width', '640', '--height', '480',
]
with (RUN / 'smoke.log').open('w') as output:
    process = subprocess.Popen(command, cwd=RUN, stdout=output, stderr=subprocess.STDOUT)
    print(f'Isolated test client PID {process.pid}', flush=True)
    try:
        result = process.wait(timeout=45)
        print('Exit:', result)
        if result: raise SystemExit(result)
    except subprocess.TimeoutExpired:
        process.terminate()
        try:
            process.wait(timeout=5)
        except subprocess.TimeoutExpired:
            process.kill()
            process.wait()
        print('Stopped isolated test client after 45 seconds.')
print(RUN / 'smoke.log')
