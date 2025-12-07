package bluemoonjune.quasar

import bluemoonjune.quasar.entity.ModEntities
import bluemoonjune.quasar.render.entity.RocketRenderer
import bluemoonjune.quasar.render.space.SpaceDimensionEffect
import bluemoonjune.quasar.render.space.SpaceSkyRenderer
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.fabricmc.fabric.impl.client.rendering.DimensionRenderingRegistryImpl
import net.minecraft.client.render.DimensionEffects
import net.minecraft.world.World
import net.minecraft.world.dimension.DimensionType
import net.minecraft.world.dimension.DimensionTypeRegistrar

object QuasarClient : ClientModInitializer {
	override fun onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.ROCKET, ::RocketRenderer)
        DimensionRenderingRegistry.registerSkyRenderer(Quasar.SPACE, SpaceSkyRenderer())
        DimensionRenderingRegistry.registerDimensionEffects(Quasar.id("space"), SpaceDimensionEffect(-1000f, false, DimensionEffects.SkyType.NONE, false, false))
	}
}