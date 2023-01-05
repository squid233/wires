package io.github.squid233.wires.block;

import io.github.squid233.wires.Wires;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

/**
 * @author squid233
 * @since 0.1.0
 */
public final class ModBlocks {
    public static final Block WIRES_POLE = register("wires_pole",
        new WiresPoleBlock(Settings.copy(Blocks.COBBLESTONE)));
    public static final Block IRON_WIRES_POLE = register("iron_wires_pole",
        new WiresPoleBlock(Settings.copy(Blocks.IRON_BLOCK)));
    public static final Block IRON_HANGING_WIRES_POLE = register("iron_hanging_wires_pole",
        new HangingWiresPoleBlock(Settings.copy(Blocks.IRON_BLOCK)));
    public static final Block INSULATOR = register("insulator",
        new InsulatorBlock(Settings.of(Material.METAL).strength(5f, 6f).sounds(BlockSoundGroup.METAL)));

    private static <T extends Block> T register(String name, T block) {
        return Registry.register(Registries.BLOCK, new Identifier(Wires.NAMESPACE, name), block);
    }

    public static void register() {
    }
}
