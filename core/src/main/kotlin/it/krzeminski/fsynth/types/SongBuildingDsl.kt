package it.krzeminski.fsynth.types

fun song(name: String, beatsPerMinute: Int, volume: Float, init: SongBuilder.() -> Unit): Song {
    val songBuilder = SongBuilder(name, beatsPerMinute, volume)
    songBuilder.init()
    return songBuilder.build()
}

@DslMarker
annotation class SongDslMarker

@SongDslMarker
class SongBuilder(
        name: String,
        private val beatsPerMinute: Int,
        volume: Float)
{
    private var song = Song(name = name, beatsPerMinute = beatsPerMinute, volume = volume, tracks = emptyList())

    fun track(name: String? = null, instrument: (Float) -> Waveform, init: TrackBuilder.() -> Unit) {
        val trackBuilder = TrackBuilder(instrument, name)
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

@SongDslMarker
class TrackBuilder(
        private val instrument: (Float) -> Waveform,
        private val name: String?)
{
    private var track = Track(name = name, instrument = instrument, segments = emptyList())

    fun note(value: NoteValue, pitch: MusicNote) {
        track = track.copy(segments = track.segments + TrackSegment.SingleNote(value, pitch))
    }

    fun chord(value: NoteValue, vararg pitches: MusicNote) {
        track = track.copy(segments = track.segments + TrackSegment.Chord(value, pitches.toList()))
    }

    fun pause(value: NoteValue) {
        track = track.copy(segments = track.segments + TrackSegment.Pause(value))
    }

    fun build(): Track {
        return track
    }
}

infix fun Int.by(denominator: Int): NoteValue = NoteValue(this, denominator)
