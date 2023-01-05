package io.github.squid233.wires.item;

import io.github.squid233.wires.Wires;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/**
 * @author squid233
 * @since 0.1.0
 */
public final class ModItemGroups {
    public static final ItemGroup.Builder CORE = FabricItemGroup.builder(new Identifier(Wires.NAMESPACE, "core"))
        .displayName(Text.translatable("itemGroup.wires.core"))
        .icon(() -> new ItemStack(ModItems.WIRE))
        .entries((enabledFeatures, entries, operatorEnabled) -> {
            entries.add(ModItems.WIRES_POLE);
            entries.add(ModItems.IRON_WIRES_POLE);
            entries.add(ModItems.IRON_HANGING_WIRES_POLE);
            entries.add(ModItems.WIRE);
            entries.add(ModItems.WIRE_REMOVER);
            entries.add(ModItems.INSULATOR);
        });
}
