package me.gabber235.typewriter.capture

import com.google.gson.Gson
import me.gabber235.typewriter.entry.AssetManager
import me.gabber235.typewriter.entry.entries.AssetEntry
import org.bukkit.entity.Player
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.inject

open class AssetCapturer<T>(
    override val title: String,
    private val asset: AssetEntry,
    private val capturer: RecordedCapturer<T>,
) : RecordedCapturer<T> {
    private val gson: Gson by inject(Gson::class.java, named("bukkitDataParser"))
    private val assetManager: AssetManager by inject(AssetManager::class.java)
    override fun startRecording(player: Player) = capturer.startRecording(player)
    override fun captureFrame(player: Player, frame: Int) = capturer.captureFrame(player, frame)

    override fun stopRecording(player: Player): T {
        val result = capturer.stopRecording(player)
        val data = gson.toJson(result)

        assetManager.storeAsset(asset, data)

        return result
    }
}