package io.github.squid233.wires;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import io.github.squid233.wires.block.ModBlocks;
import io.github.squid233.wires.block.entity.InsulatorBlockEntity;
import io.github.squid233.wires.block.entity.ModBlockEntities;
import io.github.squid233.wires.client.WiresClient;
import io.github.squid233.wires.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.PosArgument;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

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

        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
                if (!dedicated) {
                    dispatcher.register(
                        literal(NAMESPACE).then(
                            literal("renderoffset").then(
                                argument("pos", BlockPosArgumentType.blockPos()).then(
                                    argument("offset", Vec3ArgumentType.vec3(false)).executes(context -> {
                                        ServerPlayerEntity player = context.getSource().getPlayer();
                                        World world = context.getSource().getWorld();
                                        BlockPos pos = context.getArgument("pos", PosArgument.class).toAbsoluteBlockPos(context.getSource());
                                        if (player.isCreative() &&
                                            world.getBlockEntity(pos) instanceof InsulatorBlockEntity) {
                                            InsulatorBlockEntity insulator = (InsulatorBlockEntity) world.getBlockEntity(pos);
                                            if (insulator != null) {
                                                insulator.setRenderOffset(context.getArgument("offset", PosArgument.class)
                                                    .toAbsolutePos(context.getSource().withPosition(insulator.getRenderOffset().toImmutable())));
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        }
                                        throw new DynamicCommandExceptionType(o ->
                                            new TranslatableText("command." + NAMESPACE + ".error.render_offset", o))
                                            .create(pos);
                                    })
                                )
                            )
                        )
                    );
                    dispatcher.register(
                        literal(NAMESPACE).then(
                            literal("reload").executes(context -> {
                                WiresClient.options.load();
                                return Command.SINGLE_SUCCESS;
                            })
                        )
                    );
                }
            }
        );
    }
}
