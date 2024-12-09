package com.typewritermc.core.utils

interface Reloadable {
    suspend fun load()
    suspend fun unload()
}