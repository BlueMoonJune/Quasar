package bluemoonjune.quasar.blocks

import bluemoonjune.quasar.Quasar
import bluemoonjune.quasar.blocks.block.ControlModule
import bluemoonjune.quasar.items.ModItems.ITEM_GROUP
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object ModBlocks {

    val CONTROL_MODULE : ControlModule = register("control_module", ControlModule())

    private fun <T : Block> register(name: String, block: T): T {
        return Registry.register(Registries.BLOCK, Quasar.id(name), block)
    }
}