package it.krzeminski.fsynth.effects.envelope

import it.krzeminski.testutils.plotassert.assertFunctionConformsTo
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

/* ktlint-disable no-multi-spaces paren-spacing */

class AdsrEnvelopeTest {
    @Test
    fun genericEnvelope() {
        val envelopeDefinition = AdsrEnvelopeDefinition(
                attackTime = 0.5f,
                decayTime = 2.5f,
                sustainLevel = 0.25f,
                releaseTime = 1.5f
        )

        val envelope = adsrEnvelope(keyPressDuration = 4.0f, definition = envelopeDefinition)

        assertEquals(actual = envelope.duration, expected = 5.5f)
        assertFunctionConformsTo(envelope.waveform) {
            row(1.0f,   "      XI                                                           ")
            row(        "        IIII                                                       ")
            row(        "     I      III                                                    ")
            row(0.75f,  "               III                                                 ")
            row(        "    I             IIII                                             ")
            row(        "                      III                                          ")
            row(0.5f,   "   I                     III                                       ")
            row(        "                            IIIi                                   ")
            row(        "  I                            iIII                                ")
            row(0.25f,  "                                   IXXXXXXXXXXXXXIII               ")
            row(        " I                                                  IIIII          ")
            row(        "                                                         IIIIII    ")
            row(0.0f,   "X                                                              IIIX")
            xAxis {
                markers("|     |     |     |     |     |     |     |     |     |     |     |")
                values( 0.0f, 0.5f, 1.0f, 1.5f, 2.0f, 2.5f, 3.0f, 3.5f, 4.0f, 4.5f, 5.0f, 5.5f)
            }
        }
    }

    @Test
    fun attackTimeIsZero() {
        val envelopeDefinition = AdsrEnvelopeDefinition(
                attackTime = 0.0f,
                decayTime = 2.5f,
                sustainLevel = 0.25f,
                releaseTime = 1.5f
        )

        val envelope = adsrEnvelope(keyPressDuration = 3.5f, definition = envelopeDefinition)

        assertEquals(actual = envelope.duration, expected = 5.0f)
        assertFunctionConformsTo(envelope.waveform) {
            row(1.0f,   "XI                                                           ")
            row(        "  IIII                                                       ")
            row(        "      III                                                    ")
            row(0.75f,  "         III                                                 ")
            row(        "            IIII                                             ")
            row(        "                III                                          ")
            row(0.5f,   "                   III                                       ")
            row(        "                      IIIi                                   ")
            row(        "                         iIII                                ")
            row(0.25f,  "                             IXXXXXXXXXXXXXIII               ")
            row(        "                                              IIIII          ")
            row(        "                                                   IIIIII    ")
            row(0.0f,   "                                                         IIIX")
            xAxis {
                markers("|     |     |     |     |     |     |     |     |     |     |")
                values( 0.0f, 0.5f, 1.0f, 1.5f, 2.0f, 2.5f, 3.0f, 3.5f, 4.0f, 4.5f, 5.0f)
            }
        }
    }

    @Test
    fun decayTimeIsZero() {
        val envelopeDefinition = AdsrEnvelopeDefinition(
                attackTime = 0.5f,
                decayTime = 0.0f,
                sustainLevel = 0.25f,
                releaseTime = 1.5f
        )

        val envelope = adsrEnvelope(keyPressDuration = 4.0f, definition = envelopeDefinition)

        assertEquals(actual = envelope.duration, expected = 5.5f)
        assertFunctionConformsTo(envelope.waveform) {
            row(1.0f,   "      X                                                            ")
            row(        "                                                                   ")
            row(        "     I                                                             ")
            row(0.75f,  "                                                                   ")
            row(        "    I                                                              ")
            row(        "                                                                   ")
            row(0.5f,   "   I                                                               ")
            row(        "                                                                   ")
            row(        "  I                                                                ")
            row(0.25f,  "       XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXIII               ")
            row(        " I                                                  IIIII          ")
            row(        "                                                         IIIIII    ")
            row(0.0f,   "X                                                              IIIX")
            xAxis {
                markers("|     |     |     |     |     |     |     |     |     |     |     |")
                values( 0.0f, 0.5f, 1.0f, 1.5f, 2.0f, 2.5f, 3.0f, 3.5f, 4.0f, 4.5f, 5.0f, 5.5f)
            }
        }
    }

