package it.krzeminski.fsynth

import it.krzeminski.fsynth.types.Waveform
import kotlin.math.PI
import kotlin.math.sin

fun sineWave(frequency: Float): Waveform = { t -> sin(frequency*t*2.0*PI).toFloat() }
