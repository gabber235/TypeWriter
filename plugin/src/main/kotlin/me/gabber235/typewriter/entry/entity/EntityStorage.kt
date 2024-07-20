package me.gabber235.typewriter.entry.entity

import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.entries.EntityInstanceEntry
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class EntityStorage {
    val location = ConcurrentHashMap<Ref<out EntityInstanceEntry>, LocationProperty>()

}