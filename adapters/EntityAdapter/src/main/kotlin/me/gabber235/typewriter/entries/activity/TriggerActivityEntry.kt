package me.gabber235.typewriter.entries.activity

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.TriggerableEntry
import me.gabber235.typewriter.entry.emptyRef
import me.gabber235.typewriter.entry.entity.*
import me.gabber235.typewriter.entry.entries.EntityActivityEntry
import me.gabber235.typewriter.entry.entries.GenericEntityActivityEntry
import me.gabber235.typewriter.entry.triggerFor

@Entry("trigger_activity", "Triggers a sequence when the activity active or inactive", Colors.PALATINATE_BLUE, "fa-solid:play")
/**
 * The `Trigger Activity` entry is an activity that triggers a sequence when the activity starts or stops.
 *
 * ## How could this be used?
 * This could be used to trigger dialogue when the entity arrives at a certain location.
 * Like a tour guide that triggers dialogue when the entity arrives at a point of interest.
 */
class TriggerActivityEntry(
    override val id: String = "",
    override val name: String = "",
    @Help("The activity to use when this is active.")
    val activity: Ref<out EntityActivityEntry> = emptyRef(),
    @Help("The sequence to trigger when the activity starts.")
    val onStart: Ref<TriggerableEntry> = emptyRef(),
    @Help("The sequence to trigger when the activity stops.")
    val onStop: Ref<TriggerableEntry> = emptyRef(),
) : GenericEntityActivityEntry {
    override fun create(context: ActivityContext, currentLocation: LocationProperty): EntityActivity<ActivityContext> {
        if (!activity.isSet) return IdleActivity.create(context, currentLocation)
        return TriggerActivity(activity, currentLocation, onStart, onStop)
    }
}

private class TriggerActivity(
    private val ref: Ref<out EntityActivityEntry>,
    private val startLocation: LocationProperty,
    private val onStart: Ref<TriggerableEntry>,
    private val onStop: Ref<TriggerableEntry>,
) : GenericEntityActivity {
    private var activity: EntityActivity<in ActivityContext>? = null

    override fun initialize(context: ActivityContext) {
        activity = ref.get()?.create(context, currentLocation)
        activity?.initialize(context)
        context.viewers.forEach {
            onStart triggerFor it
        }
    }

    override fun tick(context: ActivityContext): TickResult {
        return activity?.tick(context) ?: TickResult.IGNORED
    }

    override fun dispose(context: ActivityContext) {
        context.viewers.forEach {
            onStop triggerFor it
        }
        activity?.dispose(context)
        activity = null
    }

    override val currentLocation: LocationProperty
        get() = activity?.currentLocation ?: startLocation
}