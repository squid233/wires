package io.github.squid233.wires.item;

import io.github.squid233.wires.block.InsulatorBlock;
import io.github.squid233.wires.block.entity.InsulatorBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author squid233
 * @since 0.1.0
 */
public final class WireItem extends SelectorItem {
    public WireItem(String subKey, Settings settings) {
        super(subKey, settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockPos pos = context.getBlockPos();
        World world = context.getWorld();
        if (context.shouldCancelInteraction() ||
            !(world.getBlockState(pos).getBlock() instanceof InsulatorBlock)) {
            return ActionResult.PASS;
        }
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }
        ItemStack stack = context.getStack();
        NbtCompound sub = stack.getSubTag(subKey);
        if (sub == null) {
            NbtCompound tag = new NbtCompound();
            tag.putInt("x", pos.getX());
            tag.putInt("y", pos.getY());
            tag.putInt("z", pos.getZ());
            stack.putSubTag(subKey, tag);
        } else {
            int x = sub.getInt("x");
            int y = sub.getInt("y");
            int z = sub.getInt("z");
            if (x != pos.getX() ||
                y != pos.getY() ||
                z != pos.getZ()) {
                if (world.getBlockEntity(pos) instanceof InsulatorBlockEntity) {
                    InsulatorBlockEntity insulator = (InsulatorBlockEntity) world.getBlockEntity(pos);
                    if (insulator != null) {
                        insulator.connect(x, y, z);
                    }
                }
            }
            stack.removeSubTag(subKey);
        }
        return ActionResult.CONSUME;
    }
}
