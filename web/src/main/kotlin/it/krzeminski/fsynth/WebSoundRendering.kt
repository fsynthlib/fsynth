package it.krzeminski.fsynth

import it.krzeminski.fsynth.postprocessing.reduceLevelsPerSample
import it.krzeminski.fsynth.types.Song
import it.krzeminski.fsynth.typings.AudioBuffer
import it.krzeminski.fsynth.typings.AudioContext
import it.krzeminski.testutils.measureTimeSeconds
import org.khronos.webgl.Float32Array
import kotlin.math.pow

fun Song.renderToAudioBuffer(
    synthesisSamplesPerSecond: Int,
    playbackSamplesPerSecond: Int,
    downcastToBitsPerSample: Int?,
    tempoOffset: Int
): AudioBuffer {
    lateinit var buffer: Float32Array
    val songAfterTempoAdjustment = this.copy(beatsPerMinute = beatsPerMinute + tempoOffset)
    val timeInSeconds = measureTimeSeconds {
        buffer = renderSongToArray(songAfterTempoAdjustment, synthesisSamplesPerSecond, downcastToBitsPerSample)
    }
    println("Synthesized in $timeInSeconds s")
    val context = AudioContext()

    return createAudioContextBuffer(context, buffer, playbackSamplesPerSecond)
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
