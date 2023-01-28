package me.gabber235.typewriter.adapters.editors

import com.google.gson.JsonPrimitive
import me.gabber235.typewriter.adapters.CustomEditor
import me.gabber235.typewriter.adapters.ObjectEditor
import me.gabber235.typewriter.utils.CronExpression

@CustomEditor(CronExpression::class)
fun ObjectEditor<CronExpression>.cron() = reference {
	default {
		JsonPrimitive(CronExpression.default().expression)
	}

	jsonDeserialize { element, _, _ ->
		CronExpression.createDynamic(element.asString)
	}

	jsonSerialize { value, _, _ ->
		JsonPrimitive(value.expression)
	}
}