    @Test
    fun sustainLevelIsZero() {
        val envelopeDefinition = AdsrEnvelopeDefinition(
                attackTime = 0.5f,
                decayTime = 2.5f,
                sustainLevel = 0.0f,
                releaseTime = 1.5f
        )

        val envelope = adsrEnvelope(keyPressDuration = 4.0f, definition = envelopeDefinition)

        assertEquals(actual = envelope.duration, expected = 4.0f)
        assertFunctionConformsTo(envelope.waveform) {
            row(1.0f,   "      XI                                         ")
            row(        "        II                                       ")
            row(        "     I    III                                    ")
            row(0.75f,  "             II                                  ")
            row(        "    I          III                               ")
            row(        "                  II                             ")
            row(0.5f,   "   I                III                          ")
            row(        "                       II                        ")
            row(        "  I                      III                     ")
            row(0.25f,  "                            II                   ")
            row(        " I                            III                ")
            row(        "                                 II              ")
            row(0.0f,   "X                                  IXXXXXXXXXXXXX")
            xAxis {
                markers("|     |     |     |     |     |     |     |     |")
                values( 0.0f, 0.5f, 1.0f, 1.5f, 2.0f, 2.5f, 3.0f, 3.5f, 4.0f)
            }
        }
    }

    @Test
    fun releaseTimeIsZero() {
        val envelopeDefinition = AdsrEnvelopeDefinition(
                attackTime = 0.5f,
                decayTime = 2.5f,
                sustainLevel = 0.25f,
                releaseTime = 0.0f
        )

        val envelope = adsrEnvelope(keyPressDuration = 4.0f, definition = envelopeDefinition)

        assertEquals(actual = envelope.duration, expected = 4.0f)
        assertFunctionConformsTo(envelope.waveform) {
            row(1.0f,   "      XI                                         ")
            row(        "        IIII                                     ")
            row(        "     I      III                                  ")
            row(0.75f,  "               III                               ")
            row(        "    I             IIII                           ")
            row(        "                      III                        ")
            row(0.5f,   "   I                     III                     ")
            row(        "                            IIIi                 ")
            row(        "  I                            iIII              ")
            row(0.25f,  "                                   IXXXXXXXXXXXXX")
            row(        " I                                               ")
            row(        "                                                 ")
            row(0.0f,   "X                                                ")
            xAxis {
                markers("|     |     |     |     |     |     |     |     |")
                values( 0.0f, 0.5f, 1.0f, 1.5f, 2.0f, 2.5f, 3.0f, 3.5f, 4.0f)
            }
        }
    }

    @Test
    fun attackAndDecayAreZero() {
        val envelopeDefinition = AdsrEnvelopeDefinition(
                attackTime = 0.0f,
                decayTime = 0.0f,
                sustainLevel = 0.25f,
                releaseTime = 1.5f
        )

        val envelope = adsrEnvelope(keyPressDuration = 4.0f, definition = envelopeDefinition)

        assertEquals(actual = envelope.duration, expected = 5.5f)
        assertFunctionConformsTo(envelope.waveform) {
            row(1.0f,   "                                                                   ")
            row(        "                                                                   ")
            row(        "                                                                   ")
            row(0.75f,  "                                                                   ")
            row(        "                                                                   ")
            row(        "                                                                   ")
            row(0.5f,   "                                                                   ")
            row(        "                                                                   ")
            row(        "                                                                   ")
            row(0.25f,  "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXIII               ")
            row(        "                                                    IIIII          ")
            row(        "                                                         IIIIII    ")
            row(0.0f,   "                                                               IIIX")
            xAxis {
                markers("|     |     |     |     |     |     |     |     |     |     |     |")
                values( 0.0f, 0.5f, 1.0f, 1.5f, 2.0f, 2.5f, 3.0f, 3.5f, 4.0f, 4.5f, 5.0f, 5.5f)
            }
        }
    }

