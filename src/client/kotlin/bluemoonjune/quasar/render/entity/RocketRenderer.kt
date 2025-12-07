package bluemoonjune.quasar.render.entity

import bluemoonjune.quasar.entity.Rocket
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayers
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.BlockRenderManager
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.texture.SpriteAtlasTexture
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper
import org.joml.AxisAngle4f
import org.joml.Quaternionf

class RocketRenderer(ctx: EntityRendererFactory.Context, private val manager: BlockRenderManager = ctx.blockRenderManager) : EntityRenderer<Rocket>(ctx) {
    override fun getTexture(entity: Rocket?): Identifier? = SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE

    override fun render(
        entity: Rocket?,
        yaw: Float,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int
    ) {
        entity ?: return

        matrices.push()
        matrices.multiply(Quaternionf(AxisAngle4f((entity.world.time+tickDelta)/20, 0f, 1f, 0f)))
        matrices.translate(-0.5, 0.0, -0.5)

        val anchor = entity.blockPos

        for ((pos, state) in entity.blocks) {
            matrices.push()
            matrices.translate(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())



            manager.modelRenderer.render(
                entity.world,
                manager.getModel(state),
                state,
                pos.add(anchor),
                matrices,
                vertexConsumers.getBuffer(RenderLayers.getMovingBlockLayer(state)),
                false,
                net.minecraft.util.math.random.Random.create(),
                state.getRenderingSeed(pos),
                OverlayTexture.DEFAULT_UV
            )
            matrices.pop()
        }
        matrices.pop()
    }
}