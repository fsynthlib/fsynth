package it.krzeminski.fsynth.instruments

import it.krzeminski.fsynth.effects.envelope.AdsrEnvelopeDefinition
import it.krzeminski.fsynth.effects.envelope.buildEnvelopeFunction
import it.krzeminski.fsynth.sineWave
import it.krzeminski.fsynth.types.plus
import it.krzeminski.fsynth.types.times

val organs = Instrument(
        waveform = { frequency: Frequency ->
            0.3 * sineWave(2 * frequency) +
            0.7 * sineWave(frequency) +
            0.2 * sineWave(0.5f * frequency)
        },
        envelope = buildEnvelopeFunction(
                AdsrEnvelopeDefinition(
                        attackTime = 0.0f,
                        decayTime = 0.0f,
                        sustainLevel = 1.0f,
                        releaseTime = 0.0f))
)
