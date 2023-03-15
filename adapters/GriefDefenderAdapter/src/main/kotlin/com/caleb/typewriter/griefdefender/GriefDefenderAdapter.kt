package com.caleb.typewriter.griefdefender

import App
import com.griefdefender.api.GriefDefender
import com.griefdefender.api.claim.Claim
import com.griefdefender.api.event.BorderClaimEvent
import com.griefdefender.api.event.Event
import com.griefdefender.lib.kyori.event.EventBus
import com.griefdefender.lib.kyori.event.EventSubscriber
import me.gabber235.typewriter.Typewriter
import me.gabber235.typewriter.adapters.Adapter
import me.gabber235.typewriter.adapters.TypewriteAdapter
import org.bukkit.event.HandlerList
import java.util.UUID

@Adapter("GriefDefender", "For Using GriefDefender", App.VERSION)
object GriefDefenderAdapter : TypewriteAdapter() {

	override fun initialize() {
		if (GriefDefender.getCore() == null) {
			Typewriter.plugin.logger.warning("GriefDefender plugin not found, try installing it or disabling the GriefDefender adapter")
			return
		}
		val eventBus: EventBus<Event> = GriefDefender.getEventManager().bus
		eventBus.subscribe(BorderClaimEvent::class.java, EventSubscriber<BorderClaimEvent>() { event ->
			if (event.exitClaim == event.enterClaim) {
				return@EventSubscriber;
			}
			if (!event.enterClaim.isWilderness) event.user?.let { RegionEnterEvent(it.uniqueId, event.enterClaim).callEvent() }
			if (!event.exitClaim.isWilderness) event.user?.let { RegionExitEvent(it.uniqueId, event.exitClaim).callEvent() }
		})
	}

	class RegionEnterEvent(uuid: UUID, claim: Claim) : org.bukkit.event.Event() {
		private var uuid:UUID;
		private var claim:Claim;

		init {
			this.uuid = uuid;
			this.claim = claim
		}

		fun getPlayerUUID() : UUID {
			return this.uuid
		}

		fun getClaim() : Claim {
			return this.claim
		}

		companion object {
			@JvmStatic
			val handlerList = HandlerList()
		}

		override fun getHandlers(): HandlerList {
			return handlerList
		}
	}
	class RegionExitEvent(uuid: UUID, claim: Claim) : org.bukkit.event.Event() {
		private var uuid:UUID;
		private var claim:Claim;

		init {
			this.uuid = uuid;
			this.claim = claim
		}

		fun getPlayerUUID() : UUID {
			return this.uuid
		}

		fun getClaim() : Claim {
			return this.claim
		}

		companion object {
			@JvmStatic
			val handlerList = HandlerList()
		}

		override fun getHandlers(): HandlerList {
			return handlerList
		}
	}

}