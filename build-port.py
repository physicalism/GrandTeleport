#!/usr/bin/env python3
"""Offline macOS/JDK 25 build against installed, unobfuscated Minecraft 26.3."""
import json, os, subprocess, tempfile, zipfile, hashlib
from pathlib import Path
ROOT=Path(__file__).resolve().parent
META=Path(os.environ.get('MODRINTH_META', Path.home()/'Library/Application Support/ModrinthApp/meta'))
MODS=Path(os.environ.get('MODS_DIR', META.parent/'profiles/Fabric Realm/mods'))
BASE=META/'versions/26.3-0.20.1/26.3-0.20.1'
JAVA=Path(os.environ.get('JAVA_HOME','/Library/Java/JavaVirtualMachines/temurin-25.jdk/Contents/Home'))/'bin'
def libraries():
    paths=[Path(str(BASE)+'.jar')]
    for lib in json.loads(Path(str(BASE)+'.json').read_text())['libraries']:
        g,n,v,*_=lib['name'].split(':')
        path=META/'libraries'/lib.get('downloads',{}).get('artifact',{}).get('path',f'{g.replace(".","/")}/{n}/{v}/{n}-{v}.jar')
        if path.is_file(): paths.append(path)
    return paths
if __name__=='__main__':
    out=ROOT/'build/libs/grand-teleport-client-26.3-1.0.0.jar'; out.parent.mkdir(parents=True,exist_ok=True)
    with tempfile.TemporaryDirectory() as temp:
        tmp=Path(temp); paths=libraries()
        for pattern in ['fabric-api-*.jar','modmenu-*.jar','sodium-fabric-*.jar']:
            matches=sorted(MODS.glob(pattern)); paths+=matches
            for mod in matches:
                with zipfile.ZipFile(mod) as z:
                    for name in z.namelist():
                        if name.startswith('META-INF/jars/') and name.endswith('.jar'):
                            dest=tmp/Path(name).name; dest.write_bytes(z.read(name)); paths.append(dest)
        cp=os.pathsep.join(map(str,paths)); classes=tmp/'classes'; classes.mkdir()
        subprocess.run([str(JAVA/'javac'),'--release','25','-proc:none','-cp',cp,'-d',str(classes),*map(str,(ROOT/'src/mc26_3/java').rglob('*.java'))],check=True)
        values={'version':'1.0.0+26.3','minecraft_version':'26.3','loader_version':'0.20.1','java_version':'25','mixin_compatibility_level':'JAVA_25'}
        with zipfile.ZipFile(out,'w',zipfile.ZIP_DEFLATED) as z:
            for folder in [classes, ROOT/'src/mc26_3/resources',ROOT/'src/main/resources']:
                for path in sorted(folder.rglob('*')):
                    if not path.is_file(): continue
                    rel=path.relative_to(folder).as_posix()
                    if folder == ROOT/'src/main/resources' and not rel.startswith('assets/'): continue
                    if path.suffix=='.ogg' or '/sounds/' in rel or rel.endswith('sounds.json'): continue
                    data=path.read_bytes()
                    if path.suffix=='.json':
                        s=data.decode()
                        for k,v in values.items(): s=s.replace('${'+k+'}',v)
                        data=s.encode()
                    z.writestr(rel,data)
            z.write(ROOT/'LICENSE','LICENSE')
        out.with_suffix('.jar.sha256').write_text(hashlib.sha256(out.read_bytes()).hexdigest()+'  '+out.name+'\n')
        print(out)
