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

## Cosmetics
Every player gets four cosmetic slots: **hat, shirt, pants and shoes**. They give no protection, only looks.
- Press **K** (rebindable) to open the Wardrobe, or right-click a cosmetic to put it on.
- Slots are saved with the player, kept through death, and synced to the player and everyone who can see them.

### How cosmetics render
Cosmetics are painted onto the player's own model rather than an armor-style shell, so a shirt looks like part of the skin.
- The cosmetic texture is a normal 64×64 **skin-layout** PNG at `assets/<namespace>/textures/cosmetic/<item>.png`.
  Pixels on the base layer sit on the skin; pixels on the outer layer form the extruded second layer.
- While a cosmetic is worn, the matching parts of the player's own outer layer are hidden so they don't poke through.
  Defaults: shirts hide the jacket and sleeves, pants hide the pants layer, hats and shoes hide nothing.
- Cosmetics can also add extra 3D models (sleeves, hoods, capes, fully custom models). These only render when the cosmetic asks for them.

### Adding a cosmetic
Register it in `ModItems` with a `CosmeticProperties` builder:

```java
CosmeticProperties.builder(CosmeticSlot.HAT)
        .hideHeadOverlay()                 // hats: hide the skin's outer head layer (often hair)
        .showSkinLayers(SkinLayer.JACKET)  // undo a default
        .slimVariant()                     // also ship <item>_slim.png for slim-armed skins
        .extraModel(Constants.id("hood"))  // render a registered extra model
        .noSkinTexture()                   // model-only cosmetic, nothing painted on the skin
```

Extra models are registered client side with `CosmeticModels.register(id, layerDefinitionSupplier, texture)`.
Export them from Blockbench as a Mojang-mapped Java entity model, with the parts `head`, `hat`, `body`, `right_arm`, `left_arm`, `right_leg` and `left_leg` (empty is fine). `CosmeticModels.humanoidMesh()` gives you all seven with the player's pivots.
