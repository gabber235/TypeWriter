package me.gabber235.typewriter.adapters.editors

import com.google.gson.JsonPrimitive
import me.gabber235.typewriter.adapters.CustomEditor
import me.gabber235.typewriter.adapters.ObjectEditor
import org.bukkit.potion.PotionEffectType

@CustomEditor(PotionEffectType::class)
fun ObjectEditor<PotionEffectType>.potionEffectType() = reference {
    default {
        JsonPrimitive("speed")
    }

    jsonDeserialize { element, _, _ ->
        PotionEffectType.getByName(element.asString) ?: PotionEffectType.SPEED
    }

    jsonSerialize { src, _, _ ->
        JsonPrimitive(src.name)
    }
}