package com.typewritermc.loader

interface DependencyChecker {
    fun hasDependency(dependency: String): Boolean
}