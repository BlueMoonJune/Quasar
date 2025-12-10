package bluemoonjune.quasar

import net.minecraft.entity.Entity
import net.minecraft.world.World

object Utils {
    fun hasGravity(entity: Entity): Boolean {
        return hasGravity(entity.world)
    }

    fun hasGravity(world: World): Boolean {
        return world.registryKey == Quasar.SPACE
    }

    fun hasDrag(entity: Entity): Boolean {
        return hasGravity(entity.world)
    }

    fun hasDrag(world: World): Boolean {
        return world.registryKey == Quasar.SPACE
    }
}