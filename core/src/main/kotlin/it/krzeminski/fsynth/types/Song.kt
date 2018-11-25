package it.krzeminski.fsynth.types

data class Song(val name: String, val beatsPerMinute: Int, val volume: Float, val tracks: List<Track>)

data class Track(val name: String?, val instrument: (Float) -> Waveform, val segments: List<TrackSegment>)

sealed class TrackSegment {
    data class SingleNote(val value: NoteValue, val pitch: MusicNote) : TrackSegment()
    data class Chord(val value: NoteValue, val pitches: List<MusicNote>) : TrackSegment()
    data class Pause(val value: NoteValue) : TrackSegment()
}

data class NoteValue(val numerator: Int, val denominator: Int)
