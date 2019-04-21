package it.krzeminski.fsynth

import it.krzeminski.fsynth.types.Song
import it.krzeminski.fsynth.typings.AudioBuffer
import it.krzeminski.fsynth.typings.AudioContext
import it.krzeminski.testutils.measureTimeSeconds
import org.khronos.webgl.Float32Array

fun playSong(song: Song) {
    val samplesPerSecond = 44100

    lateinit var buffer: Float32Array
    val timeInSeconds = measureTimeSeconds {
        buffer = renderSongToArray(song, samplesPerSecond)
    }
    println("Synthesized in $timeInSeconds s")
    val context = AudioContext()
    val contextBuffer = createAudioContextBuffer(context, buffer, samplesPerSecond)

    startPlayback(context, contextBuffer)
}

private fun renderSongToArray(song: Song, samplesPerSecond: Int): Float32Array {
    return Float32Array(
            song.renderWithSampleRate(samplesPerSecond, startTime = 0.0f)
                    .toList()
                    .toTypedArray())
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
