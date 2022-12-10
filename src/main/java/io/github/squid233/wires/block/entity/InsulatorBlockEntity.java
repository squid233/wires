package io.github.squid233.wires.block.entity;

import io.github.squid233.wires.block.InsulatorBlock;
import io.github.squid233.wires.util.MutableVec3d;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;

/**
 * @author squid233
 * @since 0.1.0
 */
public final class InsulatorBlockEntity extends BlockEntity implements BlockEntityClientSerializable {
    private final Set<BlockPos> connectedTo = new HashSet<>();
    private final MutableVec3d renderOffset = new MutableVec3d(.5, .5, .5);

    public InsulatorBlockEntity() {
        super(ModBlockEntities.INSULATOR);
    }

    @SuppressWarnings("deprecation")
    private void _connect(BlockPos bpos) {
        connectedTo.add(bpos);
        var oldState = getCachedState();
        var state = oldState.with(InsulatorBlock.CONNECTED, true);
        //setCachedState(state);
        markDirty();
        if (world != null) {
            world.setBlockState(pos, state, 2 | 16);
        }
        updateListeners(oldState);
    }

    public void connect(int targetX, int targetY, int targetZ) {
        var bpos = new BlockPos(targetX, targetY, targetZ);
        if (world != null && world.getBlockEntity(bpos) instanceof InsulatorBlockEntity insulator) {
            _connect(bpos);
            insulator._connect(pos);
        }
    }

    @SuppressWarnings("deprecation")
    public void disconnect(BlockPos bpos) {
        connectedTo.remove(bpos);
        var oldState = getCachedState();
        if (connectedTo.isEmpty()) {
            var state = oldState.with(InsulatorBlock.CONNECTED, false);
            //setCachedState(state);
            markDirty();
            if (world != null) {
                world.setBlockState(pos, state, 2 | 16);
            }
        }
        updateListeners(oldState);
    }

    private void disconnectTarget(World world, BlockPos bpos) {
        if (world.getBlockEntity(bpos) instanceof InsulatorBlockEntity insulator) {
            insulator.disconnect(pos);
        }
    }

    public void disconnectAll() {
        if (world != null) {
            for (var bpos : connectedTo) {
                disconnectTarget(world, bpos);
            }
            connectedTo.clear();
            updateListeners(getCachedState());
        }
    }

    public void setRenderOffset(double x, double y, double z) {
        renderOffset.set(x, y, z);
        updateListeners(getCachedState());
    }

    public void setRenderOffset(Position offset) {
        setRenderOffset(offset.getX(), offset.getY(), offset.getZ());
    }

    private void updateListeners(BlockState oldState) {
        markDirty();
        if (world != null) {
            world.updateListeners(getPos(), oldState, getCachedState(), 3);
        }
    }

    public Set<BlockPos> getConnectedTo() {
        return connectedTo;
    }

    public MutableVec3d getRenderOffset() {
        return renderOffset;
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return writeNbt(new NbtCompound());
    }

    @Override
    public void fromTag(BlockState state, NbtCompound tag) {
        super.fromTag(state, tag);
        if (tag.contains("renderOffset", 0xa)) {
            var c = tag.getCompound("renderOffset");
            renderOffset.set(c.getDouble("x"), c.getDouble("y"), c.getDouble("z"));
        }
        connectedTo.clear();
        var list = tag.getList("connectedTo", 0xa);
        for (int i = 0; i < list.size(); i++) {
            var c = list.getCompound(i);
            connectedTo.add(new BlockPos(c.getInt("x"), c.getInt("y"), c.getInt("z")));
        }
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        var ro = new NbtCompound();
        ro.putDouble("x", renderOffset.getX());
        ro.putDouble("y", renderOffset.getY());
        ro.putDouble("z", renderOffset.getZ());
        nbt.put("renderOffset", ro);
        var list = new NbtList();
        for (var bpos : connectedTo) {
            var c = new NbtCompound();
            c.putInt("x", bpos.getX());
            c.putInt("y", bpos.getY());
            c.putInt("z", bpos.getZ());
            list.add(c);
        }
        nbt.put("connectedTo", list);
        return nbt;
    }

    @Override
    public void fromClientTag(NbtCompound tag) {
        fromTag(getCachedState(), tag);
    }

    @Override
    public NbtCompound toClientTag(NbtCompound tag) {
        return writeNbt(tag);
    }
}
