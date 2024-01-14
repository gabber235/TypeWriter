package me.gabber235.typewriter.capture

import com.google.gson.Gson
import me.gabber235.typewriter.entry.StagingManager
import me.gabber235.typewriter.ui.ClientSynchronizer
import org.bukkit.entity.Player
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.inject

open class ImmediateFieldCapturer<T>(
    override val title: String,
    private val entryId: String,
    private val fieldPath: String,
    private val capturer: ImmediateCapturer<T>,
) : ImmediateCapturer<T> {
    private val gson: Gson by inject(Gson::class.java, named("entryParser"))
    private val stagingManager: StagingManager by inject(StagingManager::class.java)
    private val clientSynchronizer: ClientSynchronizer by inject(ClientSynchronizer::class.java)
    override suspend fun capture(player: Player): T {
        val result = capturer.capture(player)
        val data = gson.toJsonTree(result)

        val pageId = stagingManager.findEntryPage(entryId).getOrThrow()

        stagingManager.updateEntryField(pageId, entryId, fieldPath, data)
        // TODO: This should automatically be done.
        clientSynchronizer.sendEntryFieldUpdate(pageId, entryId, fieldPath, data)

        return result
    }
}