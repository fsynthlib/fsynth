package it.krzeminski.fsynth.types

import it.krzeminski.fsynth.instruments.Instrument

data class Song(val name: String, val beatsPerMinute: Int, val tracks: List<Track>)

data class Track(
    val name: String?,
    val instrument: Instrument,
    val volume: Float,
    val segments: List<TrackSegment>
)

sealed class TrackSegment {
    data class SingleNote(val value: NoteValue, val pitch: MusicNote) : TrackSegment()
    data class Glissando(val value: NoteValue, val transition: MusicNoteTransition) : TrackSegment()
    data class Chord(val value: NoteValue, val pitches: List<MusicNote>) : TrackSegment()
    data class Pause(val value: NoteValue) : TrackSegment()
}

data class NoteValue(val numerator: Int, val denominator: Int)

data class MusicNoteTransition(val startPitch: MusicNote, val endPitch: MusicNote)
