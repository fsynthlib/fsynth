package it.krzeminski.fsynth.types

import it.krzeminski.fsynth.types.MusicNote.*
import kotlin.test.Test
import kotlin.test.assertEquals

class SongTest {
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
        val testSong = song(name = "Test song", beatsPerMinute = 240, volume = 1.0f) {
            track(name = "Test track", instrument = testInstrument) {
                note(1 by 4, C4)
                note(1 by 8, E4)
                note(1 by 2, G4)
            }
        }

        assertEquals(
                expected = Song(
                        name = "Test song",
                        tracks = listOf(
                                Track(listOf(
                                        TrackSegment(testInstrumentForNoteC4, 0.25f),
                                        TrackSegment(testInstrumentForNoteE4, 0.125f),
                                        TrackSegment(testInstrumentForNoteG4, 0.5f)),
                                        "Test track")),
                        volume = 1.0f),
                actual = testSong)
    }

    @Test
    fun chord() {
        val testSong = song(name = "Test song", beatsPerMinute = 240, volume = 1.0f) {
            track(name = "Test track", instrument = testInstrument) {
                chord(1 by 4, C4, E4, G4)
            }
        }

        with (testSong.tracks[0]) {
            assertEquals(1, segments.size)
            assertEquals(0.25f, segments[0].durationInSeconds)
            assertEquals(123.0f + 456.0f + 789.0f, segments[0].waveform(0.0f))
        }
    }

    @Test
    fun silence() {
        val testSong = song(name = "Test song", beatsPerMinute = 240, volume = 1.0f) {
            track(name = "Test track", instrument = testInstrument) {
                pause(1 by 4)
            }
        }

        assertEquals(
                expected = Song(
                        name = "Test song",
                        tracks = listOf(
                                Track(listOf(
                                        TrackSegment(it.krzeminski.fsynth.silence, 0.25f)),
                                        "Test track")),
                        volume = 1.0f),
                actual = testSong)
    }

    @Test
    fun severalTracks() {
        val testSong = song(name = "Test song", beatsPerMinute = 240, volume = 0.123f) {
            track(name = "Test track", instrument = testInstrument) {
                note(1 by 4, C4)
            }
            track(name = "Test track 2", instrument = testInstrument) {
                note(1 by 8, E4)
            }
        }

        assertEquals(
                expected = Song(
                        name = "Test song",
                        tracks = listOf(
                                Track(listOf(
                                        TrackSegment(testInstrumentForNoteC4, 0.25f)),
                                        "Test track"),
                                Track(listOf(
                                        TrackSegment(testInstrumentForNoteE4, 0.125f)),
                                        "Test track 2")),
                        volume = 0.123f),
                actual = testSong)
    }
}
