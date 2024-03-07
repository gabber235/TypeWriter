package me.gabber235.typewriter.entries.entity.custom

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.OnlyTags
import me.gabber235.typewriter.entries.data.minecraft.display.BillboardConstraintProperty
import me.gabber235.typewriter.entries.data.minecraft.display.TranslationProperty
import me.gabber235.typewriter.entries.entity.minecraft.PlayerEntity
import me.gabber235.typewriter.entries.entity.minecraft.TextDisplayEntity
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.emptyRef
import me.gabber235.typewriter.entry.entity.FakeEntity
import me.gabber235.typewriter.entry.entity.LocationProperty
import me.gabber235.typewriter.entry.entity.SimpleEntityDefinition
import me.gabber235.typewriter.entry.entity.SimpleEntityInstance
import me.gabber235.typewriter.entry.entity.SkinProperty
import me.gabber235.typewriter.entry.entries.EntityActivityEntry
import me.gabber235.typewriter.entry.entries.EntityData
import me.gabber235.typewriter.entry.entries.EntityDefinitionEntry
import me.gabber235.typewriter.entry.entries.EntityProperty
import me.gabber235.typewriter.entry.entries.LinesProperty
import me.gabber235.typewriter.entry.ref
import me.gabber235.typewriter.extensions.placeholderapi.parsePlaceholders
import me.gabber235.typewriter.snippets.snippet
import me.gabber235.typewriter.utils.Sound
import me.gabber235.typewriter.utils.Vector
import me.gabber235.typewriter.utils.asMini
import me.gabber235.typewriter.utils.asMiniWithResolvers
import me.tofaa.entitylib.meta.display.AbstractDisplayMeta
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.Location
import org.bukkit.entity.Player


val npcNamePlate by snippet(
    "entity.npc.nameplate", """
    <other>
    <reset><display_name>
""".trimIndent()
)
val trackedInteractIndicator by snippet("objective.entity.indicator.tracked", "<gold><b>!")
val interactIndicator by snippet("objective.entity.indicator.normal", "<blue><b>?")

@Entry("npc_definition", "A simplified premade npc", Colors.ORANGE, "material-symbols:account-box")
@Tags("npc_definition")
/**
 * The `NpcDefinition` class is an entry that represents a simplified premade npc.
 *
 * It has an Icon above the head when an `NpcInteractObjective` is active for a player.
 * And when the objective is being tracked, the npc will have a different icon.
 *
 * The npc also has its display name above the head.
 *
 * ## How could this be used?
 * This could be used to create a simple npc has most of the properties already set.
 */
class NpcDefinition(
    override val id: String = "",
    override val name: String = "",
    override val displayName: String = "",
    override val sound: Sound = Sound.EMPTY,
    @Help("The skin of the npc.")
    val skin: SkinProperty = SkinProperty(),
    @OnlyTags("generic_entity_data", "living_entity_data", "lines", "player_data")
    override val data: List<Ref<EntityData<*>>> = emptyList(),
) : SimpleEntityDefinition {

    override fun create(player: Player): FakeEntity = NpcEntity(player, displayName, skin, ref())
}

@Entry("npc_instance", "An instance of a simplified premade npc", Colors.YELLOW, "material-symbols:account-box")
/**
 * The `NpcInstance` class is an entry that represents an instance of a simplified premade npc.
 */
class NpcInstance(
    override val id: String = "",
    override val name: String = "",
    override val definition: Ref<NpcDefinition> = emptyRef(),
    override val spawnLocation: Location = Location(null, 0.0, 0.0, 0.0),
    @OnlyTags("generic_entity_data", "living_entity_data", "lines", "player_data")
    override val data: List<Ref<EntityData<*>>>,
    override val activities: List<Ref<out EntityActivityEntry>> = emptyList(),
) : SimpleEntityInstance

class NpcEntity(
    player: Player,
    private val displayName: String,
    private val skin: SkinProperty,
    definition: Ref<out EntityDefinitionEntry>,
) : FakeEntity(player) {
    private val playerEntity = PlayerEntity(player)
    private val hologram = TextDisplayEntity(player)
    private val indicatorEntity = InteractionIndicatorEntity(player, definition)

    init {
        consumeProperties(skin)
        val hologramText = hologram()
        hologram.consumeProperties(
            LinesProperty(hologramText),
            TranslationProperty(Vector(y = 0.2)),
            BillboardConstraintProperty(AbstractDisplayMeta.BillboardConstraints.VERTICAL)
        )
        indicatorEntity.consumeProperties(
            TranslationProperty(calculateIndicatorOffset(hologramText)),
            BillboardConstraintProperty(AbstractDisplayMeta.BillboardConstraints.VERTICAL)
        )
    }

    override val entityId: Int
        get() = playerEntity.entityId

    override fun applyProperties(properties: List<EntityProperty>) {
        if (properties.any { it is SkinProperty }) {
            playerEntity.consumeProperties(properties)
            return
        }
        playerEntity.consumeProperties(properties + skin)
    }

    override fun tick() {
        playerEntity.tick()
        val hologramText = hologram()
        hologram.consumeProperties(LinesProperty(hologramText))
        hologram.tick()
        indicatorEntity.consumeProperties(TranslationProperty(calculateIndicatorOffset(hologramText)))
        indicatorEntity.tick()
    }

    private fun hologram(): String {
        val other = property(LinesProperty::class)?.lines ?: ""
        val displayName = this.displayName

        return npcNamePlate.parsePlaceholders(player).asMiniWithResolvers(
            Placeholder.parsed("other", other),
            Placeholder.parsed("display_name", displayName.parsePlaceholders(player)),
        ).asMini().trim()
    }

    private fun calculateIndicatorOffset(hologramText: String): Vector {
        val lines = hologramText.count { it == '\n' } + 1
        val height = lines * 0.3 + 0.2
        return Vector(y = height)
    }

    override fun spawn(location: LocationProperty) {
        playerEntity.spawn(location)
        hologram.spawn(location)
        indicatorEntity.spawn(location)
        playerEntity.addPassenger(hologram)
        playerEntity.addPassenger(indicatorEntity)
    }

    override fun addPassenger(entity: FakeEntity) {
        playerEntity.addPassenger(entity)
    }

    override fun removePassenger(entity: FakeEntity) {
        playerEntity.removePassenger(entity)
    }

    override fun contains(entityId: Int): Boolean {
        if (playerEntity.contains(entityId)) return true
        if (hologram.contains(entityId)) return true
        if (indicatorEntity.contains(entityId)) return true
        return false
    }

    override fun dispose() {
        playerEntity.dispose()
        hologram.dispose()
        indicatorEntity.dispose()
    }
}