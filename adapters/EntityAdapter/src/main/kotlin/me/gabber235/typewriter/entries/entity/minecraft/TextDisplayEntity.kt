package me.gabber235.typewriter.entries.entity.minecraft

import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.OnlyTags
import me.gabber235.typewriter.entries.data.minecraft.applyGenericEntityData
import me.gabber235.typewriter.entries.data.minecraft.display.applyDisplayEntityData
import me.gabber235.typewriter.entries.data.minecraft.display.text.*
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.emptyRef
import me.gabber235.typewriter.entry.entity.FakeEntity
import me.gabber235.typewriter.entry.entity.SimpleEntityDefinition
import me.gabber235.typewriter.entry.entity.SimpleEntityInstance
import me.gabber235.typewriter.entries.entity.WrapperFakeEntity
import me.gabber235.typewriter.entry.entries.*
import me.gabber235.typewriter.extensions.packetevents.meta
import me.gabber235.typewriter.utils.Sound
import me.gabber235.typewriter.utils.asMini
import me.tofaa.entitylib.meta.display.TextDisplayMeta
import org.bukkit.Location
import org.bukkit.entity.Player

@Entry("text_display_definition", "A text display entity", Colors.ORANGE, "material-symbols:text-ad-rounded")
@Tags("text_display_definition")
/**
 * The `TextDisplayDefinition` class is an entry that represents a text display entity.
 *
 * ## How could this be used?
 * This could be used to create an entity that displays text.
 */
class TextDisplayDefinition(
    override val id: String = "",
    override val name: String = "",
    override val displayName: String = "",
    override val sound: Sound = Sound.EMPTY,
    @OnlyTags("generic_entity_data", "display_data", "lines", "text_display_data")
    override val data: List<Ref<EntityData<*>>> = emptyList(),
) : SimpleEntityDefinition {
    override fun create(player: Player): FakeEntity = TextDisplayEntity(player)
}

@Entry(
    "text_display_instance",
    "An instance of a text display entity",
    Colors.YELLOW,
    "material-symbols:text-ad-rounded"
)
class TextDisplayInstance(
    override val id: String = "",
    override val name: String = "",
    override val definition: Ref<TextDisplayDefinition> = emptyRef(),
    override val spawnLocation: Location = Location(null, 0.0, 0.0, 0.0),
    @OnlyTags("generic_entity_data", "display_data", "lines", "text_display_data")
    override val data: List<Ref<EntityData<*>>> = emptyList(),
    override val activity: Ref<out SharedEntityActivityEntry> = emptyRef(),
) : SimpleEntityInstance

open class TextDisplayEntity(player: Player) : WrapperFakeEntity(
    EntityTypes.TEXT_DISPLAY,
    player,
) {
    override fun applyProperty(property: EntityProperty) {
        when (property) {
            is LinesProperty -> entity.meta<TextDisplayMeta>(preventNotification = true) {
                text = property.lines.asMini()

            }

            is BackgroundColorProperty -> applyBackgroundColorData(entity, property)
            is TextOpacityProperty -> applyTextOpacityData(entity, property)
            is LineWidthProperty -> applyLineWidthData(entity, property)
            is ShadowProperty -> applyShadowData(entity, property)
            is SeeThroughProperty -> applySeeThroughData(entity, property)
            else -> {}
        }
        if (applyGenericEntityData(entity, property)) return
        if (applyDisplayEntityData(entity, property)) return
    }
}