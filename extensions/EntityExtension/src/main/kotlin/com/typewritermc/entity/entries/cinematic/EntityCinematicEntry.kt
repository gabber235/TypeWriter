package com.typewritermc.entity.entries.cinematic

import com.github.retrooper.packetevents.protocol.entity.pose.EntityPose
import com.github.retrooper.packetevents.protocol.player.EquipmentSlot.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.emptyRef
import com.typewritermc.core.extension.annotations.*
import com.typewritermc.core.utils.failure
import com.typewritermc.core.utils.ok
import com.typewritermc.core.utils.point.Coordinate
import com.typewritermc.engine.paper.content.ContentContext
import com.typewritermc.engine.paper.content.ContentMode
import com.typewritermc.engine.paper.content.components.cinematic
import com.typewritermc.engine.paper.content.components.exit
import com.typewritermc.engine.paper.content.fieldValue
import com.typewritermc.engine.paper.content.modes.*
import com.typewritermc.engine.paper.entry.AssetManager
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.engine.paper.entry.cinematic.SimpleCinematicAction
import com.typewritermc.engine.paper.entry.entity.*
import com.typewritermc.engine.paper.entry.entries.*
import com.typewritermc.engine.paper.extensions.packetevents.ArmSwing
import com.typewritermc.engine.paper.extensions.packetevents.toPacketItem
import com.typewritermc.engine.paper.utils.toBukkitLocation
import com.typewritermc.engine.paper.utils.toCoordinate
import com.typewritermc.engine.paper.utils.toWorld
import com.typewritermc.entity.entries.data.minecraft.*
import com.typewritermc.entity.entries.data.minecraft.living.EquipmentProperty
import io.papermc.paper.event.player.PlayerArmSwingEvent
import org.bukkit.SoundCategory
import org.bukkit.entity.Player
import org.bukkit.entity.Pose
import org.bukkit.event.EventHandler
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.reflect.KClass

@Entry("entity_cinematic", "Use an animated entity in a cinematic", Colors.PINK, "material-symbols:identity-platform")
/**
 * The `Entity Cinematic` entry that plays a recorded animation on an Entity back on the player.
 *
 * ## How could this be used?
 *
 * This could be used to create a cinematic entities that are animated.
 */
class EntityCinematicEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    @Help("The entity that will be used in the cinematic")
    val definition: Ref<EntityDefinitionEntry> = emptyRef(),
    @Segments(Colors.PINK, "fa6-solid:person-walking")
    val segments: List<EntityRecordedSegment> = emptyList(),
) : CinematicEntry {
    override fun create(player: Player): CinematicAction = EntityCinematicAction(player, this)
    override fun createRecording(player: Player): CinematicAction? = null
}

@Entry(
    "entity_cinematic_artifact",
    "The artifact for the recorded interactions data",
    Colors.PINK,
    "fa6-solid:person-walking"
)
@Tags("entity_cinematic_artifact")
/**
 * The `Entity Cinematic Artifact` entry that is used to store the recorded interactions' data.
 *
 * ## How could this be used?
 *
 * This could be used to store the recorded interactions data for an entity.
 */
class EntityCinematicArtifact(
    override val id: String = "",
    override val name: String = "",
    override val artifactId: String = "",
) : ArtifactEntry

data class EntityRecordedSegment(
    override val startFrame: Int = 0,
    override val endFrame: Int = 0,
    @Help("The artifact for the recorded interactions data")
    @ContentEditor(EntityCinematicViewing::class)
    val artifact: Ref<EntityCinematicArtifact> = emptyRef(),
) : Segment

