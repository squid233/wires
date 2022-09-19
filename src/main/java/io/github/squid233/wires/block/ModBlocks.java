package io.github.squid233.wires.block;

import io.github.squid233.wires.Wires;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/**
 * @author squid233
 * @since 0.1.0
 */
public final class ModBlocks {
    public static final Block WIRES_POLE = register("wires_pole",
        new WiresPoleBlock(AbstractBlock.Settings.copy(Blocks.COBBLESTONE_WALL)));
    public static final Block INSULATOR = register("insulator",
        new Block(AbstractBlock.Settings.of(Material.METAL).strength(5f, 6f).sounds(BlockSoundGroup.METAL)));

    private static <T extends Block> T register(String name, T block) {
        return Registry.register(Registry.BLOCK, new Identifier(Wires.NAMESPACE, name), block);
    }

    public static void register() {
    }
}
