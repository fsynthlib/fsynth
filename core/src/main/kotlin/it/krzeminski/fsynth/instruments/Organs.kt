package it.krzeminski.fsynth.instruments

import it.krzeminski.fsynth.sineWave
import it.krzeminski.fsynth.types.MusicNote
import it.krzeminski.fsynth.types.Waveform
import it.krzeminski.fsynth.types.plus
import it.krzeminski.fsynth.types.times

fun organs(frequency: Float): Waveform =
        0.3*sineWave(2*frequency) +
        0.7*sineWave(frequency) +
        0.2*sineWave(0.5f*frequency)
