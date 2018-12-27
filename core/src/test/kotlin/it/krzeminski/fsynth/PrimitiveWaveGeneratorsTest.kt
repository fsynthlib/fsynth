package it.krzeminski.fsynth

import it.krzeminski.testutils.plotassert.assertFunctionConformsTo
import kotlin.test.Test

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
                    row(1.0f,   "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXI                                        ")
                    row(        "                                        I                                        ")
                    row(        "                                        I                                        ")
                    row(        "                                        I                                        ")
                    row(        "                                        I                                        ")
                    row(0.0f,   "                                        I                                        ")
                    row(        "                                        I                                        ")
                    row(        "                                        I                                        ")
                    row(        "                                        I                                        ")
                    row(        "                                        I                                        ")
                    row(-1.0f,  "                                        IXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX")
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
                    row(1.0f,   "XXXXXXXXXXXXXXXXXXXXI                   IXXXXXXXXXXXXXXXXXXXI                    ")
                    row(        "                    I                   I                   I                    ")
                    row(        "                    I                   I                   I                    ")
                    row(        "                    I                   I                   I                    ")
                    row(        "                    I                   I                   I                    ")
                    row(0.0f,   "                    I                   I                   I                    ")
                    row(        "                    I                   I                   I                    ")
                    row(        "                    I                   I                   I                    ")
                    row(        "                    I                   I                   I                    ")
                    row(        "                    I                   I                   I                    ")
                    row(-1.0f,  "                    IXXXXXXXXXXXXXXXXXXXI                   IXXXXXXXXXXXXXXXXXXXX")
                    xAxis {
                        markers("|                   |                   |                   |                   |")
                        values( 0.0f,               0.25f,              0.5f,               0.75f,              1.0f)
                    }
                })
    }
}
