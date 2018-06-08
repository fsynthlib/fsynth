package it.krzeminski

import java.io.PrintStream

import it.krzeminski.MusicNote.*

fun normalizedWaveValueToCharacter(value: Float): Char =
        (((value + 1.0f)/2.0f)*255.0f).toChar()

fun playNote(note: MusicNote, samples: Int) {
    val sineWaveForFrequency = sineWave(note.frequency)
    for (t in 0..samples) {
        print(normalizedWaveValueToCharacter(sineWaveForFrequency(t.toFloat()/8000.0f)))
    }
}

fun silence(samples: Int) {
    for (t in 0..samples) {
        print(normalizedWaveValueToCharacter(0.0f))
    }
}

fun main(args: Array<String>) {
    System.setOut(PrintStream(System.out, true, "ISO8859_1"))

    playNote(D4, 2000)
    playNote(Csharp4, 1000)
    playNote(D4, 1000)
    playNote(E4, 2000)
    playNote(D4, 2000)
    silence(2000)
    playNote(Fsharp4, 2000)
    playNote(G4, 4000)
}