class EntityCinematicAction(
    private val player: Player,
    private val entry: EntityCinematicEntry,
) :
    SimpleCinematicAction<EntityRecordedSegment>() {
    private val assetManager: AssetManager by KoinJavaComponent.inject(AssetManager::class.java)
    private val gson: Gson by KoinJavaComponent.inject(Gson::class.java, named("bukkitDataParser"))

    override val segments: List<EntityRecordedSegment>
        get() = entry.segments

    private var entity: FakeEntity? = null
    private var collectors: List<PropertyCollector<EntityProperty>> = emptyList()
    private var recordings: Map<String, Tape<EntityFrame>> = emptyMap()

    private var currentData: EntityFrame = EntityFrame()

    override suspend fun setup() {
        super.setup()

        recordings = entry.segments
            .associate { it.artifact.id to it.artifact.get() }
            .filterValues { it != null }
            .mapValues { assetManager.fetchAsset(it.value!!) }
            .filterValues { it != null }
            .mapValues { gson.fromJson(it.value, object : TypeToken<Tape<EntityFrame>>() {}.type) }
    }

    override suspend fun startSegment(segment: EntityRecordedSegment) {
        super.startSegment(segment)
        val recording = recordings[segment.artifact.id] ?: return
        val definition = entry.definition.get() ?: return
        this.entity = definition.create(player)

        val prioritizedPropertySuppliers = definition.data.withPriority() +
                (FakeProvider(PositionProperty::class) { currentData.location?.toProperty(player.world.toWorld()) } to Int.MAX_VALUE) +
                (FakeProvider(PoseProperty::class) { currentData.pose?.toProperty() } to Int.MAX_VALUE) +
                (FakeProvider(ArmSwingProperty::class) { currentData.swing?.toProperty() } to Int.MAX_VALUE) +
                (FakeProvider(EquipmentProperty::class) {
                    val equipment =
                        mutableMapOf<com.github.retrooper.packetevents.protocol.player.EquipmentSlot, com.github.retrooper.packetevents.protocol.item.ItemStack>()
                    currentData.mainHand?.let { equipment[MAIN_HAND] = it.toPacketItem() }
                    currentData.offHand?.let { equipment[OFF_HAND] = it.toPacketItem() }
                    currentData.helmet?.let { equipment[HELMET] = it.toPacketItem() }
                    currentData.chestplate?.let { equipment[CHEST_PLATE] = it.toPacketItem() }
                    currentData.leggings?.let { equipment[LEGGINGS] = it.toPacketItem() }
                    currentData.boots?.let { equipment[BOOTS] = it.toPacketItem() }

                    EquipmentProperty(equipment)
                } to Int.MAX_VALUE)

        this.collectors = prioritizedPropertySuppliers.toCollectors()

        val startLocation = recording.firstNotNullWhere { it.location } ?: return
        val firstFrame = recording.firstFrame ?: return
        currentData = EntityFrame(
            location = startLocation,
            pose = firstFrame.pose,
            swing = firstFrame.swing,
            mainHand = if (firstFrame.mainHand.isNullOrEmpty()) null else firstFrame.mainHand,
            offHand = if (firstFrame.offHand.isNullOrEmpty()) null else firstFrame.offHand,
            helmet = if (firstFrame.helmet.isNullOrEmpty()) null else firstFrame.helmet,
            chestplate = if (firstFrame.chestplate.isNullOrEmpty()) null else firstFrame.chestplate,
            leggings = if (firstFrame.leggings.isNullOrEmpty()) null else firstFrame.leggings,
            boots = if (firstFrame.boots.isNullOrEmpty()) null else firstFrame.boots,
        )


        val collectedProperties = collectors.mapNotNull { it.collect(player) }

        this.entity?.spawn(startLocation.toProperty(player.world.toWorld()))
        this.entity?.consumeProperties(collectedProperties)
    }

    override suspend fun tickSegment(segment: EntityRecordedSegment, frame: Int) {
        super.tickSegment(segment, frame)
        val relativeFrame = frame - segment.startFrame
        val recording = recordings[segment.artifact.id] ?: return
        val frameData = recording.getFrame(relativeFrame) ?: return
        currentData = currentData.merge(frameData)

        val collectedProperties = collectors.mapNotNull { it.collect(player) }
        this.entity?.consumeProperties(collectedProperties)

        trackStepSound()
    }

    private var lastSoundLocation: PositionProperty? = null
    private fun trackStepSound() {
        val location = entity?.property<PositionProperty>() ?: return
        val lastLocation = lastSoundLocation
        if (lastLocation == null) {
            lastSoundLocation = location
            return
        }

        val distance = location.distanceSqrt(lastLocation) ?: 0.0
        if (distance < 1.7) return
        playStepSound()
        lastSoundLocation = location
    }

    private fun playStepSound() {
        val location = entity?.property<PositionProperty>() ?: return
        val sound = location.toBukkitLocation().block.blockData.soundGroup.stepSound
        player.playSound(location.toBukkitLocation(), sound, SoundCategory.NEUTRAL, 0.4f, 1.0f)
    }

    override suspend fun stopSegment(segment: EntityRecordedSegment) {
        super.stopSegment(segment)

        lastSoundLocation = null

        this.entity?.dispose()
        this.entity = null
    }
}

