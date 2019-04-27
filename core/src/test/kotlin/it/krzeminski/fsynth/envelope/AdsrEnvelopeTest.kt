package it.krzeminski.fsynth.envelope

import it.krzeminski.fsynth.effects.envelope.AdsrEnvelopeDefinition
import it.krzeminski.fsynth.effects.envelope.adsrEnvelope
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
            row(        "                            IIII                                   ")
            row(        "  I                             III                                ")
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
            row(0.5f,   "   IIII                                ")
            row(        "       IIIIII                          ")
            row(        "  I          IIIII                     ")
            row(0.25f,  "                  IIIIIII              ")
            row(        " I                      IIIIII         ")
            row(        "                              IIIIIII  ")
            row(0.0f,   "X                                   III")
            xAxis {
                markers("|     |     |     |     |     |     |  ")
                values( 0.0f, 0.5f, 1.0f, 1.5f, 2.0f, 2.5f, 3.0f )
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
