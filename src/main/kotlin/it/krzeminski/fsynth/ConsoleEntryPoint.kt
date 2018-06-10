package it.krzeminski.fsynth

import it.krzeminski.fsynth.songs.simpleDemoSong

fun sineWaveForNote(note: MusicNote) = sineWave(note.frequency)

fun main(args: Array<String>) {
    configureOutputFormat()

    render8bit(wave = simpleDemoSong, length = 2.0f, sampleRate = 8000)
}
