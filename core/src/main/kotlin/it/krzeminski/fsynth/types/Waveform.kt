package it.krzeminski.fsynth.types

typealias Waveform = (Float) -> Float

operator fun Waveform.plus(otherWaveform: Waveform): Waveform = { time -> this(time) + otherWaveform(time) }

operator fun Waveform.times(boundedWaveform: BoundedWaveform) =
        BoundedWaveform(
                waveform = { time -> this(time) * boundedWaveform.waveform(time) },
                duration = boundedWaveform.duration)

operator fun Double.times(waveform: Waveform): Waveform = { time -> this.toFloat() * waveform(time) }
