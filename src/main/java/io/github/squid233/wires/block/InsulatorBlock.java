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
public final class InsulatorBlock extends BlockWithEntity {
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

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        var stack = player.getStackInHand(hand);
        if (!stack.isOf(Items.STICK)) {
            return ActionResult.PASS;
        }
        if (world.isClient) return ActionResult.SUCCESS;
        boolean invisible = state.get(INVISIBLE);
        world.setBlockState(pos, state.with(INVISIBLE, !invisible), NOTIFY_LISTENERS | FORCE_STATE);
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

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case DOWN -> createCuboidShape(6, -1, 6, 10, 8, 10);
            case UP -> createCuboidShape(6, 8, 6, 10, 17, 10);
            case NORTH -> createCuboidShape(6, 6, -1, 10, 10, 8);
            case SOUTH -> createCuboidShape(6, 6, 8, 10, 10, 17);
            case WEST -> createCuboidShape(-1, 6, 6, 8, 10, 10);
            case EAST -> createCuboidShape(8, 6, 6, 17, 10, 10);
        };
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (state.get(INVISIBLE)) {
            return VoxelShapes.empty();
        }
        return switch (state.get(FACING)) {
            case DOWN -> createCuboidShape(6, 0, 6, 10, 8, 10);
            case UP -> createCuboidShape(6, 8, 6, 10, 16, 10);
            case NORTH -> createCuboidShape(6, 6, 0, 10, 10, 8);
            case SOUTH -> createCuboidShape(6, 6, 8, 10, 10, 16);
            case WEST -> createCuboidShape(0, 6, 6, 8, 10, 10);
            case EAST -> createCuboidShape(8, 6, 6, 16, 10, 10);
        };
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new InsulatorBlockEntity(pos, state);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (world.getBlockEntity(pos) instanceof InsulatorBlockEntity insulator) {
            insulator.disconnectAll();
        }
        super.onBreak(world, pos, state, player);
    }
}
