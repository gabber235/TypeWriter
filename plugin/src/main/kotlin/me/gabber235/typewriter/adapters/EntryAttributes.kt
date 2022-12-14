package me.gabber235.typewriter.adapters

@Target(AnnotationTarget.CLASS)
annotation class Tags(vararg val tags: String)


fun getTags(clazz: Class<*>): List<String> {
	return (clazz.getAnnotation(Tags::class.java)?.tags?.toList() ?: emptyList()) +
			(clazz.superclass?.let { getTags(it) } ?: emptyList()) +
			clazz.interfaces.flatMap { getTags(it) }
}

object Colors {
	const val RED = "#D32F2F"
	const val GREEN = "#4CAF50"
	const val BLUE = "#1E88E5"
	const val YELLOW = "#FBB612"
	const val PURPLE = "#5843e6"
	const val ORANGE = "#F57C00"
	const val PINK = "#eb4bb8"
	const val CYAN = "#0abab5"
}