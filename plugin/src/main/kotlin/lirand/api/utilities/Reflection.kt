package lirand.api.utilities

import java.lang.reflect.Field
import java.lang.reflect.Method


val Class<*>.allFields: List<Field>
	get() = buildList {
		var currentClass: Class<*>? = this@allFields
		while (currentClass != null) {
			val declaredFields = currentClass.declaredFields
			addAll(declaredFields)
			currentClass = currentClass.superclass
		}
	}

val Class<*>.allMethods: List<Method>
	get() = buildList {
		var currentClass: Class<*>? = this@allMethods
		while (currentClass != null) {
			val declaredMethods = currentClass.declaredMethods
			addAll(declaredMethods)
			currentClass = currentClass.superclass
		}
	}

val Class<*>.superclasses: List<Class<*>>
	get() = buildList {
		var currentClass: Class<*>? = this@superclasses
		while (true) {
			val superClass = currentClass?.superclass ?: break
			add(superClass)
			currentClass = superClass
		}
	}


val Method.isOverridden: Boolean
	get() {
		val declaringClass: Class<*> = declaringClass
		if (declaringClass == Any::class.java) {
			return false
		}
		else try {
			declaringClass.superclass.getMethod(name, *parameterTypes)
			return true
		} catch (_: NoSuchMethodException) {
			declaringClass.interfaces.forEach {
				try {
					it.getMethod(name, *parameterTypes)
					return true
				} catch (_: NoSuchMethodException) {}
			}
			return false
		}
	}