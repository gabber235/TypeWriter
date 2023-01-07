package me.gabber235.typewriter.adapters.editors

import com.google.gson.JsonPrimitive
import me.gabber235.typewriter.adapters.CustomEditor
import me.gabber235.typewriter.adapters.ObjectEditor
import org.bukkit.Material

@CustomEditor(Material::class)
fun ObjectEditor<Material>.material() = reference {
	default {
		JsonPrimitive(Material.AIR.name)
	}
}