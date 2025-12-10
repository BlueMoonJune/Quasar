package bluemoonjune.quasar.blocks.block

import bluemoonjune.quasar.entity.ModEntities
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.FurnaceBlock
import net.minecraft.block.ShapeContext
import net.minecraft.entity.SpawnReason
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World


class CommandSeat() : Block(Settings.copy(Blocks.STONE)) {

    companion object {
        val FACING = Properties.HORIZONTAL_FACING
    }

    val shapes = arrayOf(
        VoxelShapes.union(
            createCuboidShape(0.0, 0.0, 0.0, 16.0, 6.0, 16.0),
            createCuboidShape(0.0, 0.0, 12.0, 16.0, 16.0, 16.0)
        ),
        VoxelShapes.union(
            createCuboidShape(0.0, 0.0, 0.0, 16.0, 6.0, 16.0),
            createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 4.0)
        ),
        VoxelShapes.union(
            createCuboidShape(0.0, 0.0, 0.0, 16.0, 6.0, 16.0),
            createCuboidShape(12.0, 0.0, 0.0, 16.0, 16.0, 16.0)
        ),
        VoxelShapes.union(
            createCuboidShape(0.0, 0.0, 0.0, 16.0, 6.0, 16.0),
                    createCuboidShape(0.0, 0.0, 0.0, 4.0, 16.0, 16.0)
        )
    )

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult
    ): ActionResult {
        if (player.isSneaking) return ActionResult.FAIL
        if (world !is ServerWorld) return ActionResult.SUCCESS

        val entity = world.getEntitiesByType(ModEntities.SEAT, Box(pos), { _ -> true } )
            .getOrElse(0, {_ -> ModEntities.SEAT.spawn(world, pos, SpawnReason.TRIGGERED)}) ?: return ActionResult.FAIL
        entity.setAngles(state.get(FACING).asRotation(), 0f)
        player.startRiding(entity)

        return ActionResult.SUCCESS
    }

    override fun isShapeFullCube(state: BlockState?, world: BlockView?, pos: BlockPos?) = false

    override fun getCollisionShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape {
        return shapes[state.get(FACING).id-2]
    }

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape {
        return getCollisionShape(state, world, pos, context)
    }

    override fun appendProperties(builder: StateManager.Builder<Block?, BlockState?>) {
        builder.add(FACING)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState = this.defaultState.with(FACING, ctx.horizontalPlayerFacing.opposite)
}