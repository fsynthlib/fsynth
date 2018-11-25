package it.krzeminski.fsynth.synthesis

import it.krzeminski.fsynth.squareWave
import it.krzeminski.fsynth.types.Song
import it.krzeminski.fsynth.types.Track
import it.krzeminski.fsynth.types.TrackSegment
import it.krzeminski.testutils.plotassert.assertFunctionConformsTo
import kotlin.test.Test
import kotlin.test.assertEquals

class SynthesisTest {
    @Test
    fun singleTrackWithSingleSegment() {
        val testSong = Song(
                name = "Test song",
                tracks = listOf(
                        Track(listOf(
                                TrackSegment(squareWave(4.0f), 0.5f)),
                                "Test track")),
                volume = 1.0f)

        val songEvaluationFunction = testSong.buildSongEvaluationFunction()
        assertEquals(0.5f, testSong.durationInSeconds)
        assertFunctionConformsTo(songEvaluationFunction) {
            row(1.0f,   "XXXXXXXXI      IXXXXXXXI                                     ")
            row(0.0f,   "        I      I       I      IXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX")
            row(-1.0f,  "        IXXXXXXI       IXXXXXXI                              ")
            xAxis {
                markers("|                             |                             |")
                values( 0.0f,                         0.5f,                         1.0f)
            }
        }
    }

    @Test
    fun durationIsCorrectlyCalculated() {
        val testSong = Song(
                name = "Test song",
                tracks = listOf(
                        Track(listOf(
                                TrackSegment(squareWave(4.0f), 0.1f),
                                TrackSegment(squareWave(4.0f), 0.2f),
                                TrackSegment(squareWave(4.0f), 0.3f)),
                                "Test track"),
                        Track(listOf(
                                TrackSegment(squareWave(4.0f), 0.5f),
                                TrackSegment(squareWave(4.0f), 2.5f),
                                TrackSegment(squareWave(4.0f), 1.2f)),
                                "Test track"),
                        Track(listOf(
                                TrackSegment(squareWave(4.0f), 1.0f),
                                TrackSegment(squareWave(4.0f), 0.2f),
                                TrackSegment(squareWave(4.0f), 0.2f)),
                                "Test track")),
                volume = 1.0f)

        assertEquals(0.5f + 2.5f + 1.2f, testSong.durationInSeconds)
    }

    @Test
    fun durationForEmptySong() {
        val testSong = Song(
                name = "Test song",
                tracks = emptyList(),
                volume = 1.0f)

        assertEquals(0.0f, testSong.durationInSeconds)
    }

    @Test
    fun multipleTracksAreCorrectlySynthesized() {
        val testSong = Song(
                name = "Test song",
                tracks = listOf(
                        Track(listOf(
                                TrackSegment(squareWave(8.0f), 0.5f)),
                                "Test track"),
                        Track(listOf(
                                TrackSegment(squareWave(2.0f), 1.0f)),
                                "Test track")),
                volume = 0.5f)

        val songEvaluationFunction = testSong.buildSongEvaluationFunction()
        assertFunctionConformsTo(songEvaluationFunction) {
            row(1.0f,   "XXXXI   IXXXI                                                ")
            row(0.5f,   "    I   I   I                  IXXXXXXXXXXXXXXI              ")
            row(0.0f,   "    IXXXI   IXXXXXXI   IXXXI   I              I             I")
            row(-0.5f,  "                   I   I   I   I              IXXXXXXXXXXXXXI")
            row(-1.0f,  "                   IXXXI   IXXXI                             ")
            xAxis {
                markers("|                             |                             |")
                values( 0.0f,                         0.5f,                         1.0f)
            }
        }
    }

    @Test
    fun multipleTracksAreCorrectlySynthesizedForSongLengthBeyondBucketSize() {
        val testSong = Song(
                name = "Test song",
                tracks = listOf(
                        Track(listOf(
                                TrackSegment(squareWave(4.0f), 1.0f)),
                                "Test track"),
                        Track(listOf(
                                TrackSegment(squareWave(1.0f), 2.0f)),
                                "Test track")),
                volume = 0.5f)

        val songEvaluationFunction = testSong.buildSongEvaluationFunction()
        assertFunctionConformsTo(songEvaluationFunction) {
            row(1.0f,   "XXXXI   IXXXI                                                ")
            row(0.5f,   "    I   I   I                 IXXXXXXXXXXXXXXXI              ")
            row(0.0f,   "    IXXXI   IXXXXXXI   IXXXI  I               I             I")
            row(-0.5f,  "                   I   I   I  I               IXXXXXXXXXXXXXI")
            row(-1.0f,  "                   IXXXI   IXXI                              ")
            xAxis {
                markers("|              |              |              |              |")
                values( 0.0f,          0.5f,          1.0f,          1.5f,          2.0f)
            }
        }
    }
}
