package it.krzeminski.fsynth

import it.krzeminski.fsynth.synthesis.types.SongForSynthesis
import it.krzeminski.fsynth.synthesis.types.TrackForSynthesis
import it.krzeminski.fsynth.synthesis.types.TrackSegmentForSynthesis
import it.krzeminski.testutils.plotassert.assertFunctionConformsTo
import kotlin.test.Test
import kotlin.test.assertEquals

/* ktlint-disable no-multi-spaces paren-spacing */

class RenderingTest {
    @Test
    fun rendersSongCorrectly() {
        val testSong = SongForSynthesis(
                tracks = listOf(
                        TrackForSynthesis(
                                segments = listOf(
                                        TrackSegmentForSynthesis(sineWave(1.0f), 3.0f)),
                                volume = 1.0f)))

        val songSamples = testSong.renderWithSampleRate(8).toList().toFloatArray()

        assertEquals(25, songSamples.size)
        val songSamplesAsAssertableFunction = { t: Float -> songSamples[t.toInt()] }
        assertFunctionConformsTo(
                functionUnderTest = songSamplesAsAssertableFunction,
                visualisation = {
                    row(1.0f,   "  I       I       I      ")
                    row(        " I I     I I     I I     ")
                    row(        "                         ")
                    row(        "                         ")
                    row(        "                         ")
                    row(0.0f,   "X   I   I   I   I   I   I")
                    row(        "                         ")
                    row(        "                         ")
                    row(        "                         ")
                    row(        "     I I     I I     I I ")
                    row(-1.0f,  "      I       I       I  ")
                    xAxis {
                        markers("|         |         |   |")
                        values( 0.0f,    10.0f,   20.0f, 24.0f)
                    }
                })
    }
}

/* ktlint-disable no-multi-spaces paren-spacing */
