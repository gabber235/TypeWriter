package com.typewritermc.engine.paper.loader

import com.typewritermc.loader.DependencyChecker
import lirand.api.extensions.server.server

class PaperDependencyChecker : DependencyChecker {
    override fun hasDependency(dependency: String): Boolean = server.pluginManager.isPluginEnabled(dependency)
}