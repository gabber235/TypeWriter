package me.gabber235.typewriter.entry.quest

import com.github.shynixn.mccoroutine.bukkit.launch
import com.github.shynixn.mccoroutine.bukkit.ticks
import kotlinx.coroutines.delay
import lirand.api.extensions.events.SimpleListener
import lirand.api.extensions.events.listen
import lirand.api.extensions.events.unregister
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.QuestEntry
import me.gabber235.typewriter.events.AsyncQuestStatusUpdate
import me.gabber235.typewriter.events.AsyncTrackedQuestUpdate
import me.gabber235.typewriter.events.PublishedBookEvent
import me.gabber235.typewriter.events.TypewriterReloadEvent
import me.gabber235.typewriter.interaction.InteractionHandler
import me.gabber235.typewriter.plugin
import me.gabber235.typewriter.utils.ThreadType.DISPATCHERS_ASYNC
import org.bukkit.entity.Player
import org.koin.java.KoinJavaComponent.get
import java.util.concurrent.ConcurrentHashMap

class QuestTracker(
    val player: Player,
) {
    private val quests = ConcurrentHashMap<Ref<QuestEntry>, QuestStatus>()
    private var trackedQuest: Ref<QuestEntry>? = null

    private val listener = SimpleListener()
    private var factWatchSubscription: FactListenerSubscription? = null

    init {
        Query.find<QuestEntry>().forEach { refresh(it.ref()) }
        plugin.listen<TypewriterReloadEvent>(listener) { refreshWatchedFacts() }
        plugin.listen<PublishedBookEvent>(listener) { refreshWatchedFacts() }

        // At this point, the player is not yet in the interaction handler.
        // So we wait until the next tick to register the listener.
        plugin.launch {
            delay(1.ticks)
            refreshWatchedFacts()
        }
    }

    private fun refreshWatchedFacts() {
        factWatchSubscription?.cancel(player)
        val facts = Query.find<QuestEntry>().flatMap { it.activeCriteria + it.completedCriteria }.map { it.fact }
        factWatchSubscription = player.listenForFacts(
            facts,
            listener = { _, ref ->
                Query.findWhere<QuestEntry> { quest ->
                    quest.activeCriteria.any { it.fact == ref } || quest.completedCriteria.any { it.fact == ref }
                }.forEach {
                    refresh(it.ref())
                }
            }
        )
    }

    fun dispose() {
        listener.unregister()
        factWatchSubscription?.cancel(player)
    }

    private fun refresh(ref: Ref<QuestEntry>) {
        val quest = ref.get() ?: return
        val status = when {
            quest.completedCriteria.matches(player) -> QuestStatus.COMPLETED
            quest.activeCriteria.matches(player) -> QuestStatus.ACTIVE
            else -> QuestStatus.INACTIVE
        }

        val oldStatus = quests[ref]
        quests[ref] = status
        if (oldStatus == null) return
        if (oldStatus == status) return

        if (oldStatus == QuestStatus.INACTIVE && status == QuestStatus.ACTIVE) {
            trackQuest(ref)
        } else if (trackedQuest == ref && status != QuestStatus.ACTIVE) {
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
    val trackedQuest = trackedQuest() ?: return false
    return trackedQuest == quest
}

infix fun Player.trackQuest(quest: Ref<QuestEntry>) = tracker?.trackQuest(quest)
fun Player.unTrackQuest() = tracker?.unTrackQuest()

enum class QuestStatus {
    INACTIVE,
    ACTIVE,
    COMPLETED
}
