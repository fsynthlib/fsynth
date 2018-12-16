package it.krzeminski.fsynth.types

import it.krzeminski.fsynth.instruments.organs
import kotlin.test.Test
import it.krzeminski.fsynth.types.MusicNote.*
import kotlin.test.assertEquals

class SongBuildingDslTest {
    companion object {
        val testInstrument = ::organs
    }

    @Test
    fun severalSimpleNotes() {
        val songCreatedWithDsl = song("Test song", 240) {
            track("Test track", testInstrument, 1.0f) {
                note(1 by 4, C4)
                note(1 by 8, E4)
                note(1 by 2, G4)
            }
        }

        val expectedSong = Song(
                name = "Test song",
                beatsPerMinute = 240,
                tracks = listOf(
                        Track(
                                name = "Test track",
                                instrument = testInstrument,
                                volume = 1.0f,
                                segments = listOf(
                                        TrackSegment.SingleNote(NoteValue(1, 4), C4),
                                        TrackSegment.SingleNote(NoteValue(1, 8), E4),
                                        TrackSegment.SingleNote(NoteValue(1, 2), G4)
                                )
                        )
                )
        )

        assertEquals(expected = expectedSong, actual = songCreatedWithDsl)
    }

    @Test
    fun glissando() {
        val songCreatedWithDsl = song("Test song", 240) {
            track("Test track", testInstrument, 1.0f) {
                glissando(1 by 4, C4 to E4)
            }
        }

        val expectedSong = Song(
                name = "Test song",
                beatsPerMinute = 240,
                tracks = listOf(
                        Track(
                                name = "Test track",
                                instrument = testInstrument,
                                volume = 1.0f,
                                segments = listOf(
                                        TrackSegment.Glissando(NoteValue(1, 4), MusicNoteTransition(C4, E4))
                                )
                        )
                )
        )

        assertEquals(expected = expectedSong, actual = songCreatedWithDsl)
    }

    @Test
    fun chord() {
        val songCreatedWithDsl = song("Test song", 240) {
            track("Test track", testInstrument, 1.0f) {
                chord(1 by 4, C4, E4, G4)
            }
        }

        val expectedSong = Song(
                name = "Test song",
                beatsPerMinute = 240,
                tracks = listOf(
                        Track(
                                name = "Test track",
                                instrument = testInstrument,
                                volume = 1.0f,
                                segments = listOf(
                                        TrackSegment.Chord(NoteValue(1, 4), listOf(C4, E4, G4))
                                )
                        )
                )
        )

        assertEquals(expected = expectedSong, actual = songCreatedWithDsl)
    }

    @Test
    fun pause() {
        val songCreatedWithDsl = song("Test song", 240) {
            track("Test track", testInstrument, 1.0f) {
                pause(1 by 4)
            }
        }

        val expectedSong = Song(
                name = "Test song",
                beatsPerMinute = 240,
                tracks = listOf(
                        Track(
                                name = "Test track",
                                instrument = testInstrument,
                                volume = 1.0f,
                                segments = listOf(
                                        TrackSegment.Pause(NoteValue(1, 4))
                                )
                        )
                )
        )

        assertEquals(expected = expectedSong, actual = songCreatedWithDsl)
    }

    @Test
    fun severalTracks() {
        val songCreatedWithDsl = song("Test song", 240) {
            track("Test track", testInstrument, 0.123f) {
                note(1 by 4, C4)
            }
            track("Test track 2", testInstrument, 0.456f) {
                note(1 by 8, E4)
            }
        }

        val expectedSong = Song(
                name = "Test song",
                beatsPerMinute = 240,
                tracks = listOf(
                        Track(
                                name = "Test track",
                                instrument = testInstrument,
                                volume = 0.123f,
                                segments = listOf(
                                        TrackSegment.SingleNote(NoteValue(1, 4), C4)
                                )
                        ),
                        Track(
                                name = "Test track 2",
                                instrument = testInstrument,
                                volume = 0.456f,
                                segments = listOf(
                                        TrackSegment.SingleNote(NoteValue(1, 8), E4)
                                )
                        )
                )
        )

        assertEquals(expected = expectedSong, actual = songCreatedWithDsl)
    }
}
