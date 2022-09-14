package me.gabber235.typewriter.utils

import java.io.File

operator fun File.get(name: String): File = File(this, name)

