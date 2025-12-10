package bluemoonjune.quasar.blocks.block

import bluemoonjune.quasar.blocks.ModBlocks
import bluemoonjune.quasar.blocks.entity.TetherSpoolEntity
import com.mojang.serialization.MapCodec
import net.minecraft.block.Block
import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.Blocks
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import kotlin.jvm.optionals.getOrNull

class TetherSpool() : BlockWithEntity(Settings.copy(Blocks.STONE).nonOpaque()), BlockEntityProvider {

    companion object {
        val FACING = Properties.FACING
    }

    override fun getCodec(): MapCodec<out BlockWithEntity?>? = MapCodec.unit(ModBlocks.TETHER_SPOOL)

    override fun createBlockEntity(
        pos: BlockPos,
        state: BlockState
    ): BlockEntity {
        return TetherSpoolEntity(pos, state)
    }

    override fun <T : BlockEntity?> getTicker(
        world: World?,
        state: BlockState?,
        type: BlockEntityType<T?>?
    ): BlockEntityTicker<T?>? {
        return validateTicker(type, ModBlocks.TETHER_SPOOL_ENTITY, TetherSpoolEntity::tick);
    }

    override fun getRenderType(state: BlockState?): BlockRenderType = BlockRenderType.MODEL

    override fun appendProperties(builder: StateManager.Builder<Block?, BlockState?>) {
        builder.add(FACING)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState = this.defaultState.with(FACING, ctx.playerLookDirection.opposite)

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult
    ): ActionResult? {
        val entity = world.getBlockEntity(pos, ModBlocks.TETHER_SPOOL_ENTITY).getOrNull() ?: return ActionResult.FAIL
        if (entity.player == null) {
            entity.player = player
            player.sendMessage(Text.translatable("block.quasar.tether.attach"), true)
            return ActionResult.SUCCESS
        } else if (entity.player == player) {
            entity.player = null
            player.sendMessage(Text.translatable("block.quasar.tether.detach"), true)
            return ActionResult.SUCCESS
        }
        player.sendMessage(Text.translatable("block.quasar.tether.failed"), true)
        return ActionResult.FAIL
    }
}