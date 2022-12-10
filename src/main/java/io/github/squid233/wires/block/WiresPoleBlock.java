package io.github.squid233.wires.block;

import io.github.squid233.wires.Wires;
import net.minecraft.block.*;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author squid233
 * @since 0.1.0
 */
public final class WiresPoleBlock extends HorizontalConnectingBlock {
    public static final BooleanProperty POST = BooleanProperty.of("post");

    public WiresPoleBlock(Settings settings) {
        super(4f, 4f, 16f, 16f, 16f, settings);
        setDefaultState(stateManager.getDefaultState()
            .with(NORTH, false)
            .with(EAST, false)
            .with(SOUTH, false)
            .with(WEST, false)
            .with(WATERLOGGED, false)
            .with(POST, false)
        );
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, WEST, SOUTH, WATERLOGGED, POST);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        tooltip.add(Text.translatable("block.tooltip." + Wires.NAMESPACE + ".toggle")
            .styled(style -> style.withColor(Formatting.DARK_GRAY)));
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        var stack = player.getStackInHand(hand);
        if (!stack.isOf(Items.STICK)) {
            return ActionResult.PASS;
        }
        if (world.isClient) return ActionResult.SUCCESS;
        boolean post = state.get(POST);
        world.setBlockState(pos, state.with(POST, !post), NOTIFY_LISTENERS | FORCE_STATE);
        return ActionResult.CONSUME;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        var world = ctx.getWorld();
        var pos = ctx.getBlockPos();
        var fluid = world.getFluidState(pos);
        var pn = pos.north();
        var ps = pos.south();
        var pw = pos.west();
        var pe = pos.east();
        var sn = world.getBlockState(pn);
        var ss = world.getBlockState(ps);
        var sw = world.getBlockState(pw);
        var se = world.getBlockState(pe);
        return getDefaultState()
            .with(NORTH, connectsTo(sn, sn.isSideSolidFullSquare(world, pn, Direction.SOUTH)))
            .with(SOUTH, connectsTo(ss, ss.isSideSolidFullSquare(world, pn, Direction.NORTH)))
            .with(WEST, connectsTo(sw, sw.isSideSolidFullSquare(world, pn, Direction.EAST)))
            .with(EAST, connectsTo(se, se.isSideSolidFullSquare(world, pn, Direction.WEST)))
            .with(WATERLOGGED, fluid.getFluid() == Fluids.WATER);
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
                                                WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        return direction.getAxis().isHorizontal()
            ? state.with(FACING_PROPERTIES.get(direction),
            connectsTo(neighborState, neighborState.isSideSolidFullSquare(world, neighborPos, direction.getOpposite())))
            : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getCameraCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty();
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
        if (stateFrom.isOf(this)) {
            if (!direction.getAxis().isHorizontal()) {
                return true;
            }
            if (state.get(FACING_PROPERTIES.get(direction)) && stateFrom.get(FACING_PROPERTIES.get(direction.getOpposite()))) {
                return true;
            }
        }
        return super.isSideInvisible(state, stateFrom, direction);
    }

    public static boolean connectsTo(BlockState state, boolean solidFullSquare) {
        return ((PaneBlock) Blocks.GLASS_PANE).connectsTo(state, solidFullSquare) ||
               state.getBlock() instanceof WiresPoleBlock;
    }
}
