package me.gabber235.typewriter.interaction

import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.event.PacketListenerAbstract
import com.github.retrooper.packetevents.event.PacketReceiveEvent
import com.github.retrooper.packetevents.event.PacketSendEvent
import com.github.retrooper.packetevents.event.ProtocolPacketEvent
import com.github.retrooper.packetevents.protocol.packettype.ClientBoundPacket
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon
import com.github.retrooper.packetevents.protocol.packettype.ServerBoundPacket
import org.bukkit.entity.Player
import org.koin.java.KoinJavaComponent.get
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class PacketInterceptor : PacketListenerAbstract() {
    private val blockers = ConcurrentHashMap<UUID, PlayerPacketInterceptor>()

    fun initialize() {
        PacketEvents.getAPI().eventManager.registerListener(this)
    }

    override fun onPacketReceive(event: PacketReceiveEvent?) {
        if (event == null) return
        val player = event.player
        if (player !is Player) return
        val interceptor = blockers[player.uniqueId] ?: return
        interceptor.trigger(event)
    }

    override fun onPacketSend(event: PacketSendEvent?) {
        if (event == null) return
        val player = event.player
        if (player !is Player) return
        val interceptor = blockers[player.uniqueId] ?: return
        interceptor.trigger(event)
    }

    fun interceptPacket(player: UUID, interception: PacketInterception): PacketInterceptionSubscription {
        val subscription = PacketInterceptionSubscription()
        blockers.compute(player) { _, interceptor ->
            val newInterceptor = interceptor ?: PlayerPacketInterceptor()
            newInterceptor.intercept(subscription, interception)
            newInterceptor
        }
        return subscription
    }

    fun cancel(player: UUID, subscription: PacketInterceptionSubscription) {
        blockers.compute(player) { _, blocker ->
            val newBlocker = blocker ?: return@compute null
            if (newBlocker.cancel(subscription)) null else newBlocker
        }
    }

    fun shutdown() {
        PacketEvents.getAPI().eventManager.unregisterListener(this)
        blockers.clear()
    }
}

private data class PlayerPacketInterceptor(
    val interceptions: ConcurrentHashMap<PacketInterceptionSubscription, PacketInterception> = ConcurrentHashMap()
) {
    fun intercept(
        subscription: PacketInterceptionSubscription,
        interception: PacketInterception
    ): PacketInterceptionSubscription {
        interceptions[subscription] = interception
        return subscription
    }

    fun cancel(subscription: PacketInterceptionSubscription): Boolean {
        interceptions.remove(subscription)
        return interceptions.isEmpty()
    }

    fun trigger(event: ProtocolPacketEvent<Any>) {
        interceptions.values
            .asSequence()
            .filter { it.type == event.packetType }
            .forEach { it.onIntercept(event) }
    }
}

data class PacketInterceptionSubscription(
    val subscriptionId: UUID = UUID.randomUUID(),
)

interface PacketInterception {
    val type: PacketTypeCommon
    fun onIntercept(event: ProtocolPacketEvent<Any>)
}

class PacketBlocker(
    override val type: PacketTypeCommon,
) : PacketInterception {
    override fun onIntercept(event: ProtocolPacketEvent<Any>) {
        event.isCancelled = true
    }
}

class CustomPacketReceiveInterception(
    override val type: PacketTypeCommon,
    private val intercept: (PacketReceiveEvent) -> Unit
) : PacketInterception {
    override fun onIntercept(event: ProtocolPacketEvent<Any>) {
        if (event !is PacketReceiveEvent) return
        intercept(event)
    }
}

class CustomPacketSendInterception(
    override val type: PacketTypeCommon,
    private val intercept: (PacketSendEvent) -> Unit
) : PacketInterception {
    override fun onIntercept(event: ProtocolPacketEvent<Any>) {
        if (event !is PacketSendEvent) return
        intercept(event)
    }
}

fun Player.interceptPackets(block: InterceptionBundle.() -> Unit): InterceptionBundle {
    val bundle = InterceptionBundle(uniqueId)
    block(bundle)
    return bundle
}

class InterceptionBundle(private val playerId: UUID) {
    private val subscriptions = mutableListOf<PacketInterceptionSubscription>()

    private fun intercept(interception: PacketInterception) {
        val subscription = get<PacketInterceptor>(PacketInterceptor::class.java).interceptPacket(playerId, interception)
        subscriptions.add(subscription)
    }

    operator fun PacketTypeCommon.not() {
        intercept(PacketBlocker(this))
    }

    operator fun ClientBoundPacket.invoke(onIntercept: (PacketSendEvent) -> Unit) {
        if (this !is PacketTypeCommon) return
        intercept(CustomPacketSendInterception(this, onIntercept))
    }

    operator fun ServerBoundPacket.invoke(onIntercept: (PacketReceiveEvent) -> Unit) {
        if (this !is PacketTypeCommon) return
        intercept(CustomPacketReceiveInterception(this, onIntercept))
    }

    fun cancel() {
        val interceptor = get<PacketInterceptor>(PacketInterceptor::class.java)
        subscriptions.forEach { interceptor.cancel(playerId, it) }
        subscriptions.clear()
    }
}
