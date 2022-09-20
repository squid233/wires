package io.github.squid233.wires.block.entity;

import io.github.squid233.wires.block.InsulatorBlock;
import io.github.squid233.wires.block.ModBlocks;
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

    private void _connect(int targetX, int targetY, int targetZ) {
        connectedTo = new BlockPos(targetX, targetY, targetZ);
        var state = getCachedState().with(InsulatorBlock.CONNECTED, true);
        setCachedState(state);
        if (world != null) {
            world.setBlockState(pos, state);
        }
        markDirty();
    }

    public void connect(int targetX, int targetY, int targetZ) {
        _connect(targetX, targetY, targetZ);
        if (world != null && world.getBlockEntity(connectedTo) instanceof InsulatorBlockEntity insulator) {
            insulator._connect(pos.getX(), pos.getY(), pos.getZ());
        }
    }

    private void _disconnect() {
        connectedTo = null;
        var state = getCachedState().with(InsulatorBlock.CONNECTED, false);
        setCachedState(state);
        if (world != null && world.getBlockState(pos).isOf(ModBlocks.INSULATOR)) {
            world.setBlockState(pos, state);
        }
        markDirty();
    }

    public void disconnect() {
        if (world != null && world.getBlockEntity(connectedTo) instanceof InsulatorBlockEntity insulator) {
            insulator._disconnect();
        }
        _disconnect();
    }

    public BlockPos getConnectedTo() {
        return connectedTo;
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbtWithIdentifyingData();
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
