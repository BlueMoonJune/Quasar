package bluemoonjune.quasar.blocks

import bluemoonjune.quasar.Quasar
import bluemoonjune.quasar.blocks.block.CommandSeat
import bluemoonjune.quasar.blocks.block.ControlModule
import bluemoonjune.quasar.blocks.block.TetherSpool
import bluemoonjune.quasar.blocks.entity.TetherSpoolEntity
import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.block.entity.BlockEntityType.BlockEntityFactory
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier


object ModBlocks {

    val CONTROL_MODULE : ControlModule = register("control_module", ControlModule())
    val COMMAND_SEAT : CommandSeat = register("command_seat", CommandSeat())
    val TETHER_SPOOL : TetherSpool = register("tether_spool", TetherSpool())

    val TETHER_SPOOL_ENTITY : BlockEntityType<TetherSpoolEntity> = registerEntityType("tether_spool", ::TetherSpoolEntity, TETHER_SPOOL)

    private fun <T : Block> register(name: String, block: T): T {
        return Registry.register(Registries.BLOCK, Quasar.id(name), block)
    }


    fun <T : BlockEntity> registerEntityType(
        name: String,
        entityFactory: BlockEntityFactory<out T>,
        vararg blocks: Block
    ): BlockEntityType<T> {
        val id = Quasar.id(name)
        return Registry.register<BlockEntityType<*>, BlockEntityType<T>>(
            Registries.BLOCK_ENTITY_TYPE,
            id,
            BlockEntityType.Builder.create<T>(entityFactory, *blocks).build()
        )
    }
}