package me.gabber235.typewriter.entries.entity

import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.entry.entries.Npc

@Tags("z_npc")
interface ZNPC : Npc {
    val npcId: String
}