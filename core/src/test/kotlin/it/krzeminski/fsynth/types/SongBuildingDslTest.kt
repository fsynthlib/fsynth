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
        val songCreatedWithDsl = song("Test song", 240, 1.0f) {
            track("Test track", testInstrument) {
                note(1 by 4, C4)
                note(1 by 8, E4)
                note(1 by 2, G4)
            }
        }

        val expectedSong = Song(
                name = "Test song",
                beatsPerMinute = 240,
                volume = 1.0f,
                tracks = listOf(
                        Track(
                                name = "Test track",
                                instrument = testInstrument,
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
    fun chord() {
        val songCreatedWithDsl = song("Test song", 240, 1.0f) {
            track("Test track", testInstrument) {
                chord(1 by 4, C4, E4, G4)
            }
        }

        val expectedSong = Song(
                name = "Test song",
                beatsPerMinute = 240,
                volume = 1.0f,
                tracks = listOf(
                        Track(
                                name = "Test track",
                                instrument = testInstrument,
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
        val songCreatedWithDsl = song("Test song", 240, 1.0f) {
            track("Test track", testInstrument) {
                pause(1 by 4)
            }
        }

        val expectedSong = Song(
                name = "Test song",
                beatsPerMinute = 240,
                volume = 1.0f,
                tracks = listOf(
                        Track(
                                name = "Test track",
                                instrument = testInstrument,
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
        val songCreatedWithDsl = song("Test song", 240, 0.123f) {
            track("Test track", testInstrument) {
                note(1 by 4, C4)
            }
            track("Test track 2", testInstrument) {
                note(1 by 8, E4)
            }
        }

        val expectedSong = Song(
                name = "Test song",
                beatsPerMinute = 240,
                volume = 0.123f,
                tracks = listOf(
                        Track(
                                name = "Test track",
                                instrument = testInstrument,
                                segments = listOf(
                                        TrackSegment.SingleNote(NoteValue(1, 4), C4)
                                )
                        ),
                        Track(
                                name = "Test track 2",
                                instrument = testInstrument,
                                segments = listOf(
                                        TrackSegment.SingleNote(NoteValue(1, 8), E4)
                                )
                        )
                )
        )

        assertEquals(expected = expectedSong, actual = songCreatedWithDsl)
    }
}
