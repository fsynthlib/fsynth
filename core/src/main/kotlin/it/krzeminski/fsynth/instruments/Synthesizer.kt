package it.krzeminski.fsynth.instruments

import it.krzeminski.fsynth.sineWave
import it.krzeminski.fsynth.squareWave
import it.krzeminski.fsynth.types.MusicNote
import it.krzeminski.fsynth.types.Waveform
import it.krzeminski.fsynth.types.plus
import it.krzeminski.fsynth.types.times

fun synthesizer(note: MusicNote): Waveform = { t ->
    val baseValue = (
            0.3*squareWave(note.frequency) +
            0.2*squareWave(note.frequency*2) +
            0.5*sineWave(note.frequency)
        )(t)
    baseValue/(t + 1)
}
