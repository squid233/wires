package io.github.squid233.wires;

import io.github.squid233.wires.block.ModBlocks;
import io.github.squid233.wires.block.entity.ModBlockEntities;
import io.github.squid233.wires.item.ModItems;
import net.fabricmc.api.ModInitializer;

/**
 * @author squid233
 * @since 0.1.0
 */
public final class Wires implements ModInitializer {
    public static final String NAMESPACE = "wires";

    @Override
    public void onInitialize() {
        ModBlocks.register();
        ModBlockEntities.register();
        ModItems.register();
    }
}
