package it.krzeminski.fsynth.instruments

import it.krzeminski.fsynth.effects.envelope.AdsrEnvelopeDefinition
import it.krzeminski.fsynth.effects.simpleDecayEnvelope
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
        envelope = AdsrEnvelopeDefinition(
                attackTime = 0.0f,
                decayTime = 0.0f,
                sustainLevel = 1.0f,
                releaseTime = 0.0f)
)

val simpleDecayEnvelopeSynthesizer = Instrument(
        waveform = simpleDecayEnvelope { frequency: Frequency ->
            0.3 * squareWave(frequency) +
                    0.2 * squareWave(frequency*2) +
                    0.5 * sineWave(frequency)
        },
        envelope = AdsrEnvelopeDefinition(
                attackTime = 0.0f,
                decayTime = 0.0f,
                sustainLevel = 1.0f,
                releaseTime = 0.0f)
)
