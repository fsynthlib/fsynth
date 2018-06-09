package it.krzeminski

import it.krzeminski.MusicNote.*

fun playNote(note: MusicNote, length: Float) {
    val sineWaveForFrequency = sineWave(note.frequency)
    render8bit(wave = sineWaveForFrequency, length = length, sampleRate = 8000)
}

fun silence(length: Float) {
    render8bit(wave = { _ -> 0.0f }, length = length, sampleRate = 8000)
}

fun main(args: Array<String>) {
    configureOutputFormat()

    playNote(D4, 0.25f)
    playNote(Csharp4, 0.125f)
    playNote(D4, 0.125f)
    playNote(E4, 0.25f)
    playNote(D4, 0.25f)
    silence(0.25f)
    playNote(Fsharp4, 0.25f)
    playNote(G4, 0.5f)
}
