package me.gabber235.typewriter.extensions.bstats

import me.gabber235.typewriter.adapters.AdapterLoader
import me.gabber235.typewriter.entry.Entry
import me.gabber235.typewriter.entry.Query
import me.gabber235.typewriter.plugin
import org.bstats.bukkit.Metrics
import org.bstats.charts.DrilldownPie
import org.bstats.charts.SimpleBarChart
import org.bukkit.plugin.java.JavaPlugin
import org.koin.java.KoinJavaComponent.get
import kotlin.reflect.full.findAnnotation

object BStatsMetrics {
    private const val ID = 17839
    fun registerMetrics() {
        val metrics = Metrics(plugin as JavaPlugin, ID)

        metrics.addCustomChart(DrilldownPie("version") {
            val version = plugin.pluginMeta.version
            mapOf(version to mapOf("version" to 1))
        })

        metrics.addCustomChart(SimpleBarChart("adapters") {
            val adapters = get<AdapterLoader>(AdapterLoader::class.java)
            adapters.adapters.associate { it.name to 1 }.toMap()
        })

        metrics.addCustomChart(SimpleBarChart("entries") {
            val entries = Query.find<Entry>()
            entries.groupBy {
                it::class.findAnnotation<me.gabber235.typewriter.adapters.Entry>()?.name ?: it::class.simpleName
                ?: "Unknown"
            }.mapValues { it.value.size }.toMap()
        })
    }
}