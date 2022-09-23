package io.github.squid233.wires.item;

import io.github.squid233.wires.block.entity.InsulatorBlockEntity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

/**
 * @author squid233
 * @since 0.1.0
 */
public final class WireRemoverItem extends SelectorItem {
    public WireRemoverItem(String subKey, Settings settings) {
        super(subKey, settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.shouldCancelInteraction()) return ActionResult.PASS;
        var world = context.getWorld();
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }
        var stack = context.getStack();
        var sub = stack.getSubNbt(subKey);
        var pos = context.getBlockPos();
        if (sub == null) {
            var tag = new NbtCompound();
            tag.putInt("x", pos.getX());
            tag.putInt("y", pos.getY());
            tag.putInt("z", pos.getZ());
            stack.setSubNbt(subKey, tag);
        } else {
            int x = sub.getInt("x");
            int y = sub.getInt("y");
            int z = sub.getInt("z");
            if (x == pos.getX() &&
                y == pos.getY() &&
                z == pos.getZ()) {
                stack.removeSubNbt(subKey);
            } else {
                if (world.getBlockEntity(pos) instanceof InsulatorBlockEntity insulator) {
                    var bpos = new BlockPos(x, y, z);
                    insulator.disconnect(-1, bpos, false);
                    if (world.getBlockEntity(bpos) instanceof InsulatorBlockEntity other) {
                        other.disconnect(-1, pos, false);
                    }
                }
                stack.removeSubNbt(subKey);
            }
        }
        return ActionResult.CONSUME;
    }
}
