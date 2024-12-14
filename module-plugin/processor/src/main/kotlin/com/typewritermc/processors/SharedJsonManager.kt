package com.typewritermc.processors

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

class SharedJsonManager {
    private var json: MutableMap<String, JsonElement> = mutableMapOf()

    @Synchronized
    fun updateSection(sectionName: String, content: JsonElement) {
        json[sectionName] = content
    }

    @Synchronized
    fun updateSection(sectionName: String, content: String) {
        json[sectionName] = JsonPrimitive(content)
    }

    override fun toString(): String {
        return Json.encodeToString(JsonObject.serializer(), JsonObject(json))
    }
}

