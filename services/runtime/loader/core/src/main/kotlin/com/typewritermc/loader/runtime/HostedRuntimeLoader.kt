package com.typewritermc.loader.runtime

import com.typewritermc.loader.api.HostedDeploymentContext
import com.typewritermc.loader.api.HostedRuntimeEntrypoint
import com.typewritermc.loader.api.StagedHostedRuntime
import java.lang.reflect.Modifier
import java.net.URLClassLoader

/**
 * Stages a runtime from the resolved deployment context.
 *
 * Success transfers runtime and class loader ownership to the caller. Failure must release resources acquired
 * while staging.
 */
fun interface HostedRuntimeStager {
    suspend fun stage(context: HostedDeploymentContext): LoadedHostedRuntime
}

/**
 * Loads the manifest selected hosted entrypoint in a new URL class loader containing only the runtime artifact.
 *
 * The parent supplies shared API types. Staging failure closes the loader with cleanup causes preserved; success
 * must be released through [LoadedHostedRuntime].
 */
class HostedRuntimeLoader(
    private val parentClassLoader: ClassLoader = HostedRuntimeEntrypoint::class.java.classLoader,
) : HostedRuntimeStager {
    override suspend fun stage(context: HostedDeploymentContext): LoadedHostedRuntime {
        val runtimeManifest = context.artifacts.runtimeArtifact.manifest
        val classLoader =
            URLClassLoader(
                arrayOf(
                    context.artifacts.runtimeArtifact.path
                        .toUri()
                        .toURL(),
                ),
                parentClassLoader,
            )
        try {
            val entrypointClass =
                Class.forName(
                    runtimeManifest.runtimeEntrypointClass,
                    false,
                    classLoader,
                )
            require(HostedRuntimeEntrypoint::class.java.isAssignableFrom(entrypointClass)) {
                "Hosted runtime entrypoint ${runtimeManifest.runtimeEntrypointClass} does not implement HostedRuntimeEntrypoint."
            }
            val constructor = entrypointClass.getDeclaredConstructor()
            require(Modifier.isPublic(constructor.modifiers)) {
                "Hosted runtime entrypoint ${runtimeManifest.runtimeEntrypointClass} requires a public zero argument constructor."
            }
            val entrypoint = constructor.newInstance() as HostedRuntimeEntrypoint
            return LoadedHostedRuntime(entrypoint.stage(context), classLoader)
        } catch (failure: Throwable) {
            runCatching { classLoader.close() }.exceptionOrNull()?.let(failure::addSuppressed)
            throw failure
        }
    }
}

/**
 * Owns the staged runtime and the class loader retaining its artifact code.
 *
 * Closure retains the class loader until runtime cleanup succeeds. Failed cleanup can be retried.
 */
class LoadedHostedRuntime internal constructor(
    val runtime: StagedHostedRuntime,
    private val classLoader: URLClassLoader,
) {
    private var runtimeClosed = false

    suspend fun close() {
        if (!runtimeClosed) {
            runtime.close()
            runtimeClosed = true
        }
        classLoader.close()
    }
}
