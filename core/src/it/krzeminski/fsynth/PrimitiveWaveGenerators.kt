package it.krzeminski.fsynth

import it.krzeminski.fsynth.types.Waveform
import kotlin.math.PI
import kotlin.math.sin

val silence = { _: Float -> 0.0f }

fun sineWave(frequency: Float): Waveform = { t -> sin(frequency*t*2.0*PI).toFloat() }
