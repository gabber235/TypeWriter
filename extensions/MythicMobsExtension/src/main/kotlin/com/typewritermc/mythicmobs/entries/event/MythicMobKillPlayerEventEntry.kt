package com.typewritermc.mythicmobs.entries.event

/** TODO:
 *  Since minecraft 1.20.5 changed how the player death event source is handled,
 *  we cannot support it for 1.20.4 and 1.20.5 at the same time.
 *  So we can use this again once we drop support for 1.20.4
 */


//@Entry("mythicmobs_kill_player_event", "MythicMob Kill Player Event", Colors.YELLOW, "fa6-solid:skull")
///**
// * The `MythicMob Kill Player Event` event is triggered when MythicMob kills a player.
// *
// * ## How could this be used?
// * When the player is killed by a certain monster, there is a probability that the monster will drag them back to their lair.
// */
//class MythicMobKillPlayerEventEntry(
//    override val id: String = "",
//    override val name: String = "",
//    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
//    @Help("The type of the MythicMob that killed the player")
//    @Placeholder
//    @Regex
//    val mythicMobType: Optional<String> = Optional.empty(),
//) : EventEntry
//
//@EntryListener(MythicMobKillPlayerEventEntry::class)
//fun onMythicMobKillPlayer(event: PlayerDeathEvent, query: Query<MythicMobKillPlayerEventEntry>) {
//    val causingEntity = event.damageSource.causingEntity ?: return
//    val mythicMob = MythicBukkit.inst().mobManager.getMythicMobInstance(causingEntity) ?: return
//
//    query findWhere { entry ->
//        entry.mythicMobType.map { it.toRegex().matches(mythicMob.mobType) }.orElse(true)
//    } triggerAllFor event.player
//}