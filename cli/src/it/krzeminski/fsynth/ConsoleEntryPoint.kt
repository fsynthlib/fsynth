package it.krzeminski.fsynth

import it.krzeminski.fsynth.songs.vanHalenJumpIntro

fun main(args: Array<String>) {
    configureOutputFormat()

    render8bit(song = vanHalenJumpIntro, sampleRate = 8000)
}
