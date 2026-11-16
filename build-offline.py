#!/usr/bin/env python3
"""Offline build of both mods with javac (no Gradle downloads needed).

Uses the Minecraft 26.3 client and libraries that the Modrinth App has already
installed, plus the mods in the realm profile, exactly like the earlier
grand-teleport-26.3/build-port.py.

    python3 build-offline.py                 # build both into build/offline/
    python3 build-offline.py --verify A.jar B.jar
        # also compare each built JAR's class/member signatures against the
        # installed GrandTeleport and DH Bridge JARs (in that order)

Environment overrides: MODRINTH_META, MODS_DIR, JAVA_HOME.
"""
import hashlib, json, os, re, subprocess, sys, tempfile, zipfile
from pathlib import Path

ROOT = Path(__file__).resolve().parent
HOME = Path.home()
META = Path(os.environ.get('MODRINTH_META', HOME / 'Library/Application Support/ModrinthApp/meta'))
MODS = Path(os.environ.get('MODS_DIR', HOME / 'Library/Application Support/ModrinthApp/profiles/emotivist_s server/mods'))
JAVA = Path(os.environ.get('JAVA_HOME', '/Library/Java/JavaVirtualMachines/temurin-25.jdk/Contents/Home')) / 'bin'
BASE = META / 'versions/26.3-0.20.1/26.3-0.20.1'
OUT = ROOT / 'build/offline'

def props():
    p = {}
    for line in (ROOT / 'gradle.properties').read_text().splitlines():
        if '=' in line and not line.lstrip().startswith('#'):
            k, v = line.split('=', 1); p[k.strip()] = v.strip()
    return p

def classpath(tmp, patterns):
    paths = [Path(str(BASE) + '.jar')]
    for lib in json.loads(Path(str(BASE) + '.json').read_text())['libraries']:
        g, n, v, *_ = lib['name'].split(':')
        path = META / 'libraries' / lib.get('downloads', {}).get('artifact', {}).get(
            'path', f'{g.replace(".", "/")}/{n}/{v}/{n}-{v}.jar')
        if path.is_file():
            paths.append(path)
    for pattern in patterns:
        matches = sorted(MODS.glob(pattern))
        if not matches:
            sys.exit(f'missing {pattern} in {MODS}')
        paths += matches
        for mod in matches:  # nested jars (fabric-api modules etc.)
            with zipfile.ZipFile(mod) as z:
                for name in z.namelist():
                    if name.startswith('META-INF/jars/') and name.endswith('.jar'):
                        dest = tmp / Path(name).name
                        dest.write_bytes(z.read(name)); paths.append(dest)
    return os.pathsep.join(map(str, paths))

FIXED_TIME = (1980, 2, 1, 0, 0, 0)

def build(project, version, jar_name, patterns):
    src = ROOT / project / 'src/main'
    out = OUT / jar_name
    out.parent.mkdir(parents=True, exist_ok=True)
    with tempfile.TemporaryDirectory() as t:
        tmp = Path(t); classes = tmp / 'classes'; classes.mkdir()
        subprocess.run([str(JAVA / 'javac'), '--release', '25', '-proc:none', '-encoding', 'UTF-8',
                        '-nowarn', '-cp', classpath(tmp, patterns), '-d', str(classes),
                        *map(str, sorted((src / 'java').rglob('*.java')))], check=True)
        entries = []
        for folder in (classes, src / 'resources'):
            for path in folder.rglob('*'):
                if path.is_file():
                    entries.append((path.relative_to(folder).as_posix(), path))
        with zipfile.ZipFile(out, 'w', zipfile.ZIP_DEFLATED) as z:
            z.writestr(zipfile.ZipInfo('META-INF/MANIFEST.MF', FIXED_TIME), 'Manifest-Version: 1.0\r\n\r\n')
            for rel, path in sorted(entries):
                data = path.read_bytes()
                if rel == 'fabric.mod.json':
                    data = data.decode().replace('${version}', version).encode()
                info = zipfile.ZipInfo(rel, FIXED_TIME); info.compress_type = zipfile.ZIP_DEFLATED
                z.writestr(info, data)
    digest = hashlib.sha256(out.read_bytes()).hexdigest()
    out.with_suffix('.jar.sha256').write_text(f'{digest}  {out.name}\n')
    print(f'built {out}\n  sha256 {digest}')
    return out

def signatures(jar):
    """Map class -> normalised `javap -p` member list (ignores compiler-specific detail)."""
    with tempfile.TemporaryDirectory() as t:
        with zipfile.ZipFile(jar) as z:
            names = sorted(n for n in z.namelist() if n.endswith('.class'))
            z.extractall(t, names)
        if not names:
            return {}
        res = subprocess.run([str(JAVA / 'javap'), '-p', '-cp', t,
                              *[n[:-6].replace('/', '.') for n in names]],
                             check=True, capture_output=True, text=True).stdout
    sigs, current = {}, None
    for line in res.splitlines():
        if line.startswith('Compiled from'):
            continue
        if line and not line.startswith(' ') and line.rstrip().endswith('{'):
            current = re.sub(r'\s+', ' ', line.strip()); sigs[current] = set(); continue
        if current and line.startswith('  '):
            member = line.strip()
            if 'lambda$' in member or 'access$' in member or '$assertionsDisabled' in member:
                continue  # synthetic names differ between compilers
            sigs[current].add(member)
    return sigs

def resources(jar):
    with zipfile.ZipFile(jar) as z:
        out = {}
        for n in z.namelist():
            if n.endswith('/') or n.endswith('.class') or n.startswith('META-INF/'):
                continue
            out[n] = hashlib.sha256(z.read(n)).hexdigest()
        return out

def verify(built, reference):
    a, b = signatures(built), signatures(reference)
    ok = True
    for cls in sorted(set(a) | set(b)):
        if cls not in a: print(f'  MISSING class  {cls}'); ok = False; continue
        if cls not in b: print(f'  EXTRA class    {cls}'); ok = False; continue
        for m in sorted(b[cls] - a[cls]): print(f'  missing member {cls}: {m}'); ok = False
        for m in sorted(a[cls] - b[cls]): print(f'  extra member   {cls}: {m}'); ok = False
    ra, rb = resources(built), resources(reference)
    for n in sorted(set(ra) | set(rb)):
        if ra.get(n) != rb.get(n): print(f'  resource differs: {n}'); ok = False
    print(f'{"MATCH" if ok else "DIFFERS"}: {built.name} vs {reference.name} '
          f'({len(b)} classes compared)')
    return ok

if __name__ == '__main__':
    p = props()
    gt = build('grand-teleport', p['grand_teleport_version'],
               f"GrandTeleport-{p['grand_teleport_version']}.jar",
               ['fabric-api-*.jar', 'modmenu-*.jar', 'sodium-fabric-*.jar'])
    dh = build('dh-bridge', p['dh_bridge_version'],
               f"GrandTeleport-DHBridge-v{p['dh_bridge_version']}.jar",
               ['fabric-api-*.jar'])
    if len(sys.argv) == 4 and sys.argv[1] == '--verify':
        results = [verify(gt, Path(sys.argv[2])), verify(dh, Path(sys.argv[3]))]
        sys.exit(0 if all(results) else 1)
