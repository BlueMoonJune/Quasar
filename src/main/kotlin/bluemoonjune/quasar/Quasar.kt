package bluemoonjune.quasar

import bluemoonjune.quasar.items.ModItems
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.data.TrackedDataHandler
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.resource.ResourceType
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World
import net.minecraft.world.dimension.DimensionType
import org.slf4j.LoggerFactory

object Quasar : ModInitializer {
    const val MOD_ID: String = "quasar"
    val logger = LoggerFactory.getLogger("quasar")!!

    val SPACE = RegistryKey.of<World>(RegistryKeys.WORLD, id("space"))

	override fun onInitialize() {
		logger.info("Hello Fabric world!")

        ModItems.register()

        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(PlanetReloadListener())
	}

    fun id(vararg path: String): Identifier {
        return Identifier.of(MOD_ID, path.joinToString("/"))
    }

    fun hitboxTest(original: Box): Box {

        return original.contract(0.0, (original.lengthY - original.lengthX) / 2, 0.0)
    }
}