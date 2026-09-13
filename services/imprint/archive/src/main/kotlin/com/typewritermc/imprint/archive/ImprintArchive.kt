package com.typewritermc.imprint.archive

import com.typewritermc.imprint.IMPRINT_MANIFEST_PATH
import com.typewritermc.imprint.ImprintManifest
import com.typewritermc.imprint.ImprintManifestCodec
import java.io.IOException
import java.nio.file.Path
import java.util.zip.ZipFile

/** Result of inspecting an archive for its canonical Imprint manifest. */
sealed interface ImprintArchiveInspection {
    data object Absent : ImprintArchiveInspection

    data class Present(
        val manifest: ImprintManifest,
    ) : ImprintArchiveInspection
}

/** Reads the canonical Imprint manifest without taking ownership of the archive path. */
object ImprintArchive {
    /** Reads all exact archive entries for intermediate metadata, preserving duplicate entries for the caller. */
    fun readEntries(
        path: Path,
        entryName: String,
    ): List<ByteArray> =
        try {
            ZipFile(path.toFile()).use { archive ->
                archive
                    .entries()
                    .asSequence()
                    .filter { it.name == entryName }
                    .map { entry -> archive.getInputStream(entry).use { it.readBytes() } }
                    .toList()
            }
        } catch (failure: Exception) {
            throw IllegalArgumentException("Cannot read archive ${path.fileName} entry $entryName.", failure)
        }

    fun inspect(path: Path): ImprintArchiveInspection =
        try {
            ZipFile(path.toFile()).use { archive ->
                val entries =
                    archive
                        .entries()
                        .asSequence()
                        .filter { it.name == IMPRINT_MANIFEST_PATH }
                        .toList()
                if (entries.isEmpty()) return@use ImprintArchiveInspection.Absent
                require(entries.size == 1) {
                    "Archive ${path.fileName} contains ${entries.size} canonical Imprint manifests."
                }
                val manifest =
                    try {
                        archive.getInputStream(entries.single()).use { input ->
                            ImprintManifestCodec.decode(input.readBytes())
                        }
                    } catch (failure: Exception) {
                        throw IllegalArgumentException("Cannot read Imprint manifest from ${path.fileName}.", failure)
                    }
                ImprintArchiveInspection.Present(manifest)
            }
        } catch (failure: IOException) {
            throw IllegalArgumentException("Cannot inspect Imprint archive ${path.fileName}.", failure)
        }

    fun require(path: Path): ImprintManifest =
        when (val inspection = inspect(path)) {
            ImprintArchiveInspection.Absent -> {
                throw IllegalArgumentException("Archive ${path.fileName} does not contain $IMPRINT_MANIFEST_PATH.")
            }

            is ImprintArchiveInspection.Present -> {
                inspection.manifest
            }
        }
}
