package me.gabber235.typewriter.adapters.editors

import me.gabber235.typewriter.adapters.CustomEditor
import me.gabber235.typewriter.adapters.ObjectEditor
import org.bukkit.Material

@CustomEditor(Material::class)
fun ObjectEditor.material() = reference()