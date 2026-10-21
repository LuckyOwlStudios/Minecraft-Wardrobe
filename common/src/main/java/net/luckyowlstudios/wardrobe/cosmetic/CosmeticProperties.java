package net.luckyowlstudios.wardrobe.cosmetic;

import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * How a cosmetic looks on the player.
 *
 * @param paintsSkin        whether the cosmetic has a skin-layout texture painted onto the player model, at
 *                          {@code <namespace>:textures/cosmetic/<item>.png}. Its base-layer pixels sit directly on the
 *                          player's skin and its outer-layer pixels form the extruded second layer.
 * @param slimVariant       whether a {@code <item>_slim.png} texture exists for players with slim (Alex) arms.
 * @param hiddenSkinLayers  which of the player's own skin overlay layers are hidden while this is worn.
 * @param extraModels       ids of extra models (sleeves, hoods, capes, fully custom models) registered client side in
 *                          {@code CosmeticModels}. They only render because the cosmetic asks for them here.
 * @param helmetLift        how many pixels the extra models' head moves up while a helmet (or anything else) is worn
 *                          in the head slot, so it sits on top of the helmet. 0 keeps it in place, e.g. for hoods.
 */
public record CosmeticProperties(boolean paintsSkin, boolean slimVariant, Set<SkinLayer> hiddenSkinLayers,
                                 List<ResourceLocation> extraModels, float helmetLift) {

    public static Builder builder(CosmeticSlot slot) {
        return new Builder(slot);
    }

    public static final class Builder {
        private boolean paintsSkin = true;
        private boolean slimVariant = false;
        private final EnumSet<SkinLayer> hiddenSkinLayers;
        private final List<ResourceLocation> extraModels = new ArrayList<>();
        private float helmetLift = 0.0F;

        private Builder(CosmeticSlot slot) {
            this.hiddenSkinLayers = slot.defaultHiddenLayers();
        }

        /** For cosmetics made only of extra models, with nothing painted onto the skin. */
        public Builder noSkinTexture() {
            this.paintsSkin = false;
            return this;
        }

        public Builder slimVariant() {
            this.slimVariant = true;
            return this;
        }

        public Builder hideSkinLayers(SkinLayer... layers) {
            Collections.addAll(hiddenSkinLayers, layers);
            return this;
        }

        public Builder showSkinLayers(SkinLayer... layers) {
            for (SkinLayer layer : layers) {
                hiddenSkinLayers.remove(layer);
            }
            return this;
        }

        /** Hats: hide the outer head layer, which many skins use for hair. */
        public Builder hideHeadOverlay() {
            return hideSkinLayers(SkinLayer.HAT);
        }

        public Builder extraModel(ResourceLocation modelId) {
            extraModels.add(modelId);
            return this;
        }

        /** Moves the extra models' head up by this many pixels while a helmet is worn. Hoods should leave this out. */
        public Builder liftOverHelmet(float pixels) {
            this.helmetLift = pixels;
            return this;
        }

        public CosmeticProperties build() {
            return new CosmeticProperties(paintsSkin, slimVariant, Set.copyOf(hiddenSkinLayers), List.copyOf(extraModels),
                    helmetLift);
        }
    }
}
