package bluemoonjune.quasar

import bluemoonjune.quasar.items.ModItems
import net.fabricmc.api.ModInitializer
import net.minecraft.block.BlockState
import net.minecraft.entity.data.TrackedDataHandler
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import org.slf4j.LoggerFactory

object Quasar : ModInitializer {
    const val MOD_ID: String = "quasar"
    val logger = LoggerFactory.getLogger("quasar")!!

    val SPACE = RegistryKey.of<World>(RegistryKeys.WORLD, id("space"))

	override fun onInitialize() {
		logger.info("Hello Fabric world!")

        ModItems.register()
	}

    fun id(vararg path: String): Identifier {
        return Identifier.of(MOD_ID, path.joinToString("/"))
    }
}