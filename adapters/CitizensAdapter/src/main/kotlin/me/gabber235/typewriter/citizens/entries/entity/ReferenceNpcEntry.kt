package me.gabber235.typewriter.citizens.entries.entity

import com.google.gson.JsonObject
import lirand.api.extensions.other.set
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.utils.*
import net.citizensnpcs.api.CitizensAPI
import kotlin.jvm.optionals.getOrDefault
import net.kyori.adventure.sound.Sound as AdventureSound

@Deprecated("Use the EntityAdapter instead")
@Tags("reference_npc")
@Entry("reference_npc", "When the npc is not managed by TypeWriter", Colors.ORANGE, "fa-solid:user-tie")
/**
 * An identifier that references an NPC in the Citizens plugin. But does not manage the NPC.
 *
 * ## How could this be used?
 *
 * This can be used to reference an NPC which is already in the world. This could be used to create a quest that requires the player to talk to an NPC.
 */
class ReferenceNpcEntry(
    override val id: String = "",
    override val name: String = "",
    override val displayName: String = "",
    override val sound: Sound,
    @Help("The id of the NPC in the Citizens plugin.")
    override val npcId: Int = 0,
) : CitizensNpc {
    override fun getEmitter(): AdventureSound.Emitter {
        val npc = CitizensAPI.getNPCRegistry().getById(npcId) ?: return AdventureSound.Emitter.self()
        return npc.entity
    }
}

@EntryMigration(ReferenceNpcEntry::class, "0.4.0")
@NeedsMigrationIfNotParsable
fun migrate040ReferenceNpc(json: JsonObject, context: EntryMigratorContext): JsonObject {
    val data = JsonObject()
    data.copyAllBut(json, "sound")

    val soundId = json.getAndParse<String>("sound", context.gson).optional

    val sound = Sound(
        soundId = soundId.map(::DefaultSoundId).getOrDefault(SoundId.EMPTY),
        soundSource = SelfSoundSource,
        volume = 1.0f,
        pitch = 1.0f,
    )

    data["sound"] = context.gson.toJsonTree(sound)

    return data
}