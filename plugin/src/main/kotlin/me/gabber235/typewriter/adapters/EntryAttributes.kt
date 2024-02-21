package me.gabber235.typewriter.adapters

@Target(AnnotationTarget.CLASS)
annotation class Tags(vararg val tags: String)


fun getTags(clazz: Class<*>): List<String> {
    val clazzTags = clazz.getAnnotation(Tags::class.java)?.tags?.toList() ?: emptyList()
    val superClassTags = clazz.superclass?.let { getTags(it) } ?: emptyList()
    val interfaceTags = clazz.interfaces.flatMap { getTags(it) }

    val generatedTags = mutableListOf<String>()
    if (clazz.isAnnotationPresent(Deprecated::class.java)) {
        generatedTags.add("deprecated")
    }

    return (clazzTags + superClassTags + interfaceTags + generatedTags).distinct()
}

object Colors {
    const val RED = "#D32F2F"
    const val GREEN = "#4CAF50"
    const val MEDIUM_SEA_GREEN = "#3CB371"
    const val BLUE = "#1E88E5"
    const val MYRTLE_GREEN = "#297373"
    const val YELLOW = "#FBB612"
    const val PURPLE = "#5843e6"
    const val MEDIUM_PURPLE = "#9370DB"
    const val BLUE_VIOLET = "#8A2BE2"
    const val ORANGE = "#F57C00"
    const val DARK_ORANGE = "#FF8C00"
    const val ORANGE_RED = "#FF4500"
    const val PINK = "#eb4bb8"
    const val CYAN = "#0abab5"
}