package it.krzeminski.fsynth.effects.envelope

import it.krzeminski.fsynth.types.BoundedWaveform

/**
 * Describes parameters of an Attack-Decay-Sustain-Release envelope.
 * All time parameters are given in seconds.
 * @see <a href="https://en.wikipedia.org/wiki/Envelope_(music)#ADSR">Wikipedia, Envelope (music)#ADSR</a>.
 */
data class AdsrEnvelopeDefinition(
    val attackTime: Float,
    val decayTime: Float,
    val sustainLevel: Float,
    val releaseTime: Float
)

fun adsrEnvelope(keyPressDuration: Float, definition: AdsrEnvelopeDefinition): BoundedWaveform {
    with(definition) {
        require(keyPressDuration >= 0.0f) { "Key press duration must not be negative!" }
        require(attackTime >= 0.0f) { "Attack time must not be negative!" }
        require(decayTime >= 0.0f) { "Decay time must not be negative!" }
        require(sustainLevel >= 0.0f) { "Sustain level must not be negative!" }
        require(releaseTime >= 0.0f) { "Release time must not be negative!" }

        fun envelopeForPressedKey(t: Float): Float {
            return when (t) {
                in 0.0f..attackTime -> t / attackTime
                in attackTime..(attackTime + decayTime) -> 1.0f - (1.0f - sustainLevel)*(t - attackTime) / decayTime
                else -> sustainLevel
            }
        }
        val envelopeAtKeyRelease = envelopeForPressedKey(keyPressDuration)
        val actualReleaseTime = releaseTime * envelopeAtKeyRelease / sustainLevel
        val waveform = { t: Float ->
            when (t) {
                in 0.0f..keyPressDuration -> envelopeForPressedKey(t)
                else -> envelopeAtKeyRelease - sustainLevel*(t - keyPressDuration) / releaseTime
            }
        }
        return BoundedWaveform(waveform, keyPressDuration + actualReleaseTime)
    }
}
