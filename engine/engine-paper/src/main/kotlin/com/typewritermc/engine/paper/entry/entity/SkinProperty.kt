package com.typewritermc.engine.paper.entry.entity

import com.typewritermc.engine.paper.entry.entries.EntityProperty
import org.bukkit.entity.Player

data class SkinProperty(
    val texture: String = "",
    val signature: String = "",
) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<SkinProperty>(SkinProperty::class)
}

val Player.skin: SkinProperty
    get() {
        val profile = playerProfile
        if (!profile.hasTextures()) return SkinProperty()
        val textures = profile.properties.firstOrNull { it.name == "textures" } ?: return SkinProperty()

        return SkinProperty(textures.value, textures.signature ?: "")
    }