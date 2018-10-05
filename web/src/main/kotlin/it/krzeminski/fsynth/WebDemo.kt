package it.krzeminski.fsynth

import it.krzeminski.fsynth.songs.allSongs
import it.krzeminski.fsynth.typings.materialButton
import react.dom.br
import react.dom.div
import react.dom.render
import kotlin.browser.document

fun main(args: Array<String>) {
    render(document.getElementById("root")) {
        div {
            +"The sound is synthesized and played after clicking the chosen button below."
        }
        allSongs.forEach { song ->
            materialButton {
                +"Play '${song.name}'"
                attrs {
                    onClick = {
                        playSong(song)
                    }
                }
            }
            br {  }
        }
    }
}
