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