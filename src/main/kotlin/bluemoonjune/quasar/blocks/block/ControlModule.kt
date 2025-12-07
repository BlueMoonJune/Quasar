package bluemoonjune.quasar.blocks.block

import bluemoonjune.quasar.entity.ModEntities
import bluemoonjune.quasar.entity.Rocket
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.ItemActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import java.util.LinkedList
import java.util.Queue

class ControlModule : Block(Settings.copy(Blocks.STONE)) {

    override fun onUseWithItem(
        stack: ItemStack,
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand,
        hit: BlockHitResult
    ): ItemActionResult {
        world.playSound(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), SoundEvent.of(Identifier.of("minecraft:block.note_block.bell")), SoundCategory.BLOCKS, 1f, 1f, false)
        val traveled = HashSet<BlockPos>()
        val frontier = LinkedList<BlockPos>()
        frontier.add(pos)
        while (!frontier.isEmpty() && traveled.size < 2048) {
            val cur = frontier.pop()
            traveled.add(cur)
            world.addParticle(ParticleTypes.END_ROD, cur.x.toDouble(), cur.y.toDouble(), cur.z.toDouble(), 0.0, 0.0, 0.0)
            for (dir in Direction.entries) {
                val neighbor = cur.add(dir.vector)
                if (!traveled.contains(neighbor) && !world.getBlockState(neighbor).isAir) {
                    frontier.add(neighbor)
                }
            }
        }

        val entity = Rocket(ModEntities.ROCKET, world)
        world.spawnEntity(entity)
        entity.setPos(pos.x + 0.5, pos.y.toDouble(), pos.z + 0.5)

        entity.blocks = traveled.fold(HashMap<BlockPos, BlockState>(), {map, cur ->
            map[cur.subtract(pos)] = world.getBlockState(cur)
            map
        })

        traveled.map { pos -> world.setBlockState(pos, Blocks.AIR.defaultState) }

        return ItemActionResult.SUCCESS
    }

}