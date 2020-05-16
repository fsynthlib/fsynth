package it.krzeminski.fsynth

import it.krzeminski.fsynth.postprocessing.reduceLevelsPerSample
import it.krzeminski.fsynth.types.Song
import it.krzeminski.fsynth.typings.AudioBuffer
import it.krzeminski.fsynth.typings.AudioContext
import it.krzeminski.testutils.measureTimeSeconds
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Float32Array
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get
import kotlin.browser.window
import kotlin.math.pow

fun Song.renderToAudioBuffer(downcastToBitsPerSample: Int?): AudioBuffer {
    val samplesPerSecond = 44100

    lateinit var buffer: Float32Array
    val timeInSeconds = measureTimeSeconds {
        buffer = renderSongToArray(this, samplesPerSecond, downcastToBitsPerSample)
    }
    println("Synthesized in $timeInSeconds s")
    val context = AudioContext()

    return createAudioContextBuffer(context, buffer, samplesPerSecond)
}

fun ArrayBuffer.toBase64(): String {
    val bytes = Uint8Array(this)
    val binary = (0..bytes.byteLength).map {
        bytes[it].toChar()
    }.joinToString(separator = "")
    return window.btoa(binary)
}

fun String.base64WavAsDataUrl() = "data:audio/wav;base64,$this"

private fun renderSongToArray(song: Song, samplesPerSecond: Int, downcastToBitsPerSample: Int?): Float32Array {
    return Float32Array(
            song.renderWithSampleRate(samplesPerSecond)
                    .map { it.applyLevelsPerSampleReduction(downcastToBitsPerSample) }
                    .toList()
                    .toTypedArray())
}

private fun Float.applyLevelsPerSampleReduction(downcastToBitsPerSample: Int?) =
        if (downcastToBitsPerSample != null) {
            reduceLevelsPerSample(2.0f.pow(downcastToBitsPerSample).toInt())
        } else { // No levels-per-sample reduction.
            this
        }

private fun createAudioContextBuffer(context: AudioContext, buffer: Float32Array, samplesPerSecond: Int): AudioBuffer {
    val contextBuffer = context.createBuffer(
            numberOfChannels = 1, length = buffer.length, sampleRate = samplesPerSecond)
    contextBuffer.copyToChannel(buffer, 0)
    return contextBuffer
}
