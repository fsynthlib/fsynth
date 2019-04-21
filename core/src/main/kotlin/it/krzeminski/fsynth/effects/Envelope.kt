package it.krzeminski.fsynth.effects

import it.krzeminski.fsynth.types.Waveform

fun simpleDecayEnvelope(instrument: (Float) -> Waveform): (Float) -> Waveform = { frequency: Float ->
    { t: Float ->
        val baseValue = instrument(frequency)(t)
        baseValue / (t + 1)
    }
}
