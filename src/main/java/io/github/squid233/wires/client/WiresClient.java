package io.github.squid233.wires.client;

import io.github.squid233.wires.block.ModBlocks;
import io.github.squid233.wires.client.render.block.entity.InsulatorBlockEntityRenderer;
import io.github.squid233.wires.block.entity.ModBlockEntities;
import io.github.squid233.wires.util.ModOptions;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;

/**
 * @author squid233
 * @since 0.1.0
 */
public final class WiresClient implements ClientModInitializer {
    public static ModOptions options;

    @Override
    public void onInitializeClient() {
        options = new ModOptions();
        if (options.fileExists()) {
            options.load();
        }
        options.save();
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WIRES_POLE, RenderLayer.getCutoutMipped());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.IRON_WIRES_POLE, RenderLayer.getCutoutMipped());
        BlockEntityRendererRegistry.INSTANCE.register(ModBlockEntities.INSULATOR, InsulatorBlockEntityRenderer::new);
    }
}
