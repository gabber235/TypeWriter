package me.gabber235.typewriter.entries.cinematic

import com.github.retrooper.packetevents.protocol.entity.pose.EntityPose
import com.github.retrooper.packetevents.protocol.player.EquipmentSlot.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.papermc.paper.event.player.PlayerArmSwingEvent
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.ContentEditor
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.Segments
import me.gabber235.typewriter.content.ContentContext
import me.gabber235.typewriter.content.ContentMode
import me.gabber235.typewriter.content.components.cachedInventory
import me.gabber235.typewriter.content.components.cinematic
import me.gabber235.typewriter.content.components.exit
import me.gabber235.typewriter.content.modes.*
import me.gabber235.typewriter.entries.data.minecraft.ArmSwingProperty
import me.gabber235.typewriter.entries.data.minecraft.PoseProperty
import me.gabber235.typewriter.entries.data.minecraft.living.toProperty
import me.gabber235.typewriter.entries.data.minecraft.toBukkitPose
import me.gabber235.typewriter.entries.data.minecraft.toEntityPose
import me.gabber235.typewriter.entry.AssetManager
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.cinematic.SimpleCinematicAction
import me.gabber235.typewriter.entry.emptyRef
import me.gabber235.typewriter.entry.entity.FakeEntity
import me.gabber235.typewriter.entry.entity.withPriority
import me.gabber235.typewriter.entry.entity.toCollectors
import me.gabber235.typewriter.entry.entity.toProperty
import me.gabber235.typewriter.entry.entries.*
import me.gabber235.typewriter.extensions.packetevents.ArmSwing
import me.gabber235.typewriter.utils.ok
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.entity.Pose
import org.bukkit.event.EventHandler
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent
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

        this.collectors = definition.data.withPriority().toCollectors()

        val startLocation = recording.firstNotNullWhere { it.location } ?: return
        val firstFrame = recording.firstFrame ?: return

        val collectedProperties = collectors.mapNotNull { it.collect(player) }

        this.entity?.consumeProperties(collectedProperties + firstFrame.toProperties())
        this.entity?.spawn(startLocation.toProperty())
    }

    override suspend fun tickSegment(segment: EntityRecordedSegment, frame: Int) {
        super.tickSegment(segment, frame)
        val relativeFrame = frame - segment.startFrame
        val recording = recordings[segment.artifact.id] ?: return
        val frameData = recording.getFrame(relativeFrame) ?: return
        val collectedProperties = collectors.mapNotNull { it.collect(player) }
        this.entity?.consumeProperties(collectedProperties + frameData.toProperties())
    }

    override suspend fun stopSegment(segment: EntityRecordedSegment) {
        super.stopSegment(segment)
        this.entity?.dispose()
        this.entity = null
    }
}

class EntityCinematicViewing(context: ContentContext, player: Player) : ContentMode(context, player) {
    override suspend fun setup(): Result<Unit> {
        exit()
        val cinematic = cinematic(context)
        recordingCinematic(context, 4, cinematic::frame, ::EntityCinematicRecording)

        return ok(Unit)
    }
}

data class EntityFrame(
    val location: Location?,
    val pose: EntityPose?,
    val swing: ArmSwing?,

    val mainHand: ItemStack?,
    val offHand: ItemStack?,
    val helmet: ItemStack?,
    val chestplate: ItemStack?,
    val leggings: ItemStack?,
    val boots: ItemStack?,
) {
    fun toProperties(): List<EntityProperty> {
        val properties = mutableListOf<EntityProperty>()

        location?.let { properties.add(it.toProperty()) }
        pose?.let { properties.add(PoseProperty(it)) }
        swing?.let { properties.add(ArmSwingProperty(it)) }
        mainHand?.let { properties.add(it.toProperty(MAIN_HAND)) }
        offHand?.let { properties.add(it.toProperty(OFF_HAND)) }
        helmet?.let { properties.add(it.toProperty(HELMET)) }
        chestplate?.let { properties.add(it.toProperty(CHEST_PLATE)) }
        leggings?.let { properties.add(it.toProperty(LEGGINGS)) }
        boots?.let { properties.add(it.toProperty(BOOTS)) }

        return properties
    }
}

class EntityCinematicRecording(
    context: ContentContext,
    player: Player,
    initialFrame: Int,
    klass: KClass<EntityFrame>,
) : RecordingCinematicContentMode<EntityFrame>(context, player, initialFrame, klass) {
    private var swing: ArmSwing? = null

    override suspend fun setup(): Result<Unit> {
        val result =super.setup()
        if (result.isFailure) return result
        cachedInventory()
        return ok(Unit)
    }

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
            location = player.location,
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
        value.location?.let { player.teleport(it) }
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