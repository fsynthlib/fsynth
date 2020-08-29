package it.krzeminski.fsynth

import it.krzeminski.fsynth.songs.allSongs
import kotlinx.browser.document
import kotlinx.browser.window
import react.dom.render

fun main() {
    // Install Progressive Web App worker
    window.addEventListener("load", {
        window.navigator.serviceWorker
                .register("serviceworker.js")
                .then { console.log("Service worker registered!") }
                .catch { console.error("Service worker registration failed: $it") }
    })

    render(document.getElementById("root")) {
        player {
            attrs {
                songs = allSongs
            }
        }
    }
}
