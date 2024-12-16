package com.typewritermc.processors

import com.google.devtools.ksp.*
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.*
import com.google.gson.annotations.SerializedName
import kotlin.reflect.KClass

@OptIn(KspExperimental::class)
val KSDeclaration.serializedName: String
    get() {
        return getAnnotationsByType(SerializedName::class).firstOrNull()?.value ?: simpleName.asString()
    }

val KSType.fullName: String
    get() = declaration.fullName

val KSDeclaration.fullName: String
    get() = qualifiedName?.asString() ?: simpleName.asString()

val Location.format: String
    get() {
        return when (this) {
            is FileLocation -> "$filePath:$lineNumber"
            is NonExistLocation -> "NonExist"
        }
    }

@OptIn(KspExperimental::class)
val KSFile.className: String
    get() {
        val className =
            getAnnotationsByType(JvmName::class).firstOrNull()?.name ?: "${fileName.substringBeforeLast(".")}Kt"
        return "${packageName.asString()}.$className"
    }

infix fun KSDeclaration.whenClassNameIs(className: String): Boolean {
    return qualifiedName?.asString() == className
}

infix fun KSType.whenClassNameIs(className: String): Boolean = declaration whenClassNameIs className

infix fun KSDeclaration.whenClassIs(kclass: KClass<*>): Boolean {
    return qualifiedName?.asString() == kclass.qualifiedName
}

infix fun KSType.whenClassIs(kclass: KClass<*>): Boolean = declaration whenClassIs kclass

infix fun KSDeclaration.isOrExtends(className: String): Boolean {
    if (this whenClassNameIs className) return true
    if (this !is KSClassDeclaration) return false
    return getAllSuperTypes().any { it.whenClassNameIs(className) }
}

infix fun KSType.isOrExtends(className: String): Boolean = declaration isOrExtends className

context(Resolver)
@OptIn(KspExperimental::class)
inline fun <reified T : Annotation> KSAnnotated.annotationClassValue(f: T.() -> KClass<*>) =
    getAnnotationsByType(T::class).first().annotationClassValue(f)

context(Resolver)
@OptIn(KspExperimental::class)
inline fun <reified T : Annotation> T.annotationClassValue(f: T.() -> KClass<*>): KSType = try {
    val klass = f()
    val declaration = getKotlinClassByName(klass.qualifiedName!!) ?: throw ClassNotFoundException(klass.qualifiedName!!)
    declaration.asStarProjectedType()
} catch (e: KSTypeNotPresentException) {
    e.ksType
}

fun List<KSValueParameter>.hasParameter(className: String): Boolean {
    return any { it.type.resolve().whenClassNameIs(className) }
}

fun KSClassDeclaration.isImplementingInterface(interfaceName: String): Boolean {
    return superTypes.any { it.resolve().whenClassNameIs(interfaceName) }
}

class EntryNotFoundException(what: String, who: String, entry: String) :
    Exception("$what $who target entry $entry does not have the Entry annotation")

class IllegalClassTypeException(className: String) :
    Exception("Class $className does not have a qualified name. Classes must be full classes.")
