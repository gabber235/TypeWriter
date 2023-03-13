package com.caleb.typewriter.griefdefender.entries.action

import com.griefdefender.api.GriefDefender
import com.griefdefender.api.claim.TrustType
import com.griefdefender.api.claim.TrustTypes
import com.griefdefender.api.claim.TrustTypes.*
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.entries.ActionEntry
import me.gabber235.typewriter.utils.Icons
import org.bukkit.entity.Player
import java.util.*

@Entry("add_trust_to_gd_claim", "Create a schematic for GriefDefender Claim", Colors.ORANGE, Icons.HANDSHAKE)
class TrustPlayerActionEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    override val triggers: List<String> = emptyList(),
    @Help("The claim to use for add trust.")
    private val claim: String = "",
    @Help("The Trust Type. (ACCESSOR, BUILDER, CONTAINER, MANAGER, RESIDENT")
    private var trustType: String = "Accessor",
) : ActionEntry {
    override fun execute(player: Player) {
        super.execute(player)

        val claim = GriefDefender.getCore().getClaim(UUID.fromString(claim)) ?: return

        trustType = trustType.lowercase(Locale.getDefault());
        val trust : TrustType = when (trustType) {
            "accessor" -> ACCESSOR
            "builder" -> BUILDER
            "container" -> CONTAINER
            "manager" -> MANAGER
            "resident" -> RESIDENT
            else -> NONE
        }

        claim.addUserTrust(player.uniqueId, trust)
    }
}