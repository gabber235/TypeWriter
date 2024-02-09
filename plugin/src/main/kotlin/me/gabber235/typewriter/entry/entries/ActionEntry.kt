package me.gabber235.typewriter.entry.entries

import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.TriggerableEntry
import me.gabber235.typewriter.entry.triggerEntriesFor
import me.gabber235.typewriter.facts.FactDatabase
import org.bukkit.entity.Player
import org.koin.java.KoinJavaComponent.get

@Tags("action")
interface ActionEntry : TriggerableEntry {
    fun execute(player: Player) {
        val factDatabase: FactDatabase = get(FactDatabase::class.java)
        factDatabase.modify(player, modifiers)
    }
}

@Tags("custom_triggering_action")
interface CustomTriggeringActionEntry : ActionEntry {
    // Disable the normal triggers. So that the action can manually trigger the next actions.
    override val triggers: List<Ref<TriggerableEntry>>
        get() = emptyList()

    @Help("The entries that will be fired after this entry.")
    val customTriggers: List<Ref<TriggerableEntry>>

    fun Player.triggerCustomTriggers() {
        customTriggers triggerEntriesFor this
    }
}