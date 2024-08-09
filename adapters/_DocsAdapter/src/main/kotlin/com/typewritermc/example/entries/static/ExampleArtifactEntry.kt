package com.typewritermc.example.entries.static

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.AssetManager
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.entries.ArtifactEntry
import org.koin.java.KoinJavaComponent

//<code-block:artifact_entry>
@Entry("example_artifact", "An example artifact entry.", Colors.BLUE, "material-symbols:home-storage-rounded")
class ExampleArtifactEntry(
    override val id: String = "",
    override val name: String = "",
    override val artifactId: String = "",
) : ArtifactEntry
//</code-block:artifact_entry>

//<code-block:artifact_access>
suspend fun accessArtifactData(ref: Ref<out ArtifactEntry>) {
    val assetManager = KoinJavaComponent.get<AssetManager>(AssetManager::class.java)
    val entry = ref.get() ?: return
    val content: String? = assetManager.fetchAsset(entry)
    // Do something with the content
}
//</code-block:artifact_access>