package it.krzeminski.fsynth

import it.krzeminski.fsynth.songs.allSongs
import kotlinx.browser.window
import react.FC
import react.Props
import react.create
import react.dom.client.createRoot
import web.dom.ElementId
import web.dom.document
import web.html.HTMLElement

fun main() {
    // Install Progressive Web App service worker
    window.addEventListener("load", {
        window.navigator.serviceWorker
                .register("/serviceworker.js")
                .then { console.log("Service worker registered!") }
                .catch { console.error("Service worker registration failed: $it") }
    })

    val root = document.getElementById("root".unsafeCast<ElementId>())
    if (root != null) {
        val app = FC<Props> {
            Player {
                songs = allSongs
            }
        }
        createRoot(root).render(app.create())
    }
}
