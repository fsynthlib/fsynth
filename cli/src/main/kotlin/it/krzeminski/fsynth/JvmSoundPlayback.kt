package it.krzeminski.fsynth

import it.krzeminski.fsynth.types.Song
import java.io.ByteArrayInputStream
import javax.sound.sampled.*
import kotlin.system.measureTimeMillis

fun Song.playOnJvm(samplesPerSecond: Int, sampleSizeInBits: Int) {
    require(sampleSizeInBits == 8) { "Only 8-bit samples are supported now!" }

    lateinit var rawData: ByteArray
    val synthesisTimeInMilliseconds = measureTimeMillis {
        rawData = render8bit(song = this, sampleRate = 44100)
    }
    println("Synthesized in ${synthesisTimeInMilliseconds.toFloat()/1000.0f} s")
    val audioFormat = buildAudioFormat(samplesPerSecond, sampleSizeInBits)
    val audioInputStream = prepareAudioInputStream(rawData, audioFormat)

    playAndBlockUntilFinishes(audioInputStream)
}

private object AudioFormatConstants {
    const val MONO = 1
    const val UNSIGNED = false
    const val LITTLE_ENDIAN = false
}

private fun buildAudioFormat(samplesPerSecond: Int, sampleSizeInBits: Int): AudioFormat {
    return AudioFormat(
            samplesPerSecond.toFloat(),
            sampleSizeInBits,
            AudioFormatConstants.MONO,
            AudioFormatConstants.UNSIGNED,
            AudioFormatConstants.LITTLE_ENDIAN)
}

private fun prepareAudioInputStream(rawData: ByteArray, audioFormat: AudioFormat): AudioInputStream {
    return AudioInputStream(
            ByteArrayInputStream(rawData),
            audioFormat,
            rawData.size.toLong())
}

private fun playAndBlockUntilFinishes(audioInputStream: AudioInputStream) {
    println("Playing...")
    with (AudioSystem.getClip()) {
        open(audioInputStream)
        start()
        sleepUntilPlaybackFinishes()
    }
}

private fun Clip.sleepUntilPlaybackFinishes() {
    var continuePlayback = true
    addLineListener { event ->
        if (event.type == LineEvent.Type.STOP) {
            continuePlayback = false
        }
    }

    while (continuePlayback) {
        Thread.sleep(100)
    }
}
