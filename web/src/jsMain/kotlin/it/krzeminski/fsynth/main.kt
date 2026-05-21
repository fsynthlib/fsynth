package it.krzeminski.fsynth

import it.krzeminski.fsynth.songs.allSongs
import react.FC
import react.Props
import react.create
import react.dom.client.createRoot
import web.dom.ElementId
import web.dom.document
import web.html.HTMLElement

fun main() {
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