class EntityCinematicViewing(context: ContentContext, player: Player) : ContentMode(context, player) {
    override suspend fun setup(): Result<Unit> {
        val result = getAssetFromFieldValue(context.fieldValue)
        if (result.isFailure) {
            return failure(
                """
                    |You forgot to specify the EntityCinematicArtifact.
                    |It is required for recording the cinematic.
            """.trimMargin()
            )
        }

        exit()
        val cinematic = cinematic(context)
        recordingCinematic(context, 4, cinematic::frame, ::EntityCinematicRecording)

        return ok(Unit)
    }
}

@OptIn(ExperimentalContracts::class)
fun ItemStack?.isNullOrEmpty(): Boolean {
    contract {
        returns(false) implies (this@isNullOrEmpty != null)
    }
    return this == null || this.isEmpty
}

data class EntityFrame(
    val location: Coordinate? = null,
    val pose: EntityPose? = null,
    val swing: ArmSwing? = null,

    val mainHand: ItemStack? = null,
    val offHand: ItemStack? = null,
    val helmet: ItemStack? = null,
    val chestplate: ItemStack? = null,
    val leggings: ItemStack? = null,
    val boots: ItemStack? = null,
) {
    fun merge(next: EntityFrame): EntityFrame {
        return EntityFrame(
            location = next.location ?: location,
            pose = next.pose ?: pose,
            swing = next.swing,
            mainHand = next.mainHand ?: mainHand,
            offHand = next.offHand ?: offHand,
            helmet = next.helmet ?: helmet,
            chestplate = next.chestplate ?: chestplate,
            leggings = next.leggings ?: leggings,
            boots = next.boots ?: boots,
        )
    }
}

private class FakeProvider<P : EntityProperty>(private val klass: KClass<P>, private val supplier: () -> P?) :
    PropertySupplier<P> {
    override fun type(): KClass<P> = klass

    override fun build(player: Player): P {
        return supplier() ?: throw IllegalStateException("Could not build property $klass")
    }

    override fun canApply(player: Player): Boolean {
        return supplier() != null
    }
}

class EntityCinematicRecording(
    context: ContentContext,
    player: Player,
    initialFrame: Int,
    klass: KClass<EntityFrame>,
) : RecordingCinematicContentMode<EntityFrame>(context, player, initialFrame, klass) {
    private var swing: ArmSwing? = null

    @EventHandler
    fun onArmSwing(event: PlayerArmSwingEvent) {
        if (event.player.uniqueId != player.uniqueId) return
        addSwing(
            when (event.hand) {
                EquipmentSlot.OFF_HAND -> ArmSwing.LEFT
                EquipmentSlot.HAND -> ArmSwing.RIGHT
                else -> ArmSwing.BOTH
            }
        )
    }

    private fun addSwing(swing: ArmSwing) {
        if (this.swing != null && this.swing != swing) {
            this.swing = ArmSwing.BOTH
            return
        }

        this.swing = swing
    }

    override fun captureFrame(): EntityFrame {
        val inv = player.inventory
        val pose = if (player.isInsideVehicle) Pose.SITTING else player.pose
        val data = EntityFrame(
            location = player.location.toCoordinate(),
            pose = pose.toEntityPose(),
            swing = swing,
            mainHand = if (inv.itemInMainHand.isEmpty) null else inv.itemInMainHand,
            offHand = if (inv.itemInOffHand.isEmpty) null else inv.itemInOffHand,
            helmet = inv.helmet,
            chestplate = inv.chestplate,
            leggings = inv.leggings,
            boots = inv.boots,
        )
        this.swing = null
        return data
    }

    override fun applyState(value: EntityFrame) {
        value.location?.let { player.teleport(it.toBukkitLocation(player.world)) }
        value.pose?.let { player.pose = it.toBukkitPose() }
        value.swing?.let { swing ->
            when (swing) {
                ArmSwing.LEFT -> player.swingMainHand()
                ArmSwing.RIGHT -> player.swingOffHand()
                ArmSwing.BOTH -> {
                    player.swingMainHand()
                    player.swingOffHand()
                }
            }
        }

        value.mainHand?.let { player.inventory.setItemInMainHand(it) }
        value.offHand?.let { player.inventory.setItemInOffHand(it) }
        value.helmet?.let { player.inventory.helmet = it }
        value.chestplate?.let { player.inventory.chestplate = it }
        value.leggings?.let { player.inventory.leggings = it }
        value.boots?.let { player.inventory.boots = it }
    }
}