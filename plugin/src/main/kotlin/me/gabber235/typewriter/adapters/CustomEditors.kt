package me.gabber235.typewriter.adapters

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import me.gabber235.typewriter.adapters.editors.*
import me.gabber235.typewriter.adapters.modifiers.StaticModifierComputer
import me.gabber235.typewriter.utils.CronExpression
import me.gabber235.typewriter.utils.Item
import me.gabber235.typewriter.utils.SoundId
import me.gabber235.typewriter.utils.SoundSource
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.potion.PotionEffectType
import java.lang.reflect.Type
import java.time.Duration
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.extensionReceiverParameter
import kotlin.reflect.full.findAnnotation

typealias GsonDeserializer<T> = (JsonElement, Type, JsonDeserializationContext) -> T
typealias GsonSerializer<T> = (T, Type, JsonSerializationContext) -> JsonElement

typealias FieInfoGenerator = (TypeToken<*>) -> FieldInfo

typealias DefaultGenerator = (TypeToken<*>) -> JsonElement
typealias FieldModifierGenerator = (FieldInfo) -> FieldModifier?

@Target(AnnotationTarget.FUNCTION)
annotation class CustomEditor(val klass: KClass<*>)


class ObjectEditor<T : Any>(val klass: KClass<T>, val name: String) {

    /**
     * If the custom editor is implemented inside the visual interface. We will use this to indicate it.
     *
     * Example:
     * ```kotlin
     * @CustomEditor(SomeClass::class)
     * fun ObjectEditor.some() = reference {
     *    // ...
     * }
     * ```
     * In this case, the editor name that will be used is `some`.
     */
    fun reference(editor: ObjectEditor<T>.() -> Unit) = editor()

    /**
     * The deserializer used to convert a JSON string into an instance of the class represented by this editor.
     * NOTE: This field is used internally and should not be accessed directly.
     */
    internal var deserializer: JsonDeserializer<T>? = null
        private set

    /**
     * If the default deserialization is not enough, you can use this method to provide a custom deserializer for this editor.
     *
     * Example:
     * ```kotlin
     * @CustomEditor(SomeClass::class)
     * fun ObjectEditor.some() = reference {
     *   deserializer = JsonDeserializer { element, type, context ->
     *      // ...
     *   }
     *   // ...
     * }
     * ```
     *
     * @param deserializer The deserializer to use.
     */
    fun jsonDeserialize(deserializer: GsonDeserializer<T>) {
        this.deserializer =
            JsonDeserializer<T> { json, typeOfT, context -> deserializer(json, typeOfT, context) }
    }

    /**
     * The serializer used to convert an instance of the class represented by this editor into a JSON string.
     * NOTE: This field is used internally and should not be accessed directly.
     */
    internal var serializer: JsonSerializer<T>? = null
        private set

    /**
     * If the default serialization is not enough, you can use this method to provide a custom serializer for this editor.
     * Example:
     * ```kotlin
     * @CustomEditor(SomeClass::class)
     * fun ObjectEditor.some() = reference {
     *  serializer = JsonSerializer { element, type, context ->
     *    // ...
     *  }
     *  // ...
     * }
     */
    fun jsonSerialize(serializer: GsonSerializer<T>) {
        this.serializer =
            JsonSerializer<T> { src, typeOfSrc, context -> serializer(src, typeOfSrc, context) }
    }


    /**
     * Save the field info generator for this editor.
     * NOTE: This field is used internally and should not be accessed directly.
     */
    private var fieldInfoGenerator: FieInfoGenerator? = null

    /**
     * If a custom editor needs to have some information about the specific field it is editing, it can use this method to
     * provide a function that will generate the information.
     *
     * Example:
     * ```kotlin
     * @CustomEditor(SomeClass::class)
     * fun ObjectEditor.some() = reference {
     *  fieldInfo { klass ->
     *      // ...
     *  }
     *  // ...
     * }
     * ```
     *
     * @param generator The function that will generate the information.
     */
    fun fieldInfo(generator: FieInfoGenerator) {
        this.fieldInfoGenerator = generator
    }


    /**
     * Generates the field info for this editor.
     * NOTE: This method is used internally and should not be accessed directly.
     * @param token The type token of the class represented by this editor.
     */
    internal fun generateFieldInfo(token: TypeToken<*>): FieldInfo? {
        return fieldInfoGenerator?.invoke(token)
    }

    /**
     * Save the default generator for this editor.
     * NOTE: This field is used internally and should not be accessed directly.
     */
    private var defaultGenerator: DefaultGenerator? = null

    /**
     * Custom editors need a default value to be able to create new instances of the class they are editing.
     *
     * Example:
     * ```kotlin
     * @CustomEditor(SomeClass::class)
     * fun ObjectEditor.some() = reference {
     *  default { token ->
     *      // ...
     *  }
     *  // ...
     * }
     * ```
     *
     * @param generator The function that will generate the information.
     */
    fun default(generator: DefaultGenerator) {
        this.defaultGenerator = generator
    }

    /**
     * Generates the default value for this editor.
     * NOTE: This method is used internally and should not be accessed directly.
     * @param token The type token of the class represented by this editor.
     */
    internal fun generateDefault(token: TypeToken<*>): JsonElement {
        return defaultGenerator?.invoke(token) ?: JsonNull.INSTANCE
    }

    /**
     * Save the modifiers for this editor.
     * NOTE: This field is used internally and should not be accessed directly.
     */
    private val modifiers = mutableListOf<FieldModifierGenerator>()

    /**
     * Adds a modifier to this editor.
     */
    operator fun FieldModifier?.unaryPlus() {
        if (this == null) return
        modifiers.add { this }
    }

    infix fun <A : Annotation> StaticModifierComputer<A>.with(annotation: A) {
        modifiers.add { this.computeModifier(annotation, it) }
    }

    /**
     * Generates the modifiers for this editor.
     * NOTE: This method is used internally and should not be accessed directly.
     */
    internal fun generateModifiers(info: FieldInfo): List<FieldModifier> {
        return modifiers.mapNotNull { it(info) }
    }
}

internal val customEditors by lazy {
    listOf(
        ObjectEditor<Material>::material,
        ObjectEditor<Optional<*>>::optional,
        ObjectEditor<Location>::location,
        ObjectEditor<Duration>::duration,
        ObjectEditor<CronExpression>::cron,
        ObjectEditor<PotionEffectType>::potionEffectType,
        ObjectEditor<Item>::item,
        ObjectEditor<SoundId>::soundId,
        ObjectEditor<SoundSource>::soundSource,
    )
        .mapNotNull(::objectEditorFromFunction)
        .associateBy { it.klass }
}

private fun objectEditorFromFunction(function: KFunction<*>): ObjectEditor<*>? {
    val annotation = function.findAnnotation<CustomEditor>() ?: return null
    val extension = function.extensionReceiverParameter ?: return null
    if (extension.type.classifier != ObjectEditor::class) return null
    val klass = annotation.klass
    val name = function.name
    return ObjectEditor(klass, name).apply { function.call(this) }
}


fun computeCustomEditor(token: TypeToken<*>): ObjectEditor<*>? {
    return customEditors[token.rawType.kotlin]
}