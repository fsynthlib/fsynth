package it.krzeminski.fsynth.types

typealias Waveform = (Float) -> Float

operator fun Waveform.plus(otherWaveform: Waveform): Waveform = { time -> this(time) + otherWaveform(time) }

operator fun Double.times(waveform: Waveform): Waveform = { time -> this.toFloat() * waveform(time) }
