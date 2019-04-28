package it.krzeminski.fsynth.instruments

import it.krzeminski.fsynth.effects.envelope.AdsrEnvelopeDefinition
import it.krzeminski.fsynth.types.Waveform

data class Instrument(
    val waveform: (Frequency) -> Waveform,
    val envelope: AdsrEnvelopeDefinition
)

typealias Frequency = Float
