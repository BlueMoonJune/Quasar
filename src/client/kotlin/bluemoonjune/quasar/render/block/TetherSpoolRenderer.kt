package bluemoonjune.quasar.render.block

import bluemoonjune.quasar.Quasar
import bluemoonjune.quasar.blocks.ModBlocks
import bluemoonjune.quasar.blocks.block.TetherSpool
import bluemoonjune.quasar.blocks.entity.TetherSpoolEntity
import net.fabricmc.loader.impl.lib.sat4j.core.Vec
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.LightmapTextureManager
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.Vec3d
import org.joml.Quaternionf
import org.joml.Vector3d
import org.joml.Vector3f

class TetherSpoolRenderer(val ctx: BlockEntityRendererFactory.Context) : BlockEntityRenderer<TetherSpoolEntity> {
    override fun render(
        entity: TetherSpoolEntity,
        tickDelta: Float,
        mats: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int
    ) {
        val core = MinecraftClient.getInstance().bakedModelManager.getModel(Quasar.id("block/tether_spool_core")) ?: return
        val tether = MinecraftClient.getInstance().bakedModelManager.getModel(Quasar.id("block/tether_spool_tether")) ?: return
        val world = entity.world ?: return
        val buf = vertexConsumers.getBuffer(RenderLayer.getCutout())
        val state = world.getBlockState(entity.pos)
        if (!state.isOf(ModBlocks.TETHER_SPOOL)) return
        val face = state.get(TetherSpool.FACING).unitVector.mul(0.5f)
        val facerot = Quaternionf().rotateTo(Vector3f(0f, 0f, 1f), face)
        mats.push()
        mats.translate(0.5, 0.5, 0.5)
        mats.multiply(facerot)
        val rel : Vector3f
        if (entity.player != null) {
            val p = entity.player!!
            rel = Vec3d(p.prevX, p.prevY, p.prevZ).lerp(p.pos, tickDelta.toDouble()).add(0.0, p.height/2.0, 0.0).subtract(entity.pos.toCenterPos()).toVector3f().sub(face)
            mats.multiply(Quaternionf().rotateX(rel.length()))
            val scale = 1 - rel.length() / 20f
            mats.scale(1f, scale, scale)
        } else {
            rel = Vector3f()
        }
        mats.translate(-0.5, -0.5, -0.5)
        MinecraftClient.getInstance().blockRenderManager.modelRenderer.render(mats.peek(), buf, null, core, 
            1f, 1f, 1f, light, overlay)
        mats.pop()
        entity.player ?: return
        mats.translate(face.x+0.5, face.y+0.5, face.z+0.5)
        mats.multiply(facerot)
        mats.multiply(Quaternionf().rotateTo(Vector3f(0f, 0f, 1f), rel.rotate(facerot.invert())))
        mats.translate(-0.5, -0.5, 0.0)
        val len = (rel.length()-0.5f)
        for (i in 1..len.toInt()) {
            MinecraftClient.getInstance().blockRenderManager.modelRenderer.render(
                mats.peek(), buf, null, tether,
                1f, 1f, 1f, light, overlay
            )
            mats.translate(0.0, 0.0, 1.0)
        }
        mats.scale(1f, 1f, len.mod(1f))
        MinecraftClient.getInstance().blockRenderManager.modelRenderer.render(
            mats.peek(), buf, null, tether,
            1f, 1f, 1f, light, overlay
        )

    }
}