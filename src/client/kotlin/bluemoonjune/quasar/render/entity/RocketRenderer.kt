package bluemoonjune.quasar.render.entity

import bluemoonjune.quasar.entity.Rocket
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayers
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.BlockRenderManager
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.texture.SpriteAtlasTexture
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.component.type.MapIdComponent
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.Fluid
import net.minecraft.item.map.MapState
import net.minecraft.recipe.BrewingRecipeRegistry
import net.minecraft.recipe.RecipeManager
import net.minecraft.registry.DynamicRegistryManager
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.resource.featuretoggle.FeatureSet
import net.minecraft.scoreboard.Scoreboard
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.util.profiler.Profiler
import net.minecraft.world.MutableWorldProperties
import net.minecraft.world.biome.Biome
import net.minecraft.world.chunk.ChunkManager
import net.minecraft.world.dimension.DimensionType
import net.minecraft.world.entity.EntityLookup
import net.minecraft.world.event.GameEvent
import net.minecraft.world.tick.QueryableTickScheduler
import net.minecraft.world.tick.TickManager
import org.joml.AxisAngle4f
import org.joml.Quaternionf
import java.util.ArrayList
import java.util.function.Supplier

class RocketRenderer(ctx: EntityRendererFactory.Context, private val manager: BlockRenderManager = ctx.blockRenderManager) : EntityRenderer<Rocket>(ctx) {
    override fun getTexture(entity: Rocket?): Identifier? = SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE

    override fun render(
        entity: Rocket,
        yaw: Float,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int
    ) {
        matrices.push()
        matrices.multiply(Quaternionf(AxisAngle4f((entity.world.time + tickDelta) / 20, 0f, 1f, 0f)))
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
                true,
                net.minecraft.util.math.random.Random.create(),
                state.getRenderingSeed(pos),
                OverlayTexture.DEFAULT_UV
            )
            matrices.pop()
        }
        matrices.pop()
    }
}