package it.krzeminski.fsynth.instruments

import it.krzeminski.fsynth.effects.envelope.AdsrEnvelopeDefinition
import kotlin.random.Random

val cymbals = Instrument(
        waveform = { _: Frequency ->
            with(Random(0)) {
                { _ -> this.nextFloat() }
            }
        },
        envelope = AdsrEnvelopeDefinition(
                attackTime = 0.0f,
                decayTime = 0.0f,
                sustainLevel = 1.0f,
                releaseTime = 0.0f)
)
