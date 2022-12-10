package io.github.squid233.wires.block;

import io.github.squid233.wires.Wires;
import io.github.squid233.wires.block.entity.InsulatorBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
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
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author squid233
 * @since 0.1.0
 */
public final class InsulatorBlock extends Block implements BlockEntityProvider {
    public static final DirectionProperty FACING = Properties.FACING;
    public static final BooleanProperty CONNECTED = BooleanProperty.of("connected");
    public static final BooleanProperty INVISIBLE = BooleanProperty.of("invisible");

    public InsulatorBlock(Settings settings) {
        super(settings);
        setDefaultState(stateManager.getDefaultState()
            .with(FACING, Direction.NORTH)
            .with(CONNECTED, false)
            .with(INVISIBLE, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, CONNECTED, INVISIBLE);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        tooltip.add(new TranslatableText("block.tooltip." + Wires.NAMESPACE + ".toggle")
            .styled(style -> style.withColor(Formatting.DARK_GRAY)));
        tooltip.add(new TranslatableText("block.tooltip." + Wires.NAMESPACE + ".insulator")
            .styled(style -> style.withColor(Formatting.DARK_GRAY)));
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);
        if (!(stack.getItem() == Items.STICK)) {
            return ActionResult.PASS;
        }
        if (world.isClient) return ActionResult.SUCCESS;
        boolean invisible = state.get(INVISIBLE);
        world.setBlockState(pos, state.with(INVISIBLE, !invisible), 2 | 16);
        return ActionResult.CONSUME;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return state.get(INVISIBLE) ? BlockRenderType.INVISIBLE : BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(FACING, ctx.getSide().getOpposite());
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        switch (state.get(FACING)) {
            case DOWN:
                return createCuboidShape(6, -1, 6, 10, 8, 10);
            case UP:
                return createCuboidShape(6, 8, 6, 10, 17, 10);
            case NORTH:
                return createCuboidShape(6, 6, -1, 10, 10, 8);
            case SOUTH:
                return createCuboidShape(6, 6, 8, 10, 10, 17);
            case WEST:
                return createCuboidShape(-1, 6, 6, 8, 10, 10);
            case EAST:
                return createCuboidShape(8, 6, 6, 17, 10, 10);
        }
        return VoxelShapes.empty();
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (state.get(INVISIBLE)) {
            return VoxelShapes.empty();
        }
        switch (state.get(FACING)) {
            case DOWN:
                return createCuboidShape(6, 0, 6, 10, 8, 10);
            case UP:
                return createCuboidShape(6, 8, 6, 10, 16, 10);
            case NORTH:
                return createCuboidShape(6, 6, 0, 10, 10, 8);
            case SOUTH:
                return createCuboidShape(6, 6, 8, 10, 10, 16);
            case WEST:
                return createCuboidShape(0, 6, 6, 8, 10, 10);
            case EAST:
                return createCuboidShape(8, 6, 6, 16, 10, 10);
        }
        return VoxelShapes.empty();
    }

    @Override
    public BlockEntity createBlockEntity(BlockView blockView) {
        return new InsulatorBlockEntity();
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (world.getBlockEntity(pos) instanceof InsulatorBlockEntity) {
            InsulatorBlockEntity insulator = (InsulatorBlockEntity) world.getBlockEntity(pos);
            if (insulator != null) {
                insulator.disconnectAll();
            }
        }
        super.onBreak(world, pos, state, player);
    }
}