    @Test
    fun decayAndSustainAreZero() {
        val envelopeDefinition = AdsrEnvelopeDefinition(
                attackTime = 0.5f,
                decayTime = 0.0f,
                sustainLevel = 0.0f,
                releaseTime = 1.5f
        )

        val envelope = adsrEnvelope(keyPressDuration = 4.0f, definition = envelopeDefinition)

        assertEquals(actual = envelope.duration, expected = 4.0f)
        assertFunctionConformsTo(envelope.waveform) {
            row(1.0f,   "      X                                          ")
            row(        "                                                 ")
            row(        "     I                                           ")
            row(0.75f,  "                                                 ")
            row(        "    I                                            ")
            row(        "                                                 ")
            row(0.5f,   "   I                                             ")
            row(        "                                                 ")
            row(        "  I                                              ")
            row(0.25f,  "                                                 ")
            row(        " I                                               ")
            row(        "                                                 ")
            row(0.0f,   "X      XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX")
            xAxis {
                markers("|     |     |     |     |     |     |     |     |")
                values( 0.0f, 0.5f, 1.0f, 1.5f, 2.0f, 2.5f, 3.0f, 3.5f, 4.0f)
            }
        }
    }

    @Test
    fun sustainAndReleaseAreZero() {
        val envelopeDefinition = AdsrEnvelopeDefinition(
                attackTime = 0.5f,
                decayTime = 2.5f,
                sustainLevel = 0.0f,
                releaseTime = 0.0f
        )

        val envelope = adsrEnvelope(keyPressDuration = 4.0f, definition = envelopeDefinition)

        assertEquals(actual = envelope.duration, expected = 4.0f)
        assertFunctionConformsTo(envelope.waveform) {
            row(1.0f,   "      XI                                         ")
            row(        "        II                                       ")
            row(        "     I    III                                    ")
            row(0.75f,  "             II                                  ")
            row(        "    I          III                               ")
            row(        "                  II                             ")
            row(0.5f,   "   I                III                          ")
            row(        "                       II                        ")
            row(        "  I                      III                     ")
            row(0.25f,  "                            II                   ")
            row(        " I                            III                ")
            row(        "                                 II              ")
            row(0.0f,   "X                                  IXXXXXXXXXXXXX")
            xAxis {
                markers("|     |     |     |     |     |     |     |     |")
                values( 0.0f, 0.5f, 1.0f, 1.5f, 2.0f, 2.5f, 3.0f, 3.5f, 4.0f)
            }
        }
    }

    @Test
    fun attackAndSustainAreZero() {
        val envelopeDefinition = AdsrEnvelopeDefinition(
                attackTime = 0.0f,
                decayTime = 2.5f,
                sustainLevel = 0.0f,
                releaseTime = 1.5f
        )

        val envelope = adsrEnvelope(keyPressDuration = 4.0f, definition = envelopeDefinition)

        assertEquals(actual = envelope.duration, expected = 4.0f)
        assertFunctionConformsTo(envelope.waveform) {
            row(1.0f,   "XI                                               ")
            row(        "  II                                             ")
            row(        "    III                                          ")
            row(0.75f,  "       II                                        ")
            row(        "         III                                     ")
            row(        "            II                                   ")
            row(0.5f,   "              III                                ")
            row(        "                 II                              ")
            row(        "                   III                           ")
            row(0.25f,  "                      II                         ")
            row(        "                        III                      ")
            row(        "                           II                    ")
            row(0.0f,   "                             IXXXXXXXXXXXXXXXXXXX")
            xAxis {
                markers("|     |     |     |     |     |     |     |     |")
                values( 0.0f, 0.5f, 1.0f, 1.5f, 2.0f, 2.5f, 3.0f, 3.5f, 4.0f)
            }
        }
    }

