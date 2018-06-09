package it.krzeminski

import java.io.PrintStream

fun configureOutputFormat() =
        // Needed to be able to output the full spectrum of 1-byte values.
        // Otherwise, UTF-8 is used by default, where the size of a single character is variable.
        System.setOut(PrintStream(System.out, true, "ISO8859_1"))

/**
 * Takes [value] of the sound wave, between -1.0 and 1.0, and returns an 8-bit sample (e.g. 0 for -1.0, 255 for 1.0).
 */
fun normalizedWaveValueToCharacter(value: Float): Char =
        (((value + 1.0f)/2.0f)*255.0f).toChar()

/**
 * Prints out raw sound data taken from [wave] to standard output. The first [length] seconds is rendered.
 * 8 bits per sample and [sampleRate] samples per second are used.
 */
fun render8bit(wave: (Float) -> Float, length: Float, sampleRate: Int) {
    val samplesToRender = (sampleRate.toFloat()*length).toInt()
    for (sample in 0..samplesToRender) {
        print(normalizedWaveValueToCharacter(wave(sample.toFloat()/sampleRate.toFloat())))
    }
}
