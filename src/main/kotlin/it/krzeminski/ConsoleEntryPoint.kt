package it.krzeminski

import java.io.PrintStream
import java.lang.Math.pow

fun normalizedWaveValueToCharacter(value: Float): Char =
        (((value + 1.0f)/2.0f)*255.0f).toChar()

fun noteIndexToFrequency(index: Int) = pow(2.0, (index - 9).toDouble()/12.0).toFloat()*440.0f/32.0f

fun playNote(index: Int, samples: Int) {
    val sineWaveForFrequency = sineWave(noteIndexToFrequency(index))
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

    playNote(62, 2000)
    playNote(61, 1000)
    playNote(62, 1000)
    playNote(64, 2000)
    playNote(62, 2000)
    silence(2000)
    playNote(66, 2000)
    playNote(67, 4000)
}

