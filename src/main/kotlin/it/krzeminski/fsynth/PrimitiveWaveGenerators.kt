package it.krzeminski.fsynth

import kotlin.math.PI
import kotlin.math.sin

fun sineWave(frequency: Float): (Float) -> Float = { t -> sin(frequency*t*2.0*PI).toFloat() }
