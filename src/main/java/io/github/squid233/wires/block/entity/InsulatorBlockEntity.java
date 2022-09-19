package io.github.squid233.wires.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

/**
 * @author squid233
 * @since 0.1.0
 */
public final class InsulatorBlockEntity extends BlockEntity {
    private BlockPos connectedTo;

    public InsulatorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.INSULATOR, pos, state);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbtWithId();
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (nbt.contains("connectedTo")) {
            connectedTo = posFromNbt(nbt.getCompound("connectedTo"));
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if (connectedTo != null) {
            var c = new NbtCompound();
            c.putInt("x", connectedTo.getX());
            c.putInt("y", connectedTo.getY());
            c.putInt("z", connectedTo.getZ());
            nbt.put("connectedTo", c);
        }
    }
}
