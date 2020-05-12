package it.krzeminski.fsynth

import it.krzeminski.fsynth.songs.allSongs
import react.dom.render
import kotlin.browser.document

fun main(args: Array<String>) {
    render(document.getElementById("root")) {
        player {
            attrs {
                songs = allSongs
            }
        }
    }
}
