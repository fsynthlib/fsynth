package it.krzeminski.fsynth.types

import it.krzeminski.fsynth.squareWave
import it.krzeminski.testutils.plotassert.assertFunctionConformsTo
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/* ktlint-disable no-multi-spaces paren-spacing */

class BoundedWaveformTest {
    @Test
    fun addingTwoBoundedWaveformsWithEqualDurations() {
        val waveform1 = squareWave(2.0f)
        assertFunctionConformsTo(waveform1) {
            row(1.0f,   "XXXXXXXi       iXXXXXXXi       i")
            row(-1.0f,  "       iXXXXXXXi       iXXXXXXXi")
            xAxis {
                markers("|                              |")
                values( 0.0f,                          1.0f)
            }
        }
        val boundedWaveform1 = BoundedWaveform(waveform1, 1.0f)
        val waveform2 = squareWave(4.0f)
        assertFunctionConformsTo(waveform2) {
            row(1.0f,   "XXXi   iXXXi   iXXXi   iXXXi   i")
            row(-1.0f,  "   iXXXi   iXXXi   iXXXi   iXXXi")
            xAxis {
                markers("|                              |")
                values( 0.0f,                          1.0f)
            }
        }
        val boundedWaveform2 = BoundedWaveform(waveform2, 1.0f)

        val aboveBoundedWaveformsAdded = boundedWaveform1 + boundedWaveform2

        assertEquals(1.0f, aboveBoundedWaveformsAdded.duration)
        assertFunctionConformsTo(aboveBoundedWaveformsAdded.waveform) {
            row(2.0f,   "XXXi           iXXXi           i")
            row(0.0f,   "   iXXXXXXXi   i   iXXXXXXXi   i")
            row(-2.0f,  "           iXXXi           iXXXi")
            xAxis {
                markers("|                              |")
                values( 0.0f,                          1.0f)
            }
        }
    }

    @Test
    fun addingTwoBoundedWaveformsWithDifferentDuration() {
        val waveform1 = squareWave(2.0f)
        assertFunctionConformsTo(waveform1) {
            row(1.0f,   "XXXXXXXi       iXXXXXXXi       i")
            row(-1.0f,  "       iXXXXXXXi       iXXXXXXXi")
            xAxis {
                markers("|                              |")
                values( 0.0f,                          1.0f)
            }
        }
        val boundedWaveform1 = BoundedWaveform(waveform1, 0.5f)
        val waveform2 = squareWave(4.0f)
        assertFunctionConformsTo(waveform2) {
            row(1.0f,   "XXXi   iXXXi   iXXXi   iXXXi   i")
            row(-1.0f,  "   iXXXi   iXXXi   iXXXi   iXXXi")
            xAxis {
                markers("|                              |")
                values( 0.0f,                          1.0f)
            }
        }
        val boundedWaveform2 = BoundedWaveform(waveform2, 1.0f)

        assertFailsWith<IllegalArgumentException> {
            boundedWaveform1 + boundedWaveform2
        }.let { e ->
            assertEquals("Adding waveforms with different durations is not supported!", e.message)
        }
    }
}

/* ktlint-disable no-multi-spaces paren-spacing */
