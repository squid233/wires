package io.github.squid233.wires.client.render.block.entity;

import io.github.squid233.wires.block.InsulatorBlock;
import io.github.squid233.wires.block.entity.InsulatorBlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

/**
 * @author squid233
 * @since 0.1.0
 */
public final class InsulatorBlockEntityRenderer implements BlockEntityRenderer<InsulatorBlockEntity> {
    public InsulatorBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
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
                if (world != null && world.getBlockEntity(target) instanceof InsulatorBlockEntity insulator) {
                    var targetO = insulator.getRenderOffset();
                    float x = target.getX() - pos.getX() - (float) offset.getX() + (float) targetO.getX();
                    float y = target.getY() - pos.getY() - (float) offset.getY() + (float) targetO.getY();
                    float z = target.getZ() - pos.getZ() - (float) offset.getZ() + (float) targetO.getZ();
                    renderLine(0f, 0f, 0f, buffer, entry);
                    renderLine(x, y, z, buffer, entry);
                }
            }
            matrices.pop();
        }
    }

    @Override
    public int getRenderDistance() {
        return 96;
    }

    private static void renderLine(float x, float y, float z,
                                   VertexConsumer buffer, MatrixStack.Entry matrices) {
        float nx = x;
        float ny = y;
        float nz = z;
        float mag = MathHelper.sqrt(nx * nx + ny * ny + nz * nz);
        // Normalize
        nx /= mag;
        ny /= mag;
        nz /= mag;
        buffer.vertex(matrices.getPositionMatrix(), x, y, z)
            .color(0, 0, 0, 255)
            .normal(matrices.getNormalMatrix(), nx, ny, nz)
            .next();
    }
}
