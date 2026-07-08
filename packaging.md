# Building and packaging WeaveDreamer

WeaveDreamer ships as self-contained release bundles: the application jar, its
dependencies, and a platform-specific Java runtime built with `jlink`, so end
users don't need a system JDK. There is also a smaller "portable" bundle with no
runtime for users who already have Java 21+.

## Prerequisites

- A JDK 25+ on your `PATH` (for `mvn`, `jlink`). Check with `java -version`.
- Maven 3.9+.
- Python 3 (`python3`) — the build runs `fetch_doc.py` during `prepare-package`.
- The bs4 python package installed, and the right venv activated if appropriate
- Per-platform JDKs downloaded into `jdks/` (see below). Only needed when
  building the platform bundles, not for a plain `mvn package` of the jar.

## One-time setup: download the target JDKs

The runtime images are produced by `jlink`-ing against the `jmods` of a JDK
**for each target platform**. Download JDK 25.0.2 for every platform you want to
ship from <https://jdk.java.net> and expand each into `jdks/<arch>/`:

```
jdks/
├── linux_arm/jdk-25.0.2/
├── linux_x64/jdk-25.0.2/
├── mac_arm/jdk-25.0.2.jdk/       # macOS ships a .jdk bundle (jmods under Contents/Home)
├── mac_x64/jdk-25.0.2/
└── windows/jdk-25.0.2/
```

`makeruntime.sh` locates each platform's `jmods` directory with `find`, so the
exact layout under `jdks/<arch>/` doesn't have to match — a `.jdk` bundle or a
plain `jdk-25.0.2/` both work. `jdks/` is git-ignored.

## The build has two stages

`jlink` can only build one runtime at a time and always for a single platform,
so the cross-platform runtime build lives in a shell script rather than the pom.
The full release is therefore two steps, **in order**:

### 1. Build the runtimes — `./makeruntime.sh`

```bash
mvn package -DskipTests      # produces target/weavedreamer-<version>.jar first
./makeruntime.sh             # then jlink one runtime per platform into runtime/<arch>/
```

This first `mvn package` also runs the assembly, but since `runtime/` doesn't
exist yet the platform zips it produces have no runtime — they're harmless and
get overwritten by step 2. Only the jar matters here.

For each platform the script runs `jlink` with a module path of the application
jar plus its dependency jars (resolved from your local `~/.m2` repository), and:

- `--add-modules com.jenkins.weavedreamer` — the app module (`module-info.java`).
- `--launcher weavedreamer=…/WeaveDreamerApp` — creates `bin/weavedreamer`
  (and `bin/weavedreamer.bat` on Windows) so the runtime is directly launchable.
- `--compress zip-6 --no-header-files --no-man-pages --strip-debug` — shrinks the
  image (~35 MB per platform).

Output lands in `runtime/<arch>/` (git-ignored). Each is a complete, launchable
Java runtime containing only the modules the app needs.

> The dependency jar paths and the JDK version are hard-coded near the top of
> `makeruntime.sh`. When you bump a dependency in `pom.xml` or the JDK version,
> update the script to match.

### 2. Package the bundles — `mvn package`

The `maven-assembly-plugin` zips one bundle per platform, each pulling in the
runtime built in step 1. Running `mvn package` runs every execution:

| Execution / zip suffix | Descriptor | Contents |
|---|---|---|
| `pack-portable` → `weavedreamer-<version>-bin.zip` | `src/assembly/bin.xml` | jar + `lib/` deps, **no runtime** (needs system Java 21+) |
| `pack-linux-x64` → `…-linux-x64-bin.zip` | `bin-linux_x64.xml` | + `runtime/` for linux x64 |
| `pack-linux-arm` → `…-linux-arm-bin.zip` | `bin-linux_arm.xml` | + `runtime/` for linux arm |
| `pack-mac-x64` → `…-mac-x64-bin.zip` | `bin-mac_x64.xml` | + `runtime/` for macOS x64 |
| `pack-mac-arm` → `…-mac-arm-bin.zip` | `bin-mac_arm.xml` | + `runtime/` for macOS arm |
| `pack-windows` → `…-windows-bin.zip` | `bin-windows.xml` | + `runtime/` for Windows |

Zips are written to `target/`.

## How the assembly descriptors fit together

`src/assembly/component-common.xml` holds everything shared between bundles — the
`WeaveDreamer.jar`, the docs (`COPYING`, `help.html`, `attachment/`), and the
dependency jars into `lib/`. Each platform descriptor (`bin-<arch>.xml`) includes
that component and adds only its own `runtime/<arch>` fileSet. `bin.xml` includes
the component with no runtime at all.

Two things to know if you edit the descriptors:

- The runtime fileSets set `fileMode` / `directoryMode` to `0755`. The assembly
  plugin drops the executable bit unless a mode is given, which would break the
  `bin/weavedreamer` and `bin/java` launchers.
- Every execution produces an artifact with the same `bin` classifier, so the
  plugin is configured with `<attach>false</attach>` to stop the six bundles
  clobbering each other in the reactor / local repo.

Per-platform bundling has to be a descriptor each because the assembly plugin has
no way to inject a per-execution property into a single shared descriptor.

## Running a bundle

Unzip and launch:

```bash
unzip weavedreamer-<version>-linux-x64-bin.zip
cd weavedreamer-<version>-linux-x64
runtime/bin/weavedreamer          # Windows: runtime\bin\weavedreamer.bat
```

The portable bundle instead runs against a system Java:

```bash
java -jar WeaveDreamer.jar
```

## Quick reference

```bash
mvn package -DskipTests   # build the jar (its platform zips are throwaway on this run)
./makeruntime.sh          # build runtime/<arch>/ for all platforms
mvn package               # zip all bundles into target/
```

`-DskipTests` skips the surefire unit tests. `mvn package` stops before the
`verify` phase, so it never runs the GUI (`uat`) tests — use `mvn verify` for
those (it unzips the portable bundle and drives it via antrun).
