package com.typewritermc.engine.paper.entry.quest

import com.typewritermc.core.entries.Query
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.ref
import com.typewritermc.engine.paper.entry.FactListenerSubscription
import com.typewritermc.engine.paper.entry.entries.QuestEntry
import com.typewritermc.engine.paper.entry.listenForFacts
import com.typewritermc.engine.paper.events.AsyncQuestStatusUpdate
import com.typewritermc.engine.paper.events.AsyncTrackedQuestUpdate
import com.typewritermc.engine.paper.interaction.InteractionHandler
import com.typewritermc.engine.paper.utils.ThreadType.DISPATCHERS_ASYNC
import org.bukkit.entity.Player
import org.koin.java.KoinJavaComponent.get
import java.util.concurrent.ConcurrentHashMap

class QuestTracker(
    val player: Player,
) {
    private val quests = ConcurrentHashMap<Ref<QuestEntry>, QuestStatus>()
    private var trackedQuest: Ref<QuestEntry>? = null

    private var factWatchSubscription: FactListenerSubscription? = null

    fun setup() {
        Query.find<QuestEntry>().forEach { refresh(it.ref()) }

        refreshWatchedFacts()
    }

    private fun refreshWatchedFacts() {
        factWatchSubscription?.cancel(player)
        val facts = Query.find<QuestEntry>().flatMap { it.facts }.toList()
        factWatchSubscription = player.listenForFacts(
            facts,
            listener = { _, ref ->
                Query.findWhere<QuestEntry> { quest ->
                    quest.facts.contains(ref)
                }.forEach {
                    refresh(it.ref())
                }
            }
        )
    }

    fun dispose() {
        factWatchSubscription?.cancel(player)
    }

    private fun refresh(ref: Ref<QuestEntry>) {
        val quest = ref.get() ?: return
        val status = quest.questStatus(player)

        val oldStatus = quests[ref]
        quests[ref] = status
        if (oldStatus == null) return
        if (oldStatus == status) return

        if (oldStatus != QuestStatus.ACTIVE && status == QuestStatus.ACTIVE) {
            trackQuest(ref)
        } else if (trackedQuest == ref) {
            unTrackQuest()
        }

        DISPATCHERS_ASYNC.launch {
            AsyncQuestStatusUpdate(player, ref, oldStatus, status).callEvent()
        }
    }

    fun inactiveQuests() = quests.filterValues { it == QuestStatus.INACTIVE }.keys.toList()
    fun activeQuests() = quests.filterValues { it == QuestStatus.ACTIVE }.keys.toList()
    fun completedQuests() = quests.filterValues { it == QuestStatus.COMPLETED }.keys.toList()
    fun isQuestInactive(quest: Ref<QuestEntry>) = quests[quest] == QuestStatus.INACTIVE
    fun isQuestActive(quest: Ref<QuestEntry>) = quests[quest] == QuestStatus.ACTIVE
    fun isQuestCompleted(quest: Ref<QuestEntry>) = quests[quest] == QuestStatus.COMPLETED
    fun trackedQuest() = trackedQuest

    fun trackQuest(quest: Ref<QuestEntry>) {
        val from = trackedQuest
        trackedQuest = quest
        DISPATCHERS_ASYNC.launch {
            AsyncTrackedQuestUpdate(player, from, quest).callEvent()
        }
    }

    fun unTrackQuest() {
        val from = trackedQuest
        trackedQuest = null
        DISPATCHERS_ASYNC.launch {
            AsyncTrackedQuestUpdate(player, from, null).callEvent()
        }
    }

    companion object {
        @JvmStatic
        fun inactiveQuests(player: Player) = player.inactiveQuests()

        @JvmStatic
        fun activeQuests(player: Player) = player.activeQuests()

        @JvmStatic
        fun completedQuests(player: Player) = player.completedQuests()

        @JvmStatic
        fun isQuestInactive(player: Player, quest: Ref<QuestEntry>) = player isQuestInactive quest

        @JvmStatic
        fun isQuestActive(player: Player, quest: Ref<QuestEntry>) = player isQuestActive quest

        @JvmStatic
        fun isQuestCompleted(player: Player, quest: Ref<QuestEntry>) = player isQuestCompleted quest

        @JvmStatic
        fun trackedQuest(player: Player) = player.trackedQuest()

        @JvmStatic
        fun isQuestTracked(player: Player, quest: Ref<QuestEntry>) = player isQuestTracked quest

        @JvmStatic
        fun trackQuest(player: Player, quest: Ref<QuestEntry>) = player trackQuest quest

        @JvmStatic
        fun unTrackQuest(player: Player) = player.unTrackQuest()
    }
}

private val Player.tracker: QuestTracker?
    get() = with(get<InteractionHandler>(InteractionHandler::class.java)) {
        interaction?.questTracker
    }

fun Player.inactiveQuests() = tracker?.inactiveQuests() ?: emptyList()
fun Player.activeQuests() = tracker?.activeQuests() ?: emptyList()
fun Player.completedQuests() = tracker?.completedQuests() ?: emptyList()
infix fun Player.isQuestInactive(quest: Ref<QuestEntry>) = tracker?.isQuestInactive(quest) ?: true
infix fun Player.isQuestActive(quest: Ref<QuestEntry>) = tracker?.isQuestActive(quest) ?: false
infix fun Player.isQuestCompleted(quest: Ref<QuestEntry>) = tracker?.isQuestCompleted(quest) ?: false
fun Player.trackedQuest() = tracker?.trackedQuest()
infix fun Player.isQuestTracked(quest: Ref<QuestEntry>): Boolean {
    val trackedQuest = trackedQuest()
    if (!quest.isSet) return trackedQuest != null
    return trackedQuest == quest
}

infix fun Player.trackQuest(quest: Ref<QuestEntry>) = tracker?.trackQuest(quest)
fun Player.unTrackQuest() = tracker?.unTrackQuest()

enum class QuestStatus {
    INACTIVE,
    ACTIVE,
    COMPLETED
}
