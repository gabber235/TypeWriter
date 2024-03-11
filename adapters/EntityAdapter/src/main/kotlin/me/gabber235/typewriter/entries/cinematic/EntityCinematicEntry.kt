package me.gabber235.typewriter.entries.cinematic

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.ContentEditor
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.Segments
import me.gabber235.typewriter.content.ContentContext
import me.gabber235.typewriter.content.ContentMode
import me.gabber235.typewriter.content.components.bossBar
import me.gabber235.typewriter.content.components.cinematic
import me.gabber235.typewriter.content.components.exit
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.cinematic.SimpleCinematicAction
import me.gabber235.typewriter.entry.emptyRef
import me.gabber235.typewriter.entry.entity.FakeEntity
import me.gabber235.typewriter.entry.entries.*
import net.kyori.adventure.bossbar.BossBar
import org.bukkit.entity.Player

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
    override val segments: List<EntityRecordedSegment>
        get() = entry.segments

    private var entity: FakeEntity? = null

    override suspend fun startSegment(segment: EntityRecordedSegment) {
        super.startSegment(segment)
        this.entity = entry.definition.get()?.create(player)
        // TODO: Spawn the entity
    }

    override suspend fun tickSegment(segment: EntityRecordedSegment, frame: Int) {
        super.tickSegment(segment, frame)
        // TODO: Handle the entity's movement
    }

    override suspend fun stopSegment(segment: EntityRecordedSegment) {
        super.stopSegment(segment)
        this.entity?.dispose()
        this.entity = null
    }
}

class EntityCinematicViewing(context: ContentContext, player: Player) : ContentMode(context, player) {
    override fun setup() {
        exit()
        cinematic(context)
    }
}