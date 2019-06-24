package it.krzeminski.fsynth

import it.krzeminski.testutils.plotassert.assertFunctionConformsTo
import kotlin.test.Test

/* ktlint-disable no-multi-spaces paren-spacing */

class PrimitiveWaveGeneratorsTest {
    @Test
    fun sineWaveFor1Hz() {
        assertFunctionConformsTo(
                functionUnderTest = sineWave(1.0f),
                visualisation = {
                    row(1.0f,   "               IIIIIXIIIII                                                       ")
                    row(        "          IIIII           IIIII                                                  ")
                    row(        "       III                     III                                               ")
                    row(        "    III                           III                                            ")
                    row(        "  II                                 II                                          ")
                    row(0.0f,   "XI                                     III                                     II")
                    row(        "                                          II                                 II  ")
                    row(        "                                            III                           III    ")
                    row(        "                                               III                     III       ")
                    row(        "                                                  IIIII           IIIII          ")
                    row(-1.0f,  "                                                       IIIIIXIIIII               ")
                    xAxis {
                        markers("|                   |                   |                   |                   |")
                        values( 0.0f,               0.25f,              0.5f,               0.75f,              1.0f)
                    }
                })
    }

    @Test
    fun sineWaveFor2Hz() {
        assertFunctionConformsTo(
                functionUnderTest = sineWave(2.0f),
                visualisation = {
                    row(1.0f,   "        IIXII                                   IIIII                            ")
                    row(        "     III     III                             III     III                         ")
                    row(        "    I           I                           I           I                        ")
                    row(        "  II             II                       II             II                      ")
                    row(        " I                 I                     I                 I                     ")
                    row(0.0f,   "X                   I                   I                   I                   I")
                    row(        "                     I                 I                     I                 I ")
                    row(        "                      II             II                       II             II  ")
                    row(        "                        I           I                           I           I    ")
                    row(        "                         III     III                             III     III     ")
                    row(-1.0f,  "                            IIIII                                   IIXII        ")
                    xAxis {
                        markers("|                   |                   |                   |                   |")
                        values( 0.0f,               0.25f,              0.5f,               0.75f,              1.0f)
                    }
                })
    }

    @Test
    fun squareWaveFor1Hz() {
        assertFunctionConformsTo(
                functionUnderTest = squareWave(1.0f),
                visualisation = {
                    row(1.0f,   "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXi                                        ")
                    row(        "                                        i                                        ")
                    row(        "                                        i                                        ")
                    row(        "                                        i                                        ")
                    row(        "                                        i                                        ")
                    row(0.0f,   "                                        i                                        ")
                    row(        "                                        i                                        ")
                    row(        "                                        i                                        ")
                    row(        "                                        i                                        ")
                    row(        "                                        i                                        ")
                    row(-1.0f,  "                                        iXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX")
                    xAxis {
                        markers("|                   |                   |                   |                   |")
                        values( 0.0f,               0.25f,              0.5f,               0.75f,              1.0f)
                    }
                })
    }

    @Test
    fun squareWaveFor2Hz() {
        assertFunctionConformsTo(
                functionUnderTest = squareWave(2.0f),
                visualisation = {
                    row(1.0f,   "XXXXXXXXXXXXXXXXXXXXi                   iXXXXXXXXXXXXXXXXXXXi                    ")
                    row(        "                    i                   i                   i                    ")
                    row(        "                    i                   i                   i                    ")
                    row(        "                    i                   i                   i                    ")
                    row(        "                    i                   i                   i                    ")
                    row(0.0f,   "                    i                   i                   i                    ")
                    row(        "                    i                   i                   i                    ")
                    row(        "                    i                   i                   i                    ")
                    row(        "                    i                   i                   i                    ")
                    row(        "                    i                   i                   i                    ")
                    row(-1.0f,  "                    iXXXXXXXXXXXXXXXXXXXi                   iXXXXXXXXXXXXXXXXXXXX")
                    xAxis {
                        markers("|                   |                   |                   |                   |")
                        values( 0.0f,               0.25f,              0.5f,               0.75f,              1.0f)
                    }
                })
    }
}

/* ktlint-disable no-multi-spaces paren-spacing */
