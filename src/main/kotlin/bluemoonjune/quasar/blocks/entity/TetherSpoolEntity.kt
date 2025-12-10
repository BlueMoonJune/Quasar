package bluemoonjune.quasar.blocks.entity

import bluemoonjune.quasar.blocks.ModBlocks
import bluemoonjune.quasar.blocks.block.TetherSpool
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import java.util.Stack

class TetherSpoolEntity(pos: BlockPos, state: BlockState) : BlockEntity(ModBlocks.TETHER_SPOOL_ENTITY, pos, state) {
    companion object {
        fun tick(world: World, pos: BlockPos, state: BlockState, entity: TetherSpoolEntity) = entity.tick(world, pos, state)
    }

    var player: PlayerEntity? = null
    val points = Stack<Pair<Double, Vec3d>>()

    private fun tick(
        world: World,
        tether: BlockPos,
        state: BlockState
    ) {
        if (!world.isClient) return
        val p = player ?: return
        val origin = tether.add(state.get(TetherSpool.FACING).vector).toCenterPos()
        val pos = p.pos.add(0.0, p.height / 2.0, 0.0)
        val next = pos.add(p.velocity)
        val rel = next.subtract(origin)
        if (rel.lengthSquared() > 16*16) {
            p.velocity = rel.normalize().multiply(16.0).add(origin).subtract(pos)
            p.velocityModified = true
        }
    }
}