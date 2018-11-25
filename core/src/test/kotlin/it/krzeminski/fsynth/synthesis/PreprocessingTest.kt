package it.krzeminski.fsynth.synthesis

import it.krzeminski.fsynth.silence
import it.krzeminski.fsynth.synthesis.types.SongForSynthesis
import it.krzeminski.fsynth.synthesis.types.TrackForSynthesis
import it.krzeminski.fsynth.synthesis.types.TrackSegmentForSynthesis
import it.krzeminski.fsynth.types.MusicNote.*
import it.krzeminski.fsynth.types.NoteValue
import it.krzeminski.fsynth.types.Song
import it.krzeminski.fsynth.types.Track
import it.krzeminski.fsynth.types.TrackSegment
import kotlin.test.Test
import kotlin.test.assertEquals

class PreprocessingTest {
    companion object {
        val testInstrumentForNoteC4 = { _: Float -> 123.0f }
        val testInstrumentForNoteE4 = { _: Float -> 456.0f }
        val testInstrumentForNoteG4 = { _: Float -> 789.0f }
        val testInstrument = { frequency: Float ->
            when (frequency) {
                C4.frequency -> testInstrumentForNoteC4
                E4.frequency -> testInstrumentForNoteE4
                G4.frequency -> testInstrumentForNoteG4
                else -> throw IllegalStateException("Only the tree above notes should be used in this test!")
            }
        }
    }

    @Test
    fun severalSimpleNotes() {
        val testSong = Song(
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

        val preprocessedTestSong = testSong.preprocessForSynthesis()

        assertEquals(
                expected = SongForSynthesis(
                        tracks = listOf(
                                TrackForSynthesis(listOf(
                                        TrackSegmentForSynthesis(testInstrumentForNoteC4, 0.25f),
                                        TrackSegmentForSynthesis(testInstrumentForNoteE4, 0.125f),
                                        TrackSegmentForSynthesis(testInstrumentForNoteG4, 0.5f)))),
                        volume = 1.0f),
                actual = preprocessedTestSong)
    }

    @Test
    fun chord() {
        val testSong = Song(
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

        val preprocessedTestSong = testSong.preprocessForSynthesis()

        with (preprocessedTestSong.tracks[0]) {
            assertEquals(1, segments.size)
            assertEquals(0.25f, segments[0].durationInSeconds)
            assertEquals(123.0f + 456.0f + 789.0f, segments[0].waveform(0.0f))
        }
    }

    @Test
    fun pause() {
        val testSong = Song(
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

        val preprocessedTestSong = testSong.preprocessForSynthesis()

        assertEquals(
                expected = SongForSynthesis(
                        tracks = listOf(
                                TrackForSynthesis(listOf(
                                        TrackSegmentForSynthesis(silence, 0.25f)))),
                        volume = 1.0f),
                actual = preprocessedTestSong)
    }

    @Test
    fun severalTracks() {
        val testSong = Song(
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

        val preprocessedTestSong = testSong.preprocessForSynthesis()

        assertEquals(
                expected = SongForSynthesis(
                        tracks = listOf(
                                TrackForSynthesis(listOf(
                                        TrackSegmentForSynthesis(testInstrumentForNoteC4, 0.25f))),
                                TrackForSynthesis(listOf(
                                        TrackSegmentForSynthesis(testInstrumentForNoteE4, 0.125f)))),
                        volume = 0.123f),
                actual = preprocessedTestSong)
    }
}
