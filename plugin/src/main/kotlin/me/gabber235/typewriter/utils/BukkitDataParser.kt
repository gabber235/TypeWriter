package me.gabber235.typewriter.utils

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.configuration.serialization.ConfigurationSerialization
import java.lang.reflect.Type


fun createBukkitDataParser(): Gson = GsonBuilder()
    .registerTypeHierarchyAdapter(ConfigurationSerializable::class.java, ConfigurationSerializableAdapter())
    .create()


class ConfigurationSerializableAdapter : JsonSerializer<ConfigurationSerializable>,
    JsonDeserializer<ConfigurationSerializable> {
    private val objectStringMapType: Type = object : TypeToken<Map<String?, Any?>?>() {}.type

    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext
    ): ConfigurationSerializable? {
        val map: MutableMap<String, Any?> = LinkedHashMap()
        for ((name, value) in json.asJsonObject.entrySet()) {
            if (value.isJsonObject && value.asJsonObject.has(ConfigurationSerialization.SERIALIZED_TYPE_KEY)) {
                map[name] = deserialize(value, value.javaClass, context)
            } else {
                map[name] = context.deserialize(value, Any::class.java)
            }
        }
        return ConfigurationSerialization.deserializeObject(map)
    }

    override fun serialize(
        src: ConfigurationSerializable,
        typeOfSrc: Type?,
        context: JsonSerializationContext
    ): JsonElement {
        val map: MutableMap<String, Any> = LinkedHashMap()
        map[ConfigurationSerialization.SERIALIZED_TYPE_KEY] = ConfigurationSerialization.getAlias(src.javaClass)
        map.putAll(src.serialize())
        return context.serialize(map, objectStringMapType)
    }
}