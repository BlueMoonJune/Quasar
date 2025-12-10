package bluemoonjune.quasar.items

import bluemoonjune.quasar.Quasar
import bluemoonjune.quasar.blocks.ModBlocks
import bluemoonjune.quasar.blocks.block.ControlModule
import bluemoonjune.quasar.items.jetpack.Jetpack
import com.jcraft.jorbis.Block
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.component.ComponentType
import net.minecraft.component.DataComponentTypes
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemGroup.EntryCollector
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.text.Text


object ModItems {

    val CONTROL_MODULE_ITEM: BlockItem = register(
        "control_module",
        BlockItem(ModBlocks.CONTROL_MODULE, Item.Settings())
    )
    val COMMAND_SEAT_ITEM: BlockItem = register(
        "command_seat",
        BlockItem(ModBlocks.COMMAND_SEAT, Item.Settings())
    )
    val TETHER_SPOOL: BlockItem = register(
        "tether_spool",
        BlockItem(ModBlocks.TETHER_SPOOL, Item.Settings())
    )

    val JETPACK: Jetpack = register(
        "jetpack",
        Jetpack(Item.Settings())
    )

    val ITEM_GROUP: ItemGroup? = FabricItemGroup.builder()
        .icon(CONTROL_MODULE_ITEM::getDefaultStack)
        .displayName(Text.translatable("quasar.item_group"))
        .entries(EntryCollector { context: ItemGroup.DisplayContext?, entries: ItemGroup.Entries ->
            entries.add(CONTROL_MODULE_ITEM)
            entries.add(COMMAND_SEAT_ITEM)
            entries.add(JETPACK)
        })
        .build()

    private fun <T : Item> register(name: String, item: T): T {
        return Registry.register(Registries.ITEM, Quasar.id(name), item)
    }

    fun register() {
        Registry.register(Registries.ITEM_GROUP, Quasar.id("quasar"), ITEM_GROUP)
    }
}