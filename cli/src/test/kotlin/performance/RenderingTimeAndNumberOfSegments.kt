package performance

import it.krzeminski.fsynth.instruments.synthesizer
import it.krzeminski.fsynth.renderWithSampleRate
import it.krzeminski.fsynth.types.MusicNote
import it.krzeminski.fsynth.types.by
import it.krzeminski.fsynth.types.song
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    (1..1000).forEach { numberOfSegments ->
        val testSong = song(name = "Test song", beatsPerMinute = 100) {
            track(instrument = synthesizer, volume = 1.0f) {
                repeat(numberOfSegments) {
                    note(1 by 4, MusicNote.A4)
                }
            }
        }
        val timeMillis = measureTimeMillis {
            testSong.renderWithSampleRate(44100).forEach { }
        }
        println("Segments: $numberOfSegments, time in ms: $timeMillis")
    }
}
