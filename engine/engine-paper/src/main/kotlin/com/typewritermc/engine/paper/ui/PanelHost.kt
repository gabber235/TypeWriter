package com.typewritermc.engine.paper.ui


import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.typewritermc.engine.paper.logger
import com.typewritermc.engine.paper.utils.config
import org.koin.core.component.KoinComponent
import java.io.File

class PanelHost : KoinComponent {
    private val enabled: Boolean by config("panel.enabled", false)
    private val port: Int by config("panel.port", 8080)

    private var server: ApplicationEngine? = null
    fun initialize() {
        if (!enabled) {
            // If we are developing the ui we don't want to start the server
            logger.warning("The panel is disabled while the websocket is enabled. This is only for development purposes. Please enable either both or none.")
            return
        }
        server = embeddedServer(io.ktor.server.netty.Netty, port) {
            routing {
                static {
                    serveResources("web")
                    defaultResource("index.html", "web")
                }
            }
        }.start(wait = false)
    }

    fun dispose() {
        server?.stop()
    }
}

fun Route.serveResources(resourcePackage: String? = null) {
    get("{static-resources...}/") {
        call.serve(resourcePackage)
    }

    get("{static-resources...}") {
        call.serve(resourcePackage)
    }
}

suspend fun ApplicationCall.serve(resourcePackage: String? = null) {
    val relativePath = parameters.getAll("static-resources")?.joinToString(File.separator) ?: return

    // This is key part. We either resolve some resource or resolve index.html using path from the request
    val content = resolveResource(relativePath, resourcePackage)
        ?: resolveResource("$relativePath/index.html", resourcePackage)

    if (content != null) {
        respond(content)
    }
}