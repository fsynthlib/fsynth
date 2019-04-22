package it.krzeminski.fsynth.instruments

import it.krzeminski.fsynth.sineWave
import it.krzeminski.fsynth.squareWave
import it.krzeminski.fsynth.types.Waveform
import it.krzeminski.fsynth.types.plus
import it.krzeminski.fsynth.types.times

fun synthesizer(frequency: Float): Waveform =
        0.3 * squareWave(frequency) +
        0.2 * squareWave(frequency*2) +
        0.5 * sineWave(frequency)
