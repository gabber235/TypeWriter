package com.typewritermc.visibility.packet

import com.github.retrooper.packetevents.protocol.player.GameMode
import com.github.retrooper.packetevents.protocol.player.TextureProperty
import com.github.retrooper.packetevents.protocol.player.UserProfile
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoRemove
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoUpdate
import com.typewritermc.core.utils.switchContext
import com.typewritermc.engine.paper.extensions.packetevents.sendPacketTo
import com.typewritermc.engine.paper.plugin
import com.typewritermc.engine.paper.utils.Sync
import com.typewritermc.engine.paper.utils.server
import com.typewritermc.visibility.VisibilityHideRegistry
import com.typewritermc.visibility.rule.PlayerPair
import com.typewritermc.visibility.rule.VisibilityRule
import kotlinx.coroutines.Dispatchers
import org.bukkit.entity.Player
import org.koin.java.KoinJavaComponent
import java.util.EnumSet

val VisibilityRule.viewerPlayer: Player? get() = server.getPlayer(viewer)
val VisibilityRule.targetPlayer: Player? get() = server.getPlayer(target)

private val hideRegistry: VisibilityHideRegistry
    get() = KoinJavaComponent.get(VisibilityHideRegistry::class.java)

/**
 * Forces the server to re send every tracking packet of the target to the viewer by untracking and
 * re tracking the pair. The re sent packets pass through the [VisibilityPacketBridge], so hooks
 * registered before this runs apply to the fresh state.
 *
 * Driven by the engine once per lifecycle transition, for the effectors that report
 * [com.typewritermc.visibility.effector.VisibilityEffector.needsPairRerender]. Used for effects that
 * only apply on spawn, such as skins and names. Metadata style effects send synthetic packets
 * instead, which avoids the respawn flicker entirely.
 *
 * A disguised pair only gets its tab list entry back: hiding the target removes it along with the
 * world entity, and cycling the entity here would reveal the real target beside the fake. The
 * entry passes the profile hooks like a tracked re add does, so a bundled name effect still
 * renames it.
 */
suspend fun PlayerPair.refreshRendering() {
    Dispatchers.Sync.switchContext {
        // Bukkit refuses to hide anyone once the plugin is disabled, and by then every client is
        // being dropped anyway.
        if (!plugin.isEnabled) return@switchContext
        if (viewer == target) return@switchContext
        val viewerPlayer = server.getPlayer(viewer) ?: return@switchContext
        val targetPlayer = server.getPlayer(target) ?: return@switchContext
        if (hideRegistry.replacementFor(viewer, target) != null) {
            resendTabEntry(viewerPlayer, targetPlayer)
            return@switchContext
        }
        if (!viewerPlayer.canSee(targetPlayer)) return@switchContext
        viewerPlayer.hidePlayer(plugin, targetPlayer)
        viewerPlayer.showPlayer(plugin, targetPlayer)
    }
}

/**
 * Removes the target's tab list entry for the viewer and adds it again from live server state.
 *
 * Only the entry travels, never the world entity, which stays hidden while a disguise is active.
 * The add passes the profile hooks, so hiding this behind the hook registration in the lifecycle
 * is what renames and reskins the entry.
 */
private fun resendTabEntry(viewer: Player, target: Player) {
    WrapperPlayServerPlayerInfoRemove(target.uniqueId) sendPacketTo viewer
    val textures = target.playerProfile.properties.map { TextureProperty(it.name, it.value, it.signature) }
    val entry = WrapperPlayServerPlayerInfoUpdate.PlayerInfo(
        UserProfile(target.uniqueId, target.name, textures),
        viewer.isListed(target),
        target.ping,
        GameMode.valueOf(target.gameMode.name),
        null,
        null,
        target.playerListOrder,
        true,
    )
    // Every action set, the way the server initializes an entry it tracks. Each action carries its
    // own slice of the entry, so sending only the add would drop the listed flag, the latency and
    // the game mode on the client.
    WrapperPlayServerPlayerInfoUpdate(
        TAB_INIT_ACTIONS,
        entry,
    ) sendPacketTo viewer
}

private val TAB_INIT_ACTIONS: EnumSet<WrapperPlayServerPlayerInfoUpdate.Action> = EnumSet.of(
    WrapperPlayServerPlayerInfoUpdate.Action.ADD_PLAYER,
    WrapperPlayServerPlayerInfoUpdate.Action.INITIALIZE_CHAT,
    WrapperPlayServerPlayerInfoUpdate.Action.UPDATE_GAME_MODE,
    WrapperPlayServerPlayerInfoUpdate.Action.UPDATE_LISTED,
    WrapperPlayServerPlayerInfoUpdate.Action.UPDATE_LATENCY,
    WrapperPlayServerPlayerInfoUpdate.Action.UPDATE_DISPLAY_NAME,
    WrapperPlayServerPlayerInfoUpdate.Action.UPDATE_LIST_ORDER,
    WrapperPlayServerPlayerInfoUpdate.Action.UPDATE_HAT,
)
