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
}