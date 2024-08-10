package me.gabber235.typewriter.utils

class Color(
    val color: Int,
) {
    companion object {
        fun fromHex(hex: String): Color {
            val color = hex.removePrefix("#").toInt(16)
            return Color(color)
        }

        val BLACK_BACKGROUND = Color(0x40000000)
    }

    val alpha: Int get() = (color shr 24) and 0xFF
    val red: Int get() = (color shr 16) and 0xFF
    val green: Int get() = (color shr 8) and 0xFF
    val blue: Int get() = color and 0xFF

    fun toBukkitColor(): org.bukkit.Color {
        return org.bukkit.Color.fromARGB(alpha, red, green, blue)
    }
}