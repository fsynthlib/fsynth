package it.krzeminski.fsynth.types

import it.krzeminski.fsynth.squareWave
import it.krzeminski.testutils.plotassert.assertFunctionConformsTo
import kotlin.test.Test

class WaveformTest {
    @Test
    fun addingTwoWaveforms() {
        val waveform1 = squareWave(2.0f)
        assertFunctionConformsTo(waveform1) {
            row(1.0f,   "XXXXXXXI       IXXXXXXXI       I")
            row(-1.0f,  "       IXXXXXXXI       IXXXXXXXI")
            xAxis {
                markers("|                              |")
                values( 0.0f,                          1.0f)
            }
        }
        val waveform2 = squareWave(4.0f)
        assertFunctionConformsTo(waveform2) {
            row(1.0f,   "XXXI   IXXXI   IXXXI   IXXXI   I")
            row(-1.0f,  "   IXXXI   IXXXI   IXXXI   IXXXI")
            xAxis {
                markers("|                              |")
                values( 0.0f,                          1.0f)
            }
        }
        val aboveWaveformsAdded = waveform1 + waveform2
        assertFunctionConformsTo(aboveWaveformsAdded) {
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
    fun multiplyingWaveformByConstant() {
        val waveform = squareWave(2.0f)
        assertFunctionConformsTo(waveform) {
            row(1.0f,   "XXXXXXXI       IXXXXXXXI       I")
            row(-1.0f,  "       IXXXXXXXI       IXXXXXXXI")
            xAxis {
                markers("|                              |")
                values( 0.0f,                          1.0f)
            }
        }
        val aboveWaveformMultiplied = 3.0*waveform
        assertFunctionConformsTo(aboveWaveformMultiplied) {
            row(3.0f,   "XXXXXXXI       IXXXXXXXI       I")
            row(-3.0f,  "       IXXXXXXXI       IXXXXXXXI")
            xAxis {
                markers("|                              |")
                values( 0.0f,                          1.0f)
            }
        }
    }
}
