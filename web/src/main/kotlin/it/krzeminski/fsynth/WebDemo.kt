package it.krzeminski.fsynth

import it.krzeminski.fsynth.songs.allSongs
import it.krzeminski.fsynth.typings.materialButton
import react.dom.a
import react.dom.br
import react.dom.div
import react.dom.render
import kotlin.browser.document

fun main(args: Array<String>) {
    render(document.getElementById("root")) {
        div {
            +"The sound is synthesized and played after clicking the chosen button below."
        }
        div {
            +"The synthesis may take some time, even up to 1 minute and block the UI,"
            +" so be patient. Optimization in scope of "
            a("https://github.com/krzema12/fsynth/issues/12") { +"issue #12" }
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
