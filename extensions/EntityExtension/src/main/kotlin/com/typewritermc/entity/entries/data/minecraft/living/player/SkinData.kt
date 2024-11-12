package com.typewritermc.entity.entries.data.minecraft.living.player

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.engine.paper.entry.entity.SkinProperty
import com.typewritermc.engine.paper.entry.entries.ConstVar
import com.typewritermc.engine.paper.entry.entries.EntityData
import com.typewritermc.engine.paper.entry.entries.Var
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("skin_data", "Skin data for players", Colors.RED, "ant-design:skin-filled")
@Tags("skin_data", "player_data")
class SkinData(
    override val id: String = "",
    override val name: String = "",
    val skin: Var<SkinProperty> = ConstVar(SkinProperty()),
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : EntityData<SkinProperty> {
    override fun type(): KClass<SkinProperty> = SkinProperty::class

    override fun build(player: Player): SkinProperty = skin.get(player)
}

