package it.krzeminski.fsynth

import it.krzeminski.fsynth.songs.allSongs
import kotlinx.browser.document
import react.dom.render

fun main() {
    render(document.getElementById("root")) {
        player {
            attrs {
                songs = allSongs
            }
        }
    }
}
