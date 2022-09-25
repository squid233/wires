package io.github.squid233.wires;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import io.github.squid233.wires.block.ModBlocks;
import io.github.squid233.wires.block.entity.ModBlockEntities;
import io.github.squid233.wires.client.WiresClient;
import io.github.squid233.wires.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;

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
                            literal("sagging").then(
                                argument("sagging", BoolArgumentType.bool()).executes(context -> {
                                    boolean sagging = context.getArgument("sagging", boolean.class);
                                    WiresClient.options.setSagging(sagging);
                                    WiresClient.options.save();
                                    return Command.SINGLE_SUCCESS;
                                })
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
