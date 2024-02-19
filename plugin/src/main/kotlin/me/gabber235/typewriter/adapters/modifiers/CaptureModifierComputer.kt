package me.gabber235.typewriter.adapters.modifiers

import me.gabber235.typewriter.adapters.FieldInfo
import me.gabber235.typewriter.adapters.FieldModifier
import me.gabber235.typewriter.capture.Capturer
import me.gabber235.typewriter.capture.CapturerCreator
import me.gabber235.typewriter.utils.failure
import me.gabber235.typewriter.utils.ok
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.jvmName

@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY)
annotation class Capture(val capturer: KClass<out Capturer<*>>)

object CaptureModifierComputer : StaticModifierComputer<Capture> {
    override val annotationClass: Class<Capture> = Capture::class.java

    override fun computeModifier(annotation: Capture, info: FieldInfo): Result<FieldModifier?> {
        val capturer = annotation.capturer
        val name = capturer.qualifiedName
            ?: return failure("Capturer ${capturer.jvmName} does not have a qualified name! It must be a non-local non-anonymous class.")

        if (capturer.companionObject == null) {
            return failure("Capturer ${capturer.jvmName} needs to have a companion object which extends CapturerCreator<${capturer.simpleName}>! It has no companion object.")
        }

        if (capturer.companionObject?.isSubclassOf(CapturerCreator::class) != true) {
            return failure("Capturer ${capturer.jvmName} needs to have a companion object which extends CapturerCreator<${capturer.simpleName}>! Forgot to extend CapturerCreator?")
        }

        return ok(FieldModifier.DynamicModifier("capture", name))
    }
}