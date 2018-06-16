package it.krzeminski.fsynth

import it.krzeminski.fsynth.songs.simpleDemoSong
import it.krzeminski.fsynth.typings.AudioBuffer
import it.krzeminski.fsynth.typings.AudioContext
import org.khronos.webgl.Float32Array
import org.khronos.webgl.set

fun main(args: Array<String>) =
    playSong(simpleDemoSong, 2.0f)

fun playSong(song: (Float) -> Float, lengthInSeconds: Float) {
    val samplesPerSecond = 8000

    val buffer = renderSongToArray(song, lengthInSeconds, samplesPerSecond)
    val context = AudioContext()
    val contextBuffer = createAudioContextBuffer(context, buffer, samplesPerSecond)

    startPlayback(context, contextBuffer)
}

private fun renderSongToArray(song: (Float) -> Float, lengthInSeconds: Float, samplesPerSecond: Int): Float32Array {
    val numberOfSamplesToRender = (lengthInSeconds*samplesPerSecond.toFloat()).toInt()
    val buffer = Float32Array(numberOfSamplesToRender)

    for (sample in 0..numberOfSamplesToRender) {
        buffer[sample] = song(sample.toFloat()/samplesPerSecond.toFloat())
    }

    return buffer
}

private fun createAudioContextBuffer(context: AudioContext, buffer: Float32Array, samplesPerSecond: Int): AudioBuffer {
    val contextBuffer = context.createBuffer(
            numberOfChannels = 1, length = buffer.length, sampleRate = samplesPerSecond)
    contextBuffer.copyToChannel(buffer, 0)
    return contextBuffer
}

fun startPlayback(context: AudioContext, audioBuffer: AudioBuffer) =
    with (context.createBufferSource()) {
        buffer = audioBuffer
        connect(context.destination)
        start(0)
    }
