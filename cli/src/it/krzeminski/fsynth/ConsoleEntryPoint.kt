package it.krzeminski.fsynth

import it.krzeminski.fsynth.songs.simpleDemoSong

fun main(args: Array<String>) {
    configureOutputFormat()

    render8bit(song = simpleDemoSong, sampleRate = 8000)
}
