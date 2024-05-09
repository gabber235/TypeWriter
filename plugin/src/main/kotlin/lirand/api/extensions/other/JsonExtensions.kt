package lirand.api.extensions.other

import com.google.gson.JsonElement
import com.google.gson.JsonObject

operator fun JsonObject.set(property: String, value: JsonElement) =
	add(property, value)