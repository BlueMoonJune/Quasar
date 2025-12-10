package bluemoonjune.quasar

import bluemoonjune.quasar.blocks.ModBlocks
import bluemoonjune.quasar.blocks.block.TetherSpool
import bluemoonjune.quasar.entity.ModEntities
import bluemoonjune.quasar.items.JetpackBehaviorImpl
import bluemoonjune.quasar.items.jetpack.Jetpack
import bluemoonjune.quasar.render.block.TetherSpoolRenderer
import bluemoonjune.quasar.render.entity.RocketRenderer
import bluemoonjune.quasar.render.entity.SeatEntityRenderer
import bluemoonjune.quasar.render.space.SpaceDimensionEffect
import bluemoonjune.quasar.render.space.SpaceSkyRenderer
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.impl.blockrenderlayer.BlockRenderLayerMapImpl
import net.fabricmc.fabric.impl.client.model.loading.ModelLoadingPluginManager
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.render.DimensionEffects
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories
import net.minecraft.client.render.model.BakedModel
import net.minecraft.client.render.model.BakedModelManager
import net.minecraft.client.util.InputUtil
import net.minecraft.resource.ResourceType
import net.minecraft.util.Identifier
import org.lwjgl.glfw.GLFW
import java.util.HashMap

object QuasarClient : ClientModInitializer {

    val PLANET_MODELS : Map<Identifier, BakedModel> = HashMap<Identifier, BakedModel>()

    lateinit var ROLL_KEYBIND : KeyBinding
    val KEYBIND_CATEGORY = "key.categories.quasar"

	override fun onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.ROCKET, ::RocketRenderer)
        EntityRendererRegistry.register(ModEntities.SEAT, ::SeatEntityRenderer)

        BlockEntityRendererFactories.register(ModBlocks.TETHER_SPOOL_ENTITY, ::TetherSpoolRenderer)


        DimensionRenderingRegistry.registerSkyRenderer(Quasar.SPACE, SpaceSkyRenderer())
        DimensionRenderingRegistry.registerDimensionEffects(Quasar.id("space"), SpaceDimensionEffect(-1000f, false, DimensionEffects.SkyType.NONE, false, false))

        Jetpack.behavior = JetpackBehaviorImpl()

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.TETHER_SPOOL, RenderLayer.getCutout())

        ROLL_KEYBIND = KeyBindingHelper.registerKeyBinding(KeyBinding(
            "key.quasar.roll",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_R,
            KEYBIND_CATEGORY
        ))

        ModelLoadingPluginManager.registerPlugin {
            context ->
            val ids = MinecraftClient.getInstance().resourceManager.findAllResources("models/planet", {_ -> true})
            context.addModels(ids.keys.map { id -> Identifier.of(id.namespace, id.path.replace("models/", "").replace(".json", "")) })
            context.addModels(
                Quasar.id("block/tether_spool_core"),
                Quasar.id("block/tether_spool_tether")
            )
        }
    }

}