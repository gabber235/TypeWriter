package me.gabber235.typewriter.entries.activity

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.emptyRef
import me.gabber235.typewriter.entry.entity.EntityActivity
import me.gabber235.typewriter.entry.entity.IndividualActivityContext
import me.gabber235.typewriter.entry.entity.LocationProperty
import me.gabber235.typewriter.entry.entity.SingleChildActivity
import me.gabber235.typewriter.entry.entries.AudienceEntry
import me.gabber235.typewriter.entry.entries.EntityActivityEntry
import me.gabber235.typewriter.entry.entries.IndividualEntityActivityEntry
import me.gabber235.typewriter.entry.inAudience

@Entry(
    "audience_activity",
    "Select activity based on the audience a player is in",
    Colors.PALATINATE_BLUE,
    "fluent:people-audience-32-filled"
)
/**
 * The `Audience Activity` is an activity that filters an audience based on the audience a player is in.
 * The activity will go through the audiences in order and the first one
 * where the player is part of the audience will have the activity selected.
 *
 * This can only be used on an individual entity instance.
 *
 * ## How could this be used?
 * This could be used to make a bodyguard distracted by something and walk away just for the player.
 */
class AudienceActivityEntry(
    override val id: String = "",
    override val name: String = "",
    val activities: List<AudienceActivityPair> = emptyList(),
    @Help("The activity that will be used when the player is not in any audience.")
    val defaultActivity: Ref<out EntityActivityEntry> = emptyRef(),
) : IndividualEntityActivityEntry {
    override fun create(
        context: IndividualActivityContext,
        currentLocation: LocationProperty
    ): EntityActivity<IndividualActivityContext> {
        return AudienceActivity(activities, defaultActivity, currentLocation)
    }
}

class AudienceActivityPair(
    val audience: Ref<out AudienceEntry>,
    val activity: Ref<out IndividualEntityActivityEntry>,
)

class AudienceActivity(
    private val activities: List<AudienceActivityPair>,
    private val defaultActivity: Ref<out EntityActivityEntry>,
    startLocation: LocationProperty,
) : SingleChildActivity<IndividualActivityContext>(startLocation) {
    override fun currentChild(context: IndividualActivityContext): Ref<out EntityActivityEntry> {
        val player = context.viewer

        return activities.firstOrNull { player.inAudience(it.audience) }?.activity ?: defaultActivity
    }
}