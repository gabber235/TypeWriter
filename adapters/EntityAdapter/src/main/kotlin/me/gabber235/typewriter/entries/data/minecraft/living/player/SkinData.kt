package me.gabber235.typewriter.entries.data.minecraft.living.player

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.entry.entity.SkinProperty
import me.gabber235.typewriter.entry.entries.EntityData
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("skin_data", "Skin data for players", Colors.RED, "ant-design:skin-filled")
@Tags("skin_data", "player_data")
class SkinData(
    override val id: String = "",
    override val name: String = "",
    val skin: SkinProperty = SkinProperty(),
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : EntityData<SkinProperty> {
    override val type: KClass<SkinProperty> = SkinProperty::class

    override fun build(player: Player): SkinProperty = skin
}

