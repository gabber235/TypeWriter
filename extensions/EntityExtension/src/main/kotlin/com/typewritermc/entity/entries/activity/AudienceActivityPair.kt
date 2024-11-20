package com.typewritermc.entity.entries.activity

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.emptyRef
import com.typewritermc.engine.paper.entry.entity.EntityActivity
import com.typewritermc.engine.paper.entry.entity.IndividualActivityContext
import com.typewritermc.engine.paper.entry.entity.PositionProperty
import com.typewritermc.engine.paper.entry.entity.SingleChildActivity
import com.typewritermc.engine.paper.entry.entries.AudienceEntry
import com.typewritermc.engine.paper.entry.entries.EntityActivityEntry
import com.typewritermc.engine.paper.entry.entries.IndividualEntityActivityEntry
import com.typewritermc.engine.paper.entry.inAudience

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
        currentLocation: PositionProperty
    ): EntityActivity<IndividualActivityContext> {
        return AudienceActivity(activities, defaultActivity, currentLocation)
    }
}

class AudienceActivityPair(
    val audience: Ref<out AudienceEntry> = emptyRef(),
    val activity: Ref<out IndividualEntityActivityEntry> = emptyRef(),
)

class AudienceActivity(
    private val activities: List<AudienceActivityPair>,
    private val defaultActivity: Ref<out EntityActivityEntry>,
    startLocation: PositionProperty,
) : SingleChildActivity<IndividualActivityContext>(startLocation) {
    override fun currentChild(context: IndividualActivityContext): Ref<out EntityActivityEntry> {
        val player = context.viewer

        return activities.firstOrNull { player.inAudience(it.audience) }?.activity ?: defaultActivity
    }
}