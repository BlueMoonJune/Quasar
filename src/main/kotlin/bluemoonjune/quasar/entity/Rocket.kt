package bluemoonjune.quasar.entity

import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtHelper
import net.minecraft.nbt.NbtList
import net.minecraft.registry.RegistryEntryLookup
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.util.HashMap

class Rocket(type: EntityType<Rocket>, world: World) : Entity(type, world) {

    private var _blocks = HashMap<BlockPos, BlockState>()
    var blocks : HashMap<BlockPos, BlockState>
        get() {
            readSyncedDataFromNbt(dataTracker.get(SYNCED))
            return _blocks
        }
        set(value) {
            _blocks = value
            val tag = NbtCompound()
            writeSyncedDataToNbt(tag)
            dataTracker.set(SYNCED, tag)
        }

    override fun initDataTracker(builder: DataTracker.Builder) {
        builder.add(SYNCED, NbtCompound())
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound?) {
        nbt ?: return
        dataTracker.set(SYNCED, nbt)
    readSyncedDataFromNbt(nbt)
    }

    fun readSyncedDataFromNbt(nbt: NbtCompound) {
        val list = nbt.getList("blocks", NbtCompound.COMPOUND_TYPE.toInt())
        for (tag in list) {
            tag ?: continue
            if (tag is NbtCompound) {
                val pos = NbtHelper.toBlockPos(tag, "pos") ?: continue
                if (pos.isEmpty) continue
                val stateData = tag.getCompound("state") ?: continue
                val state = NbtHelper.toBlockState(world.createCommandRegistryWrapper(RegistryKeys.BLOCK), stateData) ?: continue
                _blocks[pos.get()] = state
            }
        }
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound?) {
        writeSyncedDataToNbt(nbt ?: NbtCompound())
        dataTracker.set(SYNCED, nbt)
    }

    fun writeSyncedDataToNbt(nbt: NbtCompound) {
        val list = NbtList()
        for ((pos, state) in _blocks) {
            val tag = NbtCompound()
            tag.put("pos", NbtHelper.fromBlockPos(pos))
            tag.put("state", NbtHelper.fromBlockState(state))
            list.add(tag)
        }
        (nbt).put("blocks", list)
    }

    override fun kill() {
        for ((pos, state) in blocks) {
            world.setBlockState(pos.add(blockPos), state)
        }
        super.kill()
    }

    companion object {
        val SYNCED = DataTracker.registerData(Rocket::class.java, TrackedDataHandlerRegistry.NBT_COMPOUND)
    }
}