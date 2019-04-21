package it.krzeminski.fsynth

import it.krzeminski.fsynth.types.Song
import java.io.ByteArrayInputStream
import java.nio.file.Path
import javax.sound.sampled.AudioFileFormat
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.sound.sampled.LineEvent
import kotlin.system.measureTimeMillis

fun Song.asAudioStream(samplesPerSecond: Int, sampleSizeInBits: Int, startTime: Float): AudioInputStream? {
    require(sampleSizeInBits == 8) { "Only 8-bit samples are supported now!" }

    lateinit var rawData: ByteArray
    val synthesisTimeInMilliseconds = measureTimeMillis {
        rawData = render8bit(song = this, sampleRate = samplesPerSecond, startTime = startTime)
    }

    if (rawData.isEmpty()) {
        println("No song data to play, returning a null AudioInputStream")
        return null
    }

    println("Synthesized in ${synthesisTimeInMilliseconds.toFloat() / 1000.0f} s")
    val audioFormat = buildAudioFormat(samplesPerSecond, sampleSizeInBits)
    return prepareAudioInputStream(rawData, audioFormat)
}

fun AudioInputStream.playAndBlockUntilFinishes() {
    println("Playing...")
    val audioInputStream = this
    with(AudioSystem.getClip()) {
        open(audioInputStream)
        start()
        sleepUntilPlaybackFinishes()
    }
}

fun AudioInputStream.saveAsWaveFile(path: Path) {
    AudioSystem.write(this, AudioFileFormat.Type.WAVE, path.toFile())
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
