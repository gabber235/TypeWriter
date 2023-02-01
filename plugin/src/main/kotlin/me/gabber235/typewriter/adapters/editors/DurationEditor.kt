package me.gabber235.typewriter.adapters.editors

import com.google.gson.JsonPrimitive
import me.gabber235.typewriter.adapters.CustomEditor
import me.gabber235.typewriter.adapters.ObjectEditor
import java.time.Duration

@CustomEditor(Duration::class)
fun ObjectEditor<Duration>.duration() = reference {
	default {
		JsonPrimitive(0)
	}

	jsonDeserialize { element, _, _ ->
		Duration.ofMillis(element.asLong)
	}

	jsonSerialize { src, _, _ ->
		JsonPrimitive(src.toMillis())
	}
}