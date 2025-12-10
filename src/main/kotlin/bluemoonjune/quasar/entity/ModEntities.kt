package bluemoonjune.quasar.entity

import bluemoonjune.quasar.Quasar
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.world.dimension.DimensionType

object ModEntities {

    val ROCKET : EntityType<Rocket> = register("rocket",
        EntityType.Builder.create(::Rocket, SpawnGroup.MISC).build()!!
        )
    val SEAT : EntityType<SeatEntity> = register("seat",
        EntityType.Builder.create(::SeatEntity, SpawnGroup.MISC).dimensions(0.5f, 0.5f).build()!!
    )

    private fun <T : EntityType<E>, E: Entity> register(name: String, entity: T): T {
        return Registry.register(Registries.ENTITY_TYPE, Quasar.id(name), entity)
    }
}