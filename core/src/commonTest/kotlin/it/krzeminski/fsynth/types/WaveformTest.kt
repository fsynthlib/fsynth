package it.krzeminski.fsynth.types

import it.krzeminski.fsynth.squareWave
import it.krzeminski.visassert.assertFunctionConformsTo
import kotlin.test.Test
import kotlin.test.assertEquals

/* ktlint-disable no-multi-spaces paren-spacing */

class WaveformTest {
    @Test
    fun addingTwoWaveforms() {
        val waveform1 = squareWave(2.0f)
        assertFunctionConformsTo(waveform1) {
            row(1.0f,   "XXXXXXXi       iXXXXXXXi       i")
            row(-1.0f,  "       iXXXXXXXi       iXXXXXXXi")
            xAxis {
                markers("|                              |")
                values( 0.0f,                          1.0f)
            }
        }
        val waveform2 = squareWave(4.0f)
        assertFunctionConformsTo(waveform2) {
            row(1.0f,   "XXXi   iXXXi   iXXXi   iXXXi   i")
            row(-1.0f,  "   iXXXi   iXXXi   iXXXi   iXXXi")
            xAxis {
                markers("|                              |")
                values( 0.0f,                          1.0f)
            }
        }
        val aboveWaveformsAdded = waveform1 + waveform2
        assertFunctionConformsTo(aboveWaveformsAdded) {
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
    fun multiplyingWaveformByConstant() {
        val waveform = squareWave(2.0f)
        assertFunctionConformsTo(waveform) {
            row(1.0f,   "XXXXXXXi       iXXXXXXXi       i")
            row(-1.0f,  "       iXXXXXXXi       iXXXXXXXi")
            xAxis {
                markers("|                              |")
                values( 0.0f,                          1.0f)
            }
        }
        val aboveWaveformMultiplied = 3.0 * waveform
        assertFunctionConformsTo(aboveWaveformMultiplied) {
            row(3.0f,   "XXXXXXXi       iXXXXXXXi       i")
            row(-3.0f,  "       iXXXXXXXi       iXXXXXXXi")
            xAxis {
                markers("|                              |")
                values( 0.0f,                          1.0f)
            }
        }
    }

    @Test
    fun multiplyingWaveformByBoundedWaveform() {
        val waveform = squareWave(2.0f)
        assertFunctionConformsTo(waveform) {
            row(1.0f,   "XXXXXXXi       iXXXXXXXi       i")
            row(        "       i       i       i       i")
            row(        "       i       i       i       i")
            row(        "       i       i       i       i")
            row(0.0f,   "       i       i       i       i")
            row(        "       i       i       i       i")
            row(        "       i       i       i       i")
            row(        "       i       i       i       i")
            row(-1.0f,  "       iXXXXXXXi       iXXXXXXXi")
            xAxis {
                markers("|              |               |")
                values( 0.0f,          0.5f,           1.0f)
            }
        }
        val waveformForBoundedWaveform = { x: Float -> -2.0f * x + 1.0f }
        assertFunctionConformsTo(waveformForBoundedWaveform) {
            row(1.0f,   "X               ")
            row(        " II             ")
            row(        "   II           ")
            row(        "     II         ")
            row(        "       II       ")
            row(        "         II     ")
            row(        "           II   ")
            row(        "             II ")
            row(0.0f,   "               X")
            xAxis {
                markers("|              |")
                values( 0.0f,          0.5f)
            }
        }
        val boundedWaveform = BoundedWaveform(waveformForBoundedWaveform, 0.5f)

        val aboveWaveformMultipliedByBoundedWaveform = waveform * boundedWaveform
        assertEquals(0.5f, aboveWaveformMultipliedByBoundedWaveform.duration)
        assertFunctionConformsTo(aboveWaveformMultipliedByBoundedWaveform.waveform) {
            row(1.0f,   "XI              ")
            row(        "  IIII          ")
            row(        "      IIi       ")
            row(        "        i       ")
            row(0.0f,   "        i     IX")
            row(        "        i IIII  ")
            row(        "        iI      ")
            row(        "                ")
            row(-1.0f,  "                ")
            xAxis {
                markers("|              |")
                values( 0.0f,          0.5f)
            }
        }
    }
}

/* ktlint-disable no-multi-spaces paren-spacing */
