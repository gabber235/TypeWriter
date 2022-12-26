package me.gabber235.typewriter.adapters

import com.google.gson.reflect.TypeToken
import me.gabber235.typewriter.adapters.editors.material
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.extensionReceiverParameter
import kotlin.reflect.full.findAnnotation

@Target(AnnotationTarget.FUNCTION)
annotation class CustomEditor(val klass: KClass<*>)

class ObjectEditor(val klass: KClass<*>, val name: String) {

	/**
	 * If the custom editor is implemented inside the visual interface. We will use this to indicate it.
	 *
	 * Example:
	 * ```kotlin
	 * @CustomEditor(SomeClass::class)
	 * fun ObjectEditor.some() = reference()
	 * ```
	 * In this case, the editor name that will be used is `some`.
	 */
	fun reference() {}
}

private val customEditors by lazy {
	listOf(
		ObjectEditor::material
	)
		.mapNotNull(::objectEditorFromFunction)
		.associateBy { it.klass }
}

private fun objectEditorFromFunction(function: KFunction<*>): ObjectEditor? {
	val annotation = function.findAnnotation<CustomEditor>() ?: return null
	val extension = function.extensionReceiverParameter ?: return null
	if (extension.type.classifier != ObjectEditor::class) return null
	val klass = annotation.klass
	val name = function.name
	return ObjectEditor(klass, name).apply { function.call(this) }
}


fun computeCustomEditor(token: TypeToken<*>): ObjectEditor? {
	return customEditors[token.rawType.kotlin]
}