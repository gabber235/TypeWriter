package com.typewritermc.engine.paper.extensions.bstats

import com.typewritermc.core.entries.Entry
import com.typewritermc.core.entries.Query
import com.typewritermc.engine.paper.plugin
import com.typewritermc.loader.ExtensionLoader
import org.bstats.bukkit.Metrics
import org.bstats.charts.AdvancedPie
import org.koin.java.KoinJavaComponent.get
import kotlin.reflect.full.findAnnotation

object BStatsMetrics {
    private const val ID = 17839
    fun registerMetrics() {
        val metrics = Metrics(plugin, ID)

        metrics.addCustomChart(AdvancedPie("extensions") {
            get<ExtensionLoader>(ExtensionLoader::class.java).extensions.associate { it.extension.name to 1 }.toMap()
        })

        metrics.addCustomChart(AdvancedPie("entries") {
            val entries = Query.find<Entry>()
            entries.groupBy {
                it::class.findAnnotation<com.typewritermc.core.extension.annotations.Entry>()?.name
                    ?: it::class.simpleName
                    ?: "Unknown"
            }.mapValues { it.value.size }.toMap()
        })
    }
}