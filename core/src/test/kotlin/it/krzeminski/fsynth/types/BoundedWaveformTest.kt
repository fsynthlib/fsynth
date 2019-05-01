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
            row(1.0f,   "XXXXXXXI       IXXXXXXXI       I")
            row(-1.0f,  "       IXXXXXXXI       IXXXXXXXI")
            xAxis {
                markers("|                              |")
                values( 0.0f,                          1.0f)
            }
        }
        val boundedWaveform1 = BoundedWaveform(waveform1, 1.0f)
        val waveform2 = squareWave(4.0f)
        assertFunctionConformsTo(waveform2) {
            row(1.0f,   "XXXI   IXXXI   IXXXI   IXXXI   I")
            row(-1.0f,  "   IXXXI   IXXXI   IXXXI   IXXXI")
            xAxis {
                markers("|                              |")
                values( 0.0f,                          1.0f)
            }
        }
        val boundedWaveform2 = BoundedWaveform(waveform2, 1.0f)

        val aboveBoundedWaveformsAdded = boundedWaveform1 + boundedWaveform2

        assertEquals(1.0f, aboveBoundedWaveformsAdded.duration)
        assertFunctionConformsTo(aboveBoundedWaveformsAdded.waveform) {
            row(2.0f,   "XXXI           IXXXI           I")
            row(0.0f,   "   IXXXXXXXI   I   IXXXXXXXI   I")
            row(-2.0f,  "           IXXXI           IXXXI")
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
            row(1.0f,   "XXXXXXXI       IXXXXXXXI       I")
            row(-1.0f,  "       IXXXXXXXI       IXXXXXXXI")
            xAxis {
                markers("|                              |")
                values( 0.0f,                          1.0f)
            }
        }
        val boundedWaveform1 = BoundedWaveform(waveform1, 0.5f)
        val waveform2 = squareWave(4.0f)
        assertFunctionConformsTo(waveform2) {
            row(1.0f,   "XXXI   IXXXI   IXXXI   IXXXI   I")
            row(-1.0f,  "   IXXXI   IXXXI   IXXXI   IXXXI")
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
