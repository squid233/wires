package io.github.squid233.wires.item;

import io.github.squid233.wires.Wires;
import io.github.squid233.wires.block.ModBlocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/**
 * @author squid233
 * @since 0.1.0
 */
public final class ModItems {
    public static final BlockItem WIRES_POLE = register("wires_pole",
        new BlockItem(ModBlocks.WIRES_POLE, new Item.Settings().group(ModItemGroups.CORE)));
    public static final Item WIRE = register("wire",
        new SelectorItem("connecting", new Item.Settings().group(ModItemGroups.CORE)));
    public static final Item WIRE_REMOVER = register("wire_remover",
        new SelectorItem("removing", new Item.Settings().group(ModItemGroups.CORE)));
    public static final BlockItem INSULATOR = register("insulator",
        new BlockItem(ModBlocks.INSULATOR, new Item.Settings().group(ModItemGroups.CORE)));

    private static <T extends Item> T register(String name, T item) {
        return Registry.register(Registry.ITEM, new Identifier(Wires.NAMESPACE, name), item);
    }

    public static void register() {
    }
}
