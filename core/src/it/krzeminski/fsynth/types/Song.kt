package it.krzeminski.fsynth.types

import it.krzeminski.fsynth.silence

data class Song(val segments: List<SongSegment>, private val volume: Float) {
    val waveform: Waveform
        get() = ::getSongWaveformValue
    val durationInSeconds: Float
        get() = segments.map { it.durationInSeconds }.sum()

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

fun song(beatsPerMinute: Int, instrument: (MusicNote) -> Waveform, volume: Float, init: SongBuilder.() -> Unit): Song {
    val songBuilder = SongBuilder(beatsPerMinute, instrument, volume)
    songBuilder.init()
    return songBuilder.build()
}

data class SongSegment(val waveform: Waveform, val durationInSeconds: Float)

class SongBuilder(
        private val beatsPerMinute: Int,
        private val instrument: (MusicNote) -> Waveform,
        volume: Float)
{
    private var song = Song(emptyList(), volume)

    fun note(noteValue: Float, note: MusicNote) {
        song = song.copy(segments = song.segments + SongSegment(instrument(note), noteValue.toSeconds()))
    }

    fun chord(noteValue: Float, vararg notes: MusicNote) {
        song = song.copy(segments = song.segments +
                SongSegment(
                        notes.asSequence()
                                .map(instrument)
                                .fold(silence) { accumulator, current -> accumulator + current },
                        noteValue.toSeconds()))
    }

    fun pause(noteValue: Float) {
        song = song.copy(segments = song.segments + SongSegment(silence, noteValue.toSeconds()))
    }

    fun build(): Song {
        return song
    }

    private fun Float.toSeconds() = this*BEATS_PER_BAR*SECONDS_IN_MINUTE/beatsPerMinute.toFloat()
}

// Constant for now.
// TODO: generalize it (may be not always 4).
private const val BEATS_PER_BAR = 4
private const val SECONDS_IN_MINUTE = 60

infix fun Int.by(denominator: Int): Float = this.toFloat()/denominator.toFloat()
