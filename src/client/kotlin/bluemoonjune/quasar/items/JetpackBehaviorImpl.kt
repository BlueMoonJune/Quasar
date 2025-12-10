package bluemoonjune.quasar.items

import bluemoonjune.quasar.items.jetpack.JetpackBehavior
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.entity.Entity
import net.minecraft.item.ItemStack
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

class JetpackBehaviorImpl : JetpackBehavior {
    override fun inventoryTick(
        stack: ItemStack,
        world: World,
        entity: Entity,
        slot: Int,
        selected: Boolean
    ) {
        if (entity is ClientPlayerEntity) {
            val input = entity.input!!
            input.tick(false, 0f)
            val move = Vec3d(
                getMovementMultiplier(input.pressingLeft, input.pressingRight),
                getMovementMultiplier(input.jumping, input.sneaking),
                getMovementMultiplier(input.pressingForward, input.pressingBack)
            ).rotateY(-entity.yaw * MathHelper.PI / 180)
            entity.velocity = entity.velocity.add(move.multiply(0.05))
        }
    }

    private fun getMovementMultiplier(positive: Boolean, negative: Boolean): Double {
        if (positive == negative) {
            return 0.0
        } else {
            return if (positive) 1.0 else -1.0
        }
    }

}