    @Test
    fun attackAndReleaseAreZero() {
        val envelopeDefinition = AdsrEnvelopeDefinition(
                attackTime = 0.0f,
                decayTime = 2.5f,
                sustainLevel = 0.25f,
                releaseTime = 0.0f
        )

        val envelope = adsrEnvelope(keyPressDuration = 4.0f, definition = envelopeDefinition)

        assertEquals(actual = envelope.duration, expected = 4.0f)
        assertFunctionConformsTo(envelope.waveform) {
            row(1.0f,   "XI                                               ")
            row(        "  IIII                                           ")
            row(        "      III                                        ")
            row(0.75f,  "         III                                     ")
            row(        "            IIII                                 ")
            row(        "                III                              ")
            row(0.5f,   "                   III                           ")
            row(        "                      IIIi                       ")
            row(        "                         iIII                    ")
            row(0.25f,  "                             IXXXXXXXXXXXXXXXXXXX")
            row(        "                                                 ")
            row(        "                                                 ")
            row(0.0f,   "                                                 ")
            xAxis {
                markers("|     |     |     |     |     |     |     |     |")
                values( 0.0f, 0.5f, 1.0f, 1.5f, 2.0f, 2.5f, 3.0f, 3.5f, 4.0f)
            }
        }
    }

    @Test
    fun decayAndReleaseAreZero() {
        val envelopeDefinition = AdsrEnvelopeDefinition(
                attackTime = 0.5f,
                decayTime = 0.0f,
                sustainLevel = 0.25f,
                releaseTime = 0.0f
        )

        val envelope = adsrEnvelope(keyPressDuration = 4.0f, definition = envelopeDefinition)

        assertEquals(actual = envelope.duration, expected = 4.0f)
        assertFunctionConformsTo(envelope.waveform) {
            row(1.0f,   "      X                                          ")
            row(        "                                                 ")
            row(        "     I                                           ")
            row(0.75f,  "                                                 ")
            row(        "    I                                            ")
            row(        "                                                 ")
            row(0.5f,   "   I                                             ")
            row(        "                                                 ")
            row(        "  I                                              ")
            row(0.25f,  "       XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX")
            row(        " I                                               ")
            row(        "                                                 ")
            row(0.0f,   "X                                                ")
            xAxis {
                markers("|     |     |     |     |     |     |     |     |")
                values( 0.0f, 0.5f, 1.0f, 1.5f, 2.0f, 2.5f, 3.0f, 3.5f, 4.0f)
            }
        }
    }

    @Test
    fun attackDecayAndSustainAreZero() {
        val envelopeDefinition = AdsrEnvelopeDefinition(
                attackTime = 0.0f,
                decayTime = 0.0f,
                sustainLevel = 0.0f,
                releaseTime = 1.5f
        )

        val envelope = adsrEnvelope(keyPressDuration = 4.0f, definition = envelopeDefinition)

        assertEquals(actual = envelope.duration, expected = 4.0f)
        assertFunctionConformsTo(envelope.waveform) {
            row(1.0f,   "                                                 ")
            row(        "                                                 ")
            row(        "                                                 ")
            row(0.75f,  "                                                 ")
            row(        "                                                 ")
            row(        "                                                 ")
            row(0.5f,   "                                                 ")
            row(        "                                                 ")
            row(        "                                                 ")
            row(0.25f,  "                                                 ")
            row(        "                                                 ")
            row(        "                                                 ")
            row(0.0f,   "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX")
            xAxis {
                markers("|     |     |     |     |     |     |     |     |")
                values( 0.0f, 0.5f, 1.0f, 1.5f, 2.0f, 2.5f, 3.0f, 3.5f, 4.0f)
            }
        }
    }

    @Test
    fun attackDecayAndReleaseAreZero() {
        val envelopeDefinition = AdsrEnvelopeDefinition(
                attackTime = 0.0f,
                decayTime = 0.0f,
                sustainLevel = 0.25f,
                releaseTime = 0.0f
        )

        val envelope = adsrEnvelope(keyPressDuration = 4.0f, definition = envelopeDefinition)

        assertEquals(actual = envelope.duration, expected = 4.0f)
        assertFunctionConformsTo(envelope.waveform) {
            row(1.0f,   "                                                 ")
            row(        "                                                 ")
            row(        "                                                 ")
            row(0.75f,  "                                                 ")
            row(        "                                                 ")
            row(        "                                                 ")
            row(0.5f,   "                                                 ")
            row(        "                                                 ")
            row(        "                                                 ")
            row(0.25f,  "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX")
            row(        "                                                 ")
            row(        "                                                 ")
            row(0.0f,   "                                                 ")
            xAxis {
                markers("|     |     |     |     |     |     |     |     |")
                values( 0.0f, 0.5f, 1.0f, 1.5f, 2.0f, 2.5f, 3.0f, 3.5f, 4.0f)
            }
        }
    }

