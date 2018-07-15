package it.krzeminski.fsynth

import it.krzeminski.fsynth.songs.simpleDemoSong
import it.krzeminski.fsynth.songs.vanHalenJumpIntro
import it.krzeminski.fsynth.typings.materialButton
import react.dom.div
import react.dom.render
import kotlin.browser.document

fun main(args: Array<String>) {
    render(document.getElementById("root")) {
        div {
            +"The sound is synthesized and played after clicking the chosen button below."
        }
        materialButton {
            +"Play 'Simple demo song'"
            attrs {
                onClick = {
                    playSong(simpleDemoSong)
                }
            }
        }
        materialButton {
            +"Play 'Van Halen - Jump (intro)'"
            attrs {
                onClick = {
                    playSong(vanHalenJumpIntro)
                }
            }
        }
    }
}
