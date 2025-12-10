package bluemoonjune.quasar.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.vehicle.BoatEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World

class SeatEntity(type: EntityType<*>, world: World) : Entity(type, world) {
    override fun initDataTracker(builder: DataTracker.Builder?) {

    }

    override fun readCustomDataFromNbt(nbt: NbtCompound?) {

    }

    override fun writeCustomDataToNbt(nbt: NbtCompound?) {

    }

    override fun removePassenger(passenger: Entity?) {
        super.removePassenger(passenger)
        this.kill()
    }

    override fun shouldRender(distance: Double): Boolean {
        return false
    }


    fun clampPassengerYaw(passenger: Entity) {
        passenger.bodyYaw = this.yaw
        val f = MathHelper.wrapDegrees(passenger.yaw - this.yaw)
        val g = MathHelper.clamp(f, -105.0f, 105.0f)
        passenger.prevYaw += g - f
        passenger.yaw = passenger.yaw + g - f
        passenger.headYaw = passenger.yaw
    }

    override fun onPassengerLookAround(passenger: Entity) {
        this.clampPassengerYaw(passenger)
    }
}