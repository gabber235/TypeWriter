package com.typewritermc.example.entries.static

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.AssetManager
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.entries.AssetEntry
import org.koin.java.KoinJavaComponent

//<code-block:asset_entry>
@Entry("example_asset", "An example asset entry.", Colors.BLUE, "material-symbols:home-storage-rounded")
class ExampleAssetEntry(
    override val id: String = "",
    override val name: String = "",
    override val path: String = "",
) : AssetEntry
//</code-block:asset_entry>

//<code-block:asset_access>
suspend fun accessAssetData(ref: Ref<out AssetEntry>) {
    val assetManager = KoinJavaComponent.get<AssetManager>(AssetManager::class.java)
    val entry = ref.get() ?: return
    val content: String? = assetManager.fetchAsset(entry)
    // Do something with the content
}
//</code-block:asset_access>