package io.github.squid233.wires.client;

import io.github.squid233.wires.block.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

/**
 * @author squid233
 * @since 0.1.0
 */
public final class WiresClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WIRES_POLE, RenderLayer.getCutoutMipped());
    }
}
