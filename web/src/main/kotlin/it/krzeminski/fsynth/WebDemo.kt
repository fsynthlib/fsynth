package it.krzeminski.fsynth

import it.krzeminski.fsynth.songs.allSongs
import it.krzeminski.fsynth.types.Song
import it.krzeminski.fsynth.typings.materialButton
import react.dom.a
import react.dom.br
import react.dom.div
import react.dom.render
import kotlin.browser.document

fun main(args: Array<String>) {
    render(document.getElementById("root")) {
        div { +"The sound is synthesized and played after clicking the chosen button below." }
        div { +"The synthesis may take several seconds, and blocks the UI, so please be patient." }
        div {
            +"Making this UI more user-friendly is in scope of "
            a("https://github.com/krzema12/fsynth/issues/14") { +"issue #14" }
        }
        allSongs.forEach { song ->
            materialButton {
                +"Play '${song.name}' (${song.getHumanFriendlyDuration()})"
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

private fun Song.getHumanFriendlyDuration(): String =
        with(durationInSeconds.toInt()) {
            val seconds = this.rem(60)
            val minutes = this / 60
            return "$minutes:${seconds.toString().padStart(2, '0')}"
        }
