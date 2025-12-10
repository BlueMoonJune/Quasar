package bluemoonjune.quasar.items.jetpack

import net.minecraft.entity.Entity
import net.minecraft.item.ItemStack
import net.minecraft.world.World

interface JetpackBehavior {
    fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean);
}