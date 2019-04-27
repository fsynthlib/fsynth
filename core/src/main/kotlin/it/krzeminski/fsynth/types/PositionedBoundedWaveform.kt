package it.krzeminski.fsynth.types

data class PositionedBoundedWaveform(
    val boundedWaveform: BoundedWaveform,
    val startTime: Float
)

val PositionedBoundedWaveform.endTime: Float
    get() = startTime + boundedWaveform.duration
