package it.krzeminski.fsynth.synthesis

import it.krzeminski.fsynth.silence
import it.krzeminski.fsynth.sineWave
import it.krzeminski.fsynth.synthesis.types.SongForSynthesis
import it.krzeminski.fsynth.synthesis.types.TrackForSynthesis
import it.krzeminski.fsynth.types.BoundedWaveform
import it.krzeminski.fsynth.types.MusicNote.* // ktlint-disable no-wildcard-imports
import it.krzeminski.fsynth.types.MusicNoteTransition
import it.krzeminski.fsynth.types.NoteValue
import it.krzeminski.fsynth.types.PositionedBoundedWaveform
import it.krzeminski.fsynth.types.Song
import it.krzeminski.fsynth.types.Track
import it.krzeminski.fsynth.types.TrackSegment
import it.krzeminski.testutils.plotassert.assertFunctionConformsTo
import kotlin.test.Test
import kotlin.test.assertEquals

class PreprocessingTest {
    companion object {
        val testInstrumentForNoteC4 = { _: Float -> 123.0f }
        val testInstrumentForNoteE4 = { _: Float -> 456.0f }
        val testInstrumentForNoteG4 = { _: Float -> 789.0f }
        val testInstrument = { frequency: Float ->
            when (frequency) {
                1.0f -> sineWave(1.0f)
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
                tracks = listOf(
                        Track(
                                name = "Test track",
                                instrument = testInstrument,
                                segments = listOf(
                                        TrackSegment.SingleNote(NoteValue(1, 4), C4),
                                        TrackSegment.SingleNote(NoteValue(1, 8), E4),
                                        TrackSegment.SingleNote(NoteValue(1, 2), G4)
                                ),
                                volume = 1.0f
                        )
                )
        )

        val preprocessedTestSong = testSong.preprocessForSynthesis()

        assertEquals(
                expected = SongForSynthesis(
                        tracks = listOf(
                                TrackForSynthesis(
                                        segments = listOf(
                                                PositionedBoundedWaveform(
                                                        BoundedWaveform(testInstrumentForNoteC4, 0.25f),
                                                        0.0f),
                                                PositionedBoundedWaveform(
                                                        BoundedWaveform(testInstrumentForNoteE4, 0.125f),
                                                        0.25f),
                                                PositionedBoundedWaveform(
                                                        BoundedWaveform(testInstrumentForNoteG4, 0.5f),
                                                        0.375f)),
                                        volume = 1.0f))),
                actual = preprocessedTestSong)
    }

    /* ktlint-disable no-multi-spaces paren-spacing */

    @Test
    fun glissando() {
        val testSong = Song(
                name = "Test song",
                beatsPerMinute = 240,
                tracks = listOf(
                        Track(
                                name = "Test track",
                                instrument = testInstrument,
                                segments = listOf(
                                        TrackSegment.Glissando(NoteValue(1, 4),
                                                MusicNoteTransition(VeryLowForTesting, A0))
                                ),
                                volume = 1.0f
                        )
                )
        )

        val preprocessedTestSong = testSong.preprocessForSynthesis()

        with (preprocessedTestSong.tracks[0]) {
            assertEquals(1, segments.size)
            assertEquals(0.0f, segments[0].startTime)
            assertEquals(0.25f, segments[0].boundedWaveform.duration)
            assertFunctionConformsTo(segments[0].boundedWaveform.waveform) {
                row(1.0f,   "        IIII                          III                   II              I          ")
                row(        "       I    II                       I   I                 I               I I         ")
                row(        "     II       I                           I               I   I               I        ")
                row(        "    I          I                    I                                     I            ")
                row(        "   I            I                  I       I             I     I                       ")
                row(        "                                            I                                          ")
                row(        "  I              I                I                                      I     I       ")
                row(        " I                I                                     I       I                      ")
                row(0.0f,   "X                                I           I                                         ")
                row(        "                   I                                   I         I              I     I")
                row(        "                    I           I             I                         I              ")
                row(        "                                                                                       ")
                row(        "                     I         I               I      I           I                    ")
                row(        "                      I       I                                        I         I   I ")
                row(        "                       I     I                  I    I             I                   ")
                row(        "                        I   I                    I  I                 I           I I  ")
                row(-1.0f,  "                         III                      II                II             I   ")
                xAxis {
                    markers("|                                                                                     |")
                    values( 0.0f,                                                                                0.25f)
                }
            }
        }
    }

    /* ktlint-disable no-multi-spaces paren-spacing */

    @Test
    fun chord() {
        val testSong = Song(
                name = "Test song",
                beatsPerMinute = 240,
                tracks = listOf(
                        Track(
                                name = "Test track",
                                instrument = testInstrument,
                                segments = listOf(
                                        TrackSegment.Chord(NoteValue(1, 4), listOf(C4, E4, G4))
                                ),
                                volume = 1.0f
                        )
                )
        )

        val preprocessedTestSong = testSong.preprocessForSynthesis()

        with (preprocessedTestSong.tracks[0]) {
            assertEquals(1, segments.size)
            assertEquals(0.0f, segments[0].startTime)
            assertEquals(0.25f, segments[0].boundedWaveform.duration)
            assertEquals(123.0f + 456.0f + 789.0f, segments[0].boundedWaveform.waveform(0.0f))
        }
    }

    @Test
    fun pause() {
        val testSong = Song(
                name = "Test song",
                beatsPerMinute = 240,
                tracks = listOf(
                        Track(
                                name = "Test track",
                                instrument = testInstrument,
                                segments = listOf(
                                        TrackSegment.Pause(NoteValue(1, 4))
                                ),
                                volume = 1.0f
                        )
                )
        )

        val preprocessedTestSong = testSong.preprocessForSynthesis()

        assertEquals(
                expected = SongForSynthesis(
                        tracks = listOf(
                                TrackForSynthesis(
                                        segments = listOf(
                                                PositionedBoundedWaveform(BoundedWaveform(silence, 0.25f), 0.0f)),
                                        volume = 1.0f))),
                actual = preprocessedTestSong)
    }

    @Test
    fun severalTracks() {
        val testSong = Song(
                name = "Test song",
                beatsPerMinute = 240,
                tracks = listOf(
                        Track(
                                name = "Test track",
                                instrument = testInstrument,
                                segments = listOf(
                                        TrackSegment.SingleNote(NoteValue(1, 4), C4)
                                ),
                                volume = 1.0f
                        ),
                        Track(
                                name = "Test track 2",
                                instrument = testInstrument,
                                segments = listOf(
                                        TrackSegment.SingleNote(NoteValue(1, 8), E4)
                                ),
                                volume = 1.0f
                        )
                )
        )

        val preprocessedTestSong = testSong.preprocessForSynthesis()

        assertEquals(
                expected = SongForSynthesis(
                        tracks = listOf(
                                TrackForSynthesis(
                                        segments = listOf(
                                                PositionedBoundedWaveform(
                                                        BoundedWaveform(testInstrumentForNoteC4, 0.25f), 0.0f)),
                                        volume = 1.0f),
                                TrackForSynthesis(
                                        segments = listOf(
                                                PositionedBoundedWaveform(
                                                        BoundedWaveform(testInstrumentForNoteE4, 0.125f), 0.0f)),
                                        volume = 1.0f))),
                actual = preprocessedTestSong)
    }
}
