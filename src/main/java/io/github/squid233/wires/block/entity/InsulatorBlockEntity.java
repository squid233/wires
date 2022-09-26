package io.github.squid233.wires.block.entity;

import io.github.squid233.wires.block.InsulatorBlock;
import io.github.squid233.wires.util.MutableVec3d;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;

/**
 * @author squid233
 * @since 0.1.0
 */
public final class InsulatorBlockEntity extends BlockEntity {
    private final Set<BlockPos> connectedTo = new HashSet<>();
    private final MutableVec3d renderOffset = new MutableVec3d(.5, .5, .5);

    public InsulatorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.INSULATOR, pos, state);
    }

    @SuppressWarnings("deprecation")
    private void _connect(BlockPos bpos) {
        connectedTo.add(bpos);
        var oldState = getCachedState();
        var state = oldState.with(InsulatorBlock.CONNECTED, true);
        setCachedState(state);
        if (world != null) {
            world.setBlockState(pos, state, Block.NOTIFY_LISTENERS | Block.FORCE_STATE);
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
            setCachedState(state);
            if (world != null) {
                world.setBlockState(pos, state, Block.NOTIFY_LISTENERS | Block.FORCE_STATE);
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
            world.updateListeners(getPos(), oldState, getCachedState(), Block.NOTIFY_ALL);
        }
    }

    public Set<BlockPos> getConnectedTo() {
        return connectedTo;
    }

    public MutableVec3d getRenderOffset() {
        return renderOffset;
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (nbt.contains("renderOffset", NbtElement.COMPOUND_TYPE)) {
            var c = nbt.getCompound("renderOffset");
            renderOffset.set(c.getDouble("x"), c.getDouble("y"), c.getDouble("z"));
        }
        connectedTo.clear();
        var list = nbt.getList("connectedTo", NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < list.size(); i++) {
            var c = list.getCompound(i);
            connectedTo.add(posFromNbt(c));
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
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
    }
}
