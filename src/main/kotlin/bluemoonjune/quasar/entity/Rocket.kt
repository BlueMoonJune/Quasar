package bluemoonjune.quasar.entity

import com.google.common.collect.Maps
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.component.type.MapIdComponent
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.Fluid
import net.minecraft.item.map.MapState
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtHelper
import net.minecraft.nbt.NbtList
import net.minecraft.recipe.BrewingRecipeRegistry
import net.minecraft.recipe.RecipeManager
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.resource.featuretoggle.FeatureSet
import net.minecraft.scoreboard.Scoreboard
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.world.MutableWorldProperties
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import net.minecraft.world.chunk.ChunkManager
import net.minecraft.world.entity.EntityLookup
import net.minecraft.world.event.GameEvent
import net.minecraft.world.tick.QueryableTickScheduler
import net.minecraft.world.tick.TickManager

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

//    class Shell(
//        val rocket: Rocket,
//    ) : net.minecraft.world.World(
//        (rocket.world.levelProperties) as MutableWorldProperties,
//        rocket.world.registryKey,
//        rocket.world.registryManager,
//        rocket.world.dimensionEntry,
//        rocket.world.profilerSupplier,
//        rocket.world.isClient,
//        rocket.world.isDebugWorld,
//        0,
//        2048
//    ) {
//
//        val blockStates = HashMap<BlockPos, BlockState>()
//        val blockEntityNbts = HashMap<BlockPos, NbtCompound>()
//        val blockEntities = HashMap<BlockPos, BlockEntity>()
//
////        fun writeNbt(): NbtCompound {
////            val tag = NbtCompound()
////            blockStates
////                .filterNot { (key, value) -> value.isAir() }
////                .toList().fold( NbtList(), {
////                    i, (pos, state) ->
////                    val entry = NbtCompound()
////                    entry.put("pos", NbtHelper.fromBlockPos(pos))
////                    entry.put("state", NbtHelper.fromBlockState(state))
////                    if (blockEntities.containsKey(pos)) {
////                        val entity = blockEntities[pos]
////                        entry.put("entity", entity.)
////                    }
////                    i.add(entry)
////                    i
////                })
////        }
//
//        override fun updateListeners(
//            pos: BlockPos?,
//            oldState: BlockState?,
//            newState: BlockState?,
//            flags: Int
//        ) {
//            TODO("Not yet implemented")
//        }
//
//        override fun playSound(
//            source: PlayerEntity?,
//            x: Double,
//            y: Double,
//            z: Double,
//            sound: RegistryEntry<SoundEvent?>?,
//            category: SoundCategory?,
//            volume: Float,
//            pitch: Float,
//            seed: Long
//        ) {
//            TODO("Not yet implemented")
//        }
//
//        override fun playSoundFromEntity(
//            source: PlayerEntity?,
//            entity: Entity?,
//            sound: RegistryEntry<SoundEvent?>?,
//            category: SoundCategory?,
//            volume: Float,
//            pitch: Float,
//            seed: Long
//        ) {
//            TODO("Not yet implemented")
//        }
//
//        override fun asString(): String = "Rocket:%s Shell".format(rocket.uuidAsString)
//
//        override fun getEntityById(id: Int): Entity? {
//            TODO("Not yet implemented")
//        }
//
//        override fun getTickManager(): TickManager? {
//            TODO("Not yet implemented")
//        }
//
//        override fun getMapState(id: MapIdComponent?): MapState? {
//            TODO("Not yet implemented")
//        }
//
//        override fun putMapState(
//            id: MapIdComponent?,
//            state: MapState?
//        ) {
//            TODO("Not yet implemented")
//        }
//
//        override fun increaseAndGetMapId(): MapIdComponent? {
//            TODO("Not yet implemented")
//        }
//
//        override fun setBlockBreakingInfo(
//            entityId: Int,
//            pos: BlockPos?,
//            progress: Int
//        ) {
//            TODO("Not yet implemented")
//        }
//
//        override fun getScoreboard(): Scoreboard? {
//            TODO("Not yet implemented")
//        }
//
//        override fun getRecipeManager(): RecipeManager? {
//            TODO("Not yet implemented")
//        }
//
//        override fun getEntityLookup(): EntityLookup<Entity?>? {
//            TODO("Not yet implemented")
//        }
//
//        override fun getBrewingRecipeRegistry(): BrewingRecipeRegistry? {
//            TODO("Not yet implemented")
//        }
//
//        override fun getBlockTickScheduler(): QueryableTickScheduler<Block?>? {
//            TODO("Not yet implemented")
//        }
//
//        override fun getFluidTickScheduler(): QueryableTickScheduler<Fluid?>? {
//            TODO("Not yet implemented")
//        }
//
//        override fun getChunkManager(): ChunkManager? {
//            TODO("Not yet implemented")
//        }
//
//        override fun syncWorldEvent(
//            player: PlayerEntity?,
//            eventId: Int,
//            pos: BlockPos?,
//            data: Int
//        ) {
//
//        }
//
//        override fun emitGameEvent(
//            event: RegistryEntry<GameEvent?>?,
//            emitterPos: Vec3d?,
//            emitter: GameEvent.Emitter?
//        ) {
//
//        }
//
//        override fun getPlayers(): List<PlayerEntity?> = arrayListOf()
//
//        override fun getGeneratorStoredBiome(
//            biomeX: Int,
//            biomeY: Int,
//            biomeZ: Int
//        ): RegistryEntry<Biome?>? = null
//
//        override fun getEnabledFeatures(): FeatureSet = FeatureSet.empty()
//
//        override fun getBrightness(
//            direction: Direction?,
//            shaded: Boolean
//        ): Float = 1f
//
//    }
}