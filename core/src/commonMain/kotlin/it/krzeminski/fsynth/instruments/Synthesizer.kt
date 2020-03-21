package it.krzeminski.fsynth.instruments

import it.krzeminski.fsynth.effects.envelope.AdsrEnvelopeDefinition
import it.krzeminski.fsynth.effects.envelope.buildEnvelopeFunction
import it.krzeminski.fsynth.sineWave
import it.krzeminski.fsynth.squareWave
import it.krzeminski.fsynth.types.plus
import it.krzeminski.fsynth.types.times

val synthesizer = Instrument(
        waveform = { frequency: Frequency ->
            0.3 * squareWave(frequency) +
            0.2 * squareWave(frequency*2) +
            0.5 * sineWave(frequency)
        },
        envelope = buildEnvelopeFunction(
                AdsrEnvelopeDefinition(
                        attackTime = 0.05f,
                        decayTime = 0.0f,
                        sustainLevel = 1.0f,
                        releaseTime = 0.01f))
)

val simpleDecayEnvelopeSynthesizer = synthesizer.copy(
        envelope = buildEnvelopeFunction(
                AdsrEnvelopeDefinition(
                        attackTime = 0.05f,
                        decayTime = 5.0f,
                        sustainLevel = 0.2f,
                        releaseTime = 0.005f))
)
