package com.typewritermc.engine.paper.entry.entity

import com.typewritermc.core.entries.Ref
import com.typewritermc.engine.paper.entry.entries.EntityInstanceEntry
import java.util.concurrent.ConcurrentHashMap

class EntityStorage {
    val location = ConcurrentHashMap<Ref<out EntityInstanceEntry>, PositionProperty>()

}