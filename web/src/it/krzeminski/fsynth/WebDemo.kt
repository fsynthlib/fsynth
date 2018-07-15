package it.krzeminski.fsynth

import it.krzeminski.fsynth.songs.simpleDemoSong
import it.krzeminski.fsynth.songs.vanHalenJumpIntro
import it.krzeminski.fsynth.types.Song
import it.krzeminski.fsynth.typings.AudioBuffer
import it.krzeminski.fsynth.typings.AudioContext
import it.krzeminski.fsynth.typings.materialButton
import org.khronos.webgl.Float32Array
import org.khronos.webgl.set
import react.dom.div
import react.dom.render
import kotlin.browser.document

fun main(args: Array<String>) {
    render(document.getElementById("root")) {
        div {
            +"The sound is synthesized and played after clicking the chosen button below."
        }
        materialButton {
            +"Play 'Simple demo song'"
            attrs {
                onClick = {
                    playSong(simpleDemoSong)
                }
            }
        }
        materialButton {
            +"Play 'Van Halen - Jump (intro)'"
            attrs {
                onClick = {
                    playSong(vanHalenJumpIntro)
                }
            }
        }
    }
}

fun playSong(song: Song) {
    val samplesPerSecond = 44100

    val buffer = renderSongToArray(song, samplesPerSecond)
    val context = AudioContext()
    val contextBuffer = createAudioContextBuffer(context, buffer, samplesPerSecond)

    startPlayback(context, contextBuffer)
}

private fun renderSongToArray(song: Song, samplesPerSecond: Int): Float32Array {
    val numberOfSamplesToRender = (song.durationInSeconds*samplesPerSecond.toFloat()).toInt()
    val buffer = Float32Array(numberOfSamplesToRender)

    for (sample in 0..numberOfSamplesToRender) {
        buffer[sample] = song.waveform(sample.toFloat()/samplesPerSecond.toFloat())
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
