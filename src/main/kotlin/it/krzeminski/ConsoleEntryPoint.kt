package it.krzeminski

import java.lang.Math.pow

fun normalizedWaveValueToCharacter(value: Float): Char =
        (((value + 1.0f)/2.0f)*255.0f).toChar()

fun noteIndexToFrequency(index: Int) = pow(2.0, (index - 9).toDouble()/12.0).toFloat()*440.0f/32.0f

fun playNote(index: Int) {
    val sineWaveForFrequency = sineWave(noteIndexToFrequency(index))
    for (t in 0..800) {
        print(normalizedWaveValueToCharacter(sineWaveForFrequency(t.toFloat()/8000.0f)))
    }
}

fun main(args: Array<String>) {
    playNote(60)
    playNote(62)
    playNote(64)
    playNote(65)
    playNote(67)
    playNote(69)
    playNote(71)
    playNote(72)
}

