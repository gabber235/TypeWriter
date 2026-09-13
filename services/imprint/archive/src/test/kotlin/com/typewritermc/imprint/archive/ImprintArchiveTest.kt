package com.typewritermc.imprint.archive

import com.typewritermc.imprint.ArtifactId
import com.typewritermc.imprint.ArtifactVersion
import com.typewritermc.imprint.CapabilityManifest
import com.typewritermc.imprint.IMPRINT_MANIFEST_PATH
import com.typewritermc.imprint.ImprintManifestCodec
import com.typewritermc.imprint.VersionConstraint
import de.infix.testBalloon.framework.core.testSuite
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import java.nio.file.Files
import java.nio.file.Path
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

val ImprintArchiveTest by testSuite {
    test("inspect reports an ordinary archive without a manifest as absent") {
        val archive = writeArchive(Files.createTempFile("ordinary", ".jar"))

        ImprintArchive.inspect(archive) shouldBe ImprintArchiveInspection.Absent
    }

    test("require decodes exactly one canonical manifest") {
        val manifest = capabilityManifest()
        val archive = writeArchive(Files.createTempFile("manifest", ".jar"), ImprintManifestCodec.encode(manifest))

        ImprintArchive.require(archive) shouldBe manifest
    }

    test("require preserves malformed manifest context") {
        val archive = writeArchive(Files.createTempFile("malformed", ".jar"), byteArrayOf(1, 2, 3))

        val failure = shouldThrow<IllegalArgumentException> { ImprintArchive.require(archive) }
        failure.message shouldBe "Cannot read Imprint manifest from ${archive.fileName}."
    }

    test("inspect preserves malformed archive context") {
        val archive = Files.createTempFile("not-an-archive", ".jar")
        Files.writeString(archive, "not a jar")

        val failure = shouldThrow<IllegalArgumentException> { ImprintArchive.inspect(archive) }
        failure.message shouldBe "Cannot inspect Imprint archive ${archive.fileName}."
    }
}

private fun writeArchive(
    path: Path,
    manifest: ByteArray? = null,
): Path {
    ZipOutputStream(Files.newOutputStream(path)).use { output ->
        if (manifest != null) {
            output.putNextEntry(ZipEntry(IMPRINT_MANIFEST_PATH))
            output.write(manifest)
            output.closeEntry()
        }
    }
    return path
}

private fun capabilityManifest() =
    CapabilityManifest(
        id = ArtifactId("typewritermc:test"),
        version = ArtifactVersion("1.0.0"),
        directRequirements = emptyList(),
        resolvedCapabilities = emptyList(),
        contributions = emptyList(),
    )
