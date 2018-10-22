package it.krzeminski.fsynth

import it.krzeminski.fsynth.types.Song

fun render8bit(song: Song, sampleRate: Int): ByteArray =
        song.renderWithSampleRate(sampleRate)
                .map { waveformValue -> normalizedWaveValueToByte(waveformValue) }
                .toList()
                .toByteArray()

/**
 * Takes [value] of the sound wave, between -1.0 and 1.0, and returns an 8-bit sample (e.g. 0 for -1.0, 255 for 1.0).
 */
private fun normalizedWaveValueToByte(value: Float): Byte =
        (((value + 1.0f) / 2.0f) * 255.0f).toByte()
