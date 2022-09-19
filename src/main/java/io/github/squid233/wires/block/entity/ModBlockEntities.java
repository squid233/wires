package io.github.squid233.wires.block.entity;

import io.github.squid233.wires.Wires;
import io.github.squid233.wires.block.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/**
 * @author squid233
 * @since 0.1.0
 */
public final class ModBlockEntities {
    public static final BlockEntityType<InsulatorBlockEntity> INSULATOR = register("insulator",
        FabricBlockEntityTypeBuilder.create(InsulatorBlockEntity::new, ModBlocks.INSULATOR).build());

    private static <T extends BlockEntity, U extends BlockEntityType<T>> U register(String name, U type) {
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Wires.NAMESPACE, name), type);
    }

    public static void register() {
    }
}
