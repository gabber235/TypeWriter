package com.typewritermc.imprint.gradle

import com.typewritermc.imprint.ArtifactKind
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity

/** Preserves source part declarations as named inputs for manifest generation and incremental builds. */
abstract class ManifestSourcePartInput {
    @get:Input
    abstract val name: Property<String>

    @get:Input
    abstract val kind: Property<ArtifactKind>

    @get:Input
    abstract val includes: ListProperty<String>
}

/** Keeps each dependency constraint bound to its artifact, including the artifact's producer task dependency. */
abstract class ManifestRelationshipInput {
    @get:Input
    abstract val sourcePart: Property<String>

    @get:Input
    abstract val index: Property<Int>

    @get:Input
    abstract val expectedKind: Property<ArtifactKind>

    @get:Input
    abstract val constraint: Property<String>

    @get:InputFile
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val artifact: RegularFileProperty
}
