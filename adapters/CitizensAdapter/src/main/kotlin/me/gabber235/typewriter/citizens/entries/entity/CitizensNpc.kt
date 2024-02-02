package me.gabber235.typewriter.citizens.entries.entity

import me.gabber235.typewriter.adapters.Tags

@Tags("citizens_npc")
interface CitizensNpc : me.gabber235.typewriter.entry.entries.Npc {
    val npcId: Int
}