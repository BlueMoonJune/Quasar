package bluemoonjune.quasar.items.jetpack

import net.minecraft.entity.Entity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.world.World

class Jetpack(settings: Settings) : Item(settings) {
    companion object {
        var behavior: JetpackBehavior? = null
    }
    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        behavior?.inventoryTick(stack, world, entity, slot, selected)

    }
}