    @Test
    fun attackSustainAndReleaseAreZero() {
        val envelopeDefinition = AdsrEnvelopeDefinition(
                attackTime = 0.0f,
                decayTime = 2.5f,
                sustainLevel = 0.0f,
                releaseTime = 0.0f
        )

        val envelope = adsrEnvelope(keyPressDuration = 4.0f, definition = envelopeDefinition)

        assertEquals(actual = envelope.duration, expected = 4.0f)
        assertFunctionConformsTo(envelope.waveform) {
            row(1.0f,   "XI                                               ")
            row(        "  II                                             ")
            row(        "    III                                          ")
            row(0.75f,  "       II                                        ")
            row(        "         III                                     ")
            row(        "            II                                   ")
            row(0.5f,   "              III                                ")
            row(        "                 II                              ")
            row(        "                   III                           ")
            row(0.25f,  "                      II                         ")
            row(        "                        III                      ")
            row(        "                           II                    ")
            row(0.0f,   "                             IXXXXXXXXXXXXXXXXXXX")
            xAxis {
                markers("|     |     |     |     |     |     |     |     |")
                values( 0.0f, 0.5f, 1.0f, 1.5f, 2.0f, 2.5f, 3.0f, 3.5f, 4.0f)
            }
        }
    }

    @Test
    fun decaySustainAndReleaseAreZero() {
        val envelopeDefinition = AdsrEnvelopeDefinition(
                attackTime = 0.5f,
                decayTime = 0.0f,
                sustainLevel = 0.0f,
                releaseTime = 0.0f
        )

        val envelope = adsrEnvelope(keyPressDuration = 4.0f, definition = envelopeDefinition)

        assertEquals(actual = envelope.duration, expected = 4.0f)
        assertFunctionConformsTo(envelope.waveform) {
            row(1.0f,   "      X                                          ")
            row(        "                                                 ")
            row(        "     I                                           ")
            row(0.75f,  "                                                 ")
            row(        "    I                                            ")
            row(        "                                                 ")
            row(0.5f,   "   I                                             ")
            row(        "                                                 ")
            row(        "  I                                              ")
            row(0.25f,  "                                                 ")
            row(        " I                                               ")
            row(        "                                                 ")
            row(0.0f,   "X      XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX")
            xAxis {
                markers("|     |     |     |     |     |     |     |     |")
                values( 0.0f, 0.5f, 1.0f, 1.5f, 2.0f, 2.5f, 3.0f, 3.5f, 4.0f)
            }
        }
    }

    @Test
    fun attackDecaySustainAndReleaseAreZero() {
        val envelopeDefinition = AdsrEnvelopeDefinition(
                attackTime = 0.0f,
                decayTime = 0.0f,
                sustainLevel = 0.0f,
                releaseTime = 0.0f
        )

        val envelope = adsrEnvelope(keyPressDuration = 4.0f, definition = envelopeDefinition)

        assertEquals(actual = envelope.duration, expected = 4.0f)
        assertFunctionConformsTo(envelope.waveform) {
            row(1.0f,   "                                                 ")
            row(        "                                                 ")
            row(        "                                                 ")
            row(0.75f,  "                                                 ")
            row(        "                                                 ")
            row(        "                                                 ")
            row(0.5f,   "                                                 ")
            row(        "                                                 ")
            row(        "                                                 ")
            row(0.25f,  "                                                 ")
            row(        "                                                 ")
            row(        "                                                 ")
            row(0.0f,   "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX")
            xAxis {
                markers("|     |     |     |     |     |     |     |     |")
                values( 0.0f, 0.5f, 1.0f, 1.5f, 2.0f, 2.5f, 3.0f, 3.5f, 4.0f)
            }
        }
    }

    @Test
    fun keyPressDurationShorterThanAttackTime() {
        val envelopeDefinition = AdsrEnvelopeDefinition(
                attackTime = 0.5f,
                decayTime = 2.5f,
                sustainLevel = 0.25f,
                releaseTime = 1.5f
        )

        val envelope = adsrEnvelope(keyPressDuration = 0.25f, definition = envelopeDefinition)

        assertEquals(actual = envelope.duration, expected = 3.25f)
        assertFunctionConformsTo(envelope.waveform) {
            row(0.5f,   "   IIII                                    ")
            row(        "       IIIIII                              ")
            row(        "  I          IIIII                         ")
            row(0.25f,  "                  IIIIIIi                  ")
            row(        " I                      iIIIII             ")
            row(        "                              IIIIIIi      ")
            row(0.0f,   "X                                   iII    ")
            xAxis {
                markers("|     |     |     |     |     |     |     |")
                values( 0.0f, 0.5f, 1.0f, 1.5f, 2.0f, 2.5f, 3.0f, 3.5f)
            }
        }
    }

    @Test
    fun keyPressDurationShorterThanAttackPlusDecayTime() {
        val envelopeDefinition = AdsrEnvelopeDefinition(
                attackTime = 0.5f,
                decayTime = 2.5f,
                sustainLevel = 0.25f,
                releaseTime = 1.5f
        )

        val envelope = adsrEnvelope(keyPressDuration = 2.0f, definition = envelopeDefinition)

        assertTrue(abs(envelope.duration - 5.3f) < 0.000001f, message = "Was: ${envelope.duration}")
        assertFunctionConformsTo(envelope.waveform) {
            row(1.0f,   "      XI                                                           ")
            row(        "        IIII                                                       ")
            row(        "     I      III                                                    ")
            row(0.75f,  "               III                                                 ")
            row(        "    I             IIII                                             ")
            row(        "                      III                                          ")
            row(0.5f,   "   I                     IIIIII                                    ")
            row(        "                               IIIIII                              ")
            row(        "  I                                  IIIIII                        ")
            row(0.25f,  "                                           IIIIII                  ")
            row(        " I                                               IIIIII            ")
            row(        "                                                       IIIIII      ")
            row(0.0f,   "X                                                            IIIIII")
            xAxis {
                markers("|     |     |     |     |     |     |     |     |     |     |     |")
                values( 0.0f, 0.5f, 1.0f, 1.5f, 2.0f, 2.5f, 3.0f, 3.5f, 4.0f, 4.5f, 5.0f, 5.5f)
            }
        }
    }

    @Test
    fun negativeKeyPressDuration() {
        val envelopeDefinition = AdsrEnvelopeDefinition(
                attackTime = 0.5f,
                decayTime = 2.5f,
                sustainLevel = 0.25f,
                releaseTime = 1.5f
        )

        assertFailsWith<IllegalArgumentException> {
            adsrEnvelope(keyPressDuration = -2.0f, definition = envelopeDefinition)
        }.let { e ->
            assertEquals(actual = e.message, expected = "Key press duration must not be negative!")
        }
    }

    @Test
    fun negativeEnvelopeParameters() {
        listOf(
                Pair(AdsrEnvelopeDefinition(-0.5f, 2.5f, 0.25f, 1.5f), "Attack time must not be negative!"),
                Pair(AdsrEnvelopeDefinition(0.5f, -2.5f, 0.25f, 1.5f), "Decay time must not be negative!"),
                Pair(AdsrEnvelopeDefinition(0.5f, 2.5f, -0.25f, 1.5f), "Sustain level must not be negative!"),
                Pair(AdsrEnvelopeDefinition(0.5f, 2.5f, 0.25f, -1.5f), "Release time must not be negative!")
        ).forEach { testcase ->
            assertFailsWith<IllegalArgumentException> {
                adsrEnvelope(keyPressDuration = 2.0f, definition = testcase.first)
            }.let { e ->
                assertEquals(actual = e.message, expected = testcase.second)
            }
        }
    }
}
