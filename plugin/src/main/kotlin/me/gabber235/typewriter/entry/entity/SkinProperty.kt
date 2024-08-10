package me.gabber235.typewriter.entry.entity

import me.gabber235.typewriter.entry.entries.EntityProperty
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