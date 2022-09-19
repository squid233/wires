package io.github.squid233.wires.item;

import io.github.squid233.wires.Wires;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

/**
 * @author squid233
 * @since 0.1.0
 */
public final class ModItemGroups {
    public static final ItemGroup CORE = FabricItemGroupBuilder.build(new Identifier(Wires.NAMESPACE, "core"),
        () -> new ItemStack(ModItems.WIRES_POLE));
}
