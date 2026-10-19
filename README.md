# Lucky's Wardrobe

A 1.21.1 remake of Lucky's Wardrobe, built from one shared codebase for Fabric and NeoForge.
Based on [jaredlll08/MultiLoader-Template](https://github.com/jaredlll08/MultiLoader-Template) (1.21.1 branch).

## Layout
- `common/` – shared code (vanilla Minecraft classes only, Mojang mappings + Parchment). Most of the mod lives here.
- `fabric/` – Fabric entrypoint and Fabric-only code (Loom).
- `neoforge/` – NeoForge entrypoint and NeoForge-only code (ModDevGradle).
- `platform/` – the `Services` / `IPlatformHelper` pattern for calling loader-specific code from `common`.

Mod id `luckys_wardrobe`, package `net.luckyowlstudios.wardrobe`. Versions live in `gradle.properties`.

## Building and running
Requires Java 21.
- `./gradlew build` – jars land in `fabric/build/libs` and `neoforge/build/libs`.
- `./gradlew :fabric:runClient` / `./gradlew :neoforge:runClient` – launch a dev client.
- In IntelliJ IDEA: open the folder as a Gradle project; run configurations are generated on import.
