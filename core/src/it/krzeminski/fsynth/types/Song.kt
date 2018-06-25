package it.krzeminski.fsynth.types

import it.krzeminski.fsynth.silence

data class Song(val waveform: Waveform, val durationInSeconds: Float)

fun song(instrument: (MusicNote) -> Waveform, volume: Float, init: SongBuilder.() -> Unit): Song {
    val songBuilder = SongBuilder(instrument, volume)
    songBuilder.init()
    return songBuilder.build()
}

private data class SongSegment(val waveform: Waveform, val durationInSeconds: Float)

class SongBuilder(private val instrument: (MusicNote) -> Waveform, private val volume: Float) {
    private val segments = mutableListOf<SongSegment>()

    fun note(durationInSeconds: Float, note: MusicNote) {
        segments += SongSegment(instrument(note), durationInSeconds)
    }

    fun chord(durationInSeconds: Float, vararg notes: MusicNote) {
        segments += SongSegment(
                notes.asSequence()
                        .map(instrument)
                        .fold(silence) { accumulator, current -> accumulator + current },
                durationInSeconds)
    }

    fun pause(durationInSeconds: Float) {
        segments += SongSegment(silence, durationInSeconds)
    }

    fun build(): Song {
        val durationInSeconds = segments.map { it.durationInSeconds }.sum()
        return Song(
                waveform = ::getSongWaveformValue,
                durationInSeconds = durationInSeconds)
    }

    private fun getSongWaveformValue(time: Float): Float {
        var cumulatedDuration = 0.0f
        for (segment in segments) {
            if (time in cumulatedDuration..(cumulatedDuration + segment.durationInSeconds)) {
                return segment.waveform(time)*volume
            }
            cumulatedDuration += segment.durationInSeconds
        }
        return 0.0f
    }
}
