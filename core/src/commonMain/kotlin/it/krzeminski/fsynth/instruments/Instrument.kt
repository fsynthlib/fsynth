package it.krzeminski.fsynth.instruments

import it.krzeminski.fsynth.types.BoundedWaveform
import it.krzeminski.fsynth.types.Waveform

data class Instrument(
    val waveform: (Frequency) -> Waveform,
    val envelope: (KeyPressDuration) -> BoundedWaveform
)

typealias Frequency = Float
typealias KeyPressDuration = Float
