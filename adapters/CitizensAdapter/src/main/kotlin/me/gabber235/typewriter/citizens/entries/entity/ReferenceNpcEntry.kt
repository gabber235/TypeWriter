package me.gabber235.typewriter.citizens.entries.entity

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.utils.Icons

@Tags("reference_npc")
@Entry("reference_npc", "When the npc is not managed by TypeWriter", Colors.ORANGE, Icons.PERSON)
/**
 * An identifier that references an NPC in the Citizens plugin. But does not manage the NPC.
 *
 * ## How could this be used?
 *
 * This can be used to reference an NPC which is already in the world. This could be used to create a quest that requires the player to talk to an NPC.
 */
class ReferenceNpcEntry(
    override val id: String = "",
    override val name: String = "",
    override val displayName: String = "",
    override val sound: String = "",
    @Help("The id of the NPC in the Citizens plugin.")
    val npcId: Int = 0,
) : Npc