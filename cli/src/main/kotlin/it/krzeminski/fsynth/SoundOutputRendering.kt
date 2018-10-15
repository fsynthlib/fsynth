package it.krzeminski.fsynth

import it.krzeminski.fsynth.types.Song
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
 * Prints out raw sound data taken from [song] to standard output.
 * 8 bits per sample and [sampleRate] samples per second are used.
 */
fun render8bit(song: Song, sampleRate: Int) {
    song.renderWithSampleRate(sampleRate)
            .map(::normalizedWaveValueToCharacter)
            .forEach(::print)
}
