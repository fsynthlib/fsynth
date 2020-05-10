package it.krzeminski.fsynth

import it.krzeminski.fsynth.postprocessing.reduceLevelsPerSample
import it.krzeminski.fsynth.types.Song
import it.krzeminski.fsynth.typings.AudioBuffer
import it.krzeminski.fsynth.typings.AudioContext
import it.krzeminski.testutils.measureTimeSeconds
import org.khronos.webgl.Float32Array
import kotlin.math.pow

fun playSong(song: Song, downcastToBitsPerSample: Int?) {
    val samplesPerSecond = 44100

    lateinit var buffer: Float32Array
    val timeInSeconds = measureTimeSeconds {
        buffer = renderSongToArray(song, samplesPerSecond, downcastToBitsPerSample)
    }
    println("Synthesized in $timeInSeconds s")
    val context = AudioContext()
    val contextBuffer = createAudioContextBuffer(context, buffer, samplesPerSecond)

    startPlayback(context, contextBuffer)
}

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

private fun startPlayback(context: AudioContext, audioBuffer: AudioBuffer) =
        with(context.createBufferSource()) {
            buffer = audioBuffer
            connect(context.destination)
            start(0)
        }
