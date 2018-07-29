package it.krzeminski.fsynth.types

import it.krzeminski.fsynth.silence

data class Song(val name: String, val tracks: List<Track>, private val volume: Float) {
    val waveform: Waveform
        get() = ::getSongWaveformValue
    val durationInSeconds: Float
        get() = tracks.map { it.segments.map { it.durationInSeconds }.sum() }.max() ?: 0.0f

    private fun getSongWaveformValue(time: Float): Float =
            tracks.map { it.getWaveformValue(time) }.sum()

    private fun Track.getWaveformValue(time: Float): Float {
        var cumulatedDuration = 0.0f
        for (segment in segments) {
            if (time in cumulatedDuration..(cumulatedDuration + segment.durationInSeconds)) {
                return segment.waveform(time - cumulatedDuration)*volume
            }
            cumulatedDuration += segment.durationInSeconds
        }
        return 0.0f
    }
}

data class Track(val segments: List<TrackSegment>, val name: String?)

data class TrackSegment(val waveform: Waveform, val durationInSeconds: Float)

fun song(name: String, beatsPerMinute: Int, volume: Float, init: SongBuilder.() -> Unit): Song {
    val songBuilder = SongBuilder(name, beatsPerMinute, volume)
    songBuilder.init()
    return songBuilder.build()
}

class SongBuilder(
        name: String,
        private val beatsPerMinute: Int,
        volume: Float)
{
    private var song = Song(name, emptyList(), volume)

    fun track(instrument: (MusicNote) -> Waveform, init: TrackBuilder.() -> Unit) {
        val trackBuilder = TrackBuilder(instrument = instrument, beatsPerMinute = beatsPerMinute, name = null)
        initAndAppendTrack(trackBuilder, init)
    }

    fun track(name: String, instrument: (MusicNote) -> Waveform, init: TrackBuilder.() -> Unit) {
        val trackBuilder = TrackBuilder(instrument, beatsPerMinute, name)
        initAndAppendTrack(trackBuilder, init)
    }

    private fun initAndAppendTrack(trackBuilder: TrackBuilder, init: TrackBuilder.() -> Unit) {
        trackBuilder.init()
        song = song.copy(tracks = song.tracks + trackBuilder.build())
    }

    fun build(): Song {
        return song
    }
}

class TrackBuilder(
        private val instrument: (MusicNote) -> Waveform,
        private val beatsPerMinute: Int,
        private val name: String?)
{
    private var track = Track(segments = emptyList(), name = name)

    fun note(noteValue: Float, note: MusicNote) {
        track = track.copy(segments = track.segments + TrackSegment(instrument(note), noteValue.toSeconds()))
    }

    fun chord(noteValue: Float, vararg notes: MusicNote) {
        track = track.copy(segments = track.segments +
                TrackSegment(
                        notes.asSequence()
                                .map(instrument)
                                .fold(silence) { accumulator, current -> accumulator + current },
                        noteValue.toSeconds()))
    }

    fun pause(noteValue: Float) {
        track = track.copy(segments = track.segments + TrackSegment(silence, noteValue.toSeconds()))
    }

    fun build(): Track {
        return track
    }

    private fun Float.toSeconds() = this*BEATS_PER_BAR*SECONDS_IN_MINUTE/beatsPerMinute.toFloat()
}

// Constant for now.
// TODO: generalize it (may be not always 4).
private const val BEATS_PER_BAR = 4
private const val SECONDS_IN_MINUTE = 60

infix fun Int.by(denominator: Int): Float = this.toFloat()/denominator.toFloat()
