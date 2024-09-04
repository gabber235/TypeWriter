package com.typewritermc.engine.paper.entry.entity

import com.typewritermc.engine.paper.entry.entries.EntityProperty

class ActivityManager<Context : ActivityContext>(
    private val activity: EntityActivity<in Context>,
) {
    val position: PositionProperty
        get() = activity.currentPosition

    val activeProperties: List<EntityProperty>
        get() = activity.currentProperties

    fun initialize(context: Context) {
        activity.initialize(context)
    }

    fun tick(context: Context) {
        activity.tick(context)
    }

    fun dispose(context: Context) {
        activity.dispose(context)
    }
}