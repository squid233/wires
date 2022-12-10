package io.github.squid233.wires.client.render.block.entity;

import io.github.squid233.wires.block.InsulatorBlock;
import io.github.squid233.wires.block.entity.InsulatorBlockEntity;
import io.github.squid233.wires.util.MutableVec3d;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

/**
 * @author squid233
 * @since 0.1.0
 */
public final class InsulatorBlockEntityRenderer extends BlockEntityRenderer<InsulatorBlockEntity> {
    private static final int SEGMENTS = 8;
    private static final double SEGMENTS_inv = 1.0 / (double) SEGMENTS;
    private static final float SEGMENTS_invf = 1.0f / (float) SEGMENTS;
    private static final double C = 2.0;
    private static final double C_inv = 1.0 / C;
    private static final double OFFSET = offsetY(0.0);

    public InsulatorBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(InsulatorBlockEntity entity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (entity.getCachedState().get(InsulatorBlock.CONNECTED)) {
            var list = entity.getConnectedTo();
            var buffer = vertexConsumers.getBuffer(RenderLayer.getLines());
            var pos = entity.getPos();
            var offset = entity.getRenderOffset();
            var world = entity.getWorld();
            matrices.push();
            matrices.translate(offset.getX(), offset.getY(), offset.getZ());
            var entry = matrices.peek();
            for (var target : list) {
                render(world, target, pos, offset, buffer, entry);
            }
            matrices.pop();
        }
    }

    private static void render(World world, BlockPos target, BlockPos pos,
                               MutableVec3d offset, VertexConsumer buffer, MatrixStack.Entry entry) {
        if (world != null && world.getBlockEntity(target) instanceof InsulatorBlockEntity insulator) {
            var targetO = insulator.getRenderOffset();
            float x = target.getX() - pos.getX() - (float) offset.getX() + (float) targetO.getX();
            float y = target.getY() - pos.getY() - (float) offset.getY() + (float) targetO.getY();
            float z = target.getZ() - pos.getZ() - (float) offset.getZ() + (float) targetO.getZ();
            float x0, y0, z0;
            float x1, y1, z1;
            for (int i = 0; i < SEGMENTS; i++) {
                x0 = lerp(x, i);
                y0 = lerp(y, i);
                z0 = lerp(z, i);
                x1 = lerp(x, i + 1);
                y1 = lerp(y, i + 1);
                z1 = lerp(z, i + 1);
                renderLine(i,
                    x0, y0, z0,
                    x1, y1, z1,
                    buffer, entry);
                renderLine(i + 1,
                    x1, y1, z1,
                    lerp(x, i + 2), lerp(y, i + 2), lerp(z, i + 2),
                    buffer, entry);
                renderLine(i,
                    x0, y0, z0,
                    x0, y0 - 1f, z0,
                    buffer, entry);
                renderLineInv(x0, y0, z0, x0, y0 - 1f, z0, buffer, entry);
            }
            renderLine(0f, -1f, 0f, x, y - 1f, z, buffer, entry);
            renderLineInv(0f, -1f, 0f, x, y - 1f, z, buffer, entry);
        }
    }

    private static float lerp(float end, int v) {
        return end * (float) v * SEGMENTS_invf;
    }

    private static double offsetY(double x) {
        return C * Math.cosh((2 * x - 1.0) * C_inv) - C;
    }

    private static void renderLine(int seg,
                                   float x0, float y0, float z0,
                                   float x1, float y1, float z1,
                                   VertexConsumer buffer, MatrixStack.Entry matrices) {
        y0 += offsetY((double) seg * SEGMENTS_inv) - OFFSET;
        y1 += offsetY((double) (seg + 1) * SEGMENTS_inv) - OFFSET;
        renderLine(x0, y0, z0, x1, y1, z1, buffer, matrices);
    }

    private static void renderLineInv(float x0, float y0, float z0,
                                      float x1, float y1, float z1,
                                      VertexConsumer buffer, MatrixStack.Entry matrices) {
        float nx = x1 - x0;
        float ny = y1 - y0;
        float nz = z1 - z0;
        float mag = MathHelper.sqrt(nx * nx + ny * ny + nz * nz);
        // Normalize
        nx /= mag;
        ny /= mag;
        nz /= mag;
        buffer.vertex(matrices.getModel(), x1, y1, z1)
            .color(0, 0, 0, 255)
            .normal(matrices.getNormal(), nx, ny, nz)
            .next();
        buffer.vertex(matrices.getModel(), x1, y1, z1)
            .color(0, 0, 0, 255)
            .normal(matrices.getNormal(), nx, ny, nz)
            .next();
    }

    private static void renderLine(float x0, float y0, float z0,
                                   float x1, float y1, float z1,
                                   VertexConsumer buffer, MatrixStack.Entry matrices) {
        float nx = x1 - x0;
        float ny = y1 - y0;
        float nz = z1 - z0;
        float mag = MathHelper.sqrt(nx * nx + ny * ny + nz * nz);
        // Normalize
        nx /= mag;
        ny /= mag;
        nz /= mag;
        buffer.vertex(matrices.getModel(), x0, y0, z0)
            .color(0, 0, 0, 255)
            .normal(matrices.getNormal(), nx, ny, nz)
            .next();
    }
}
