package it.krzeminski.fsynth.synthesis.caching.bucketing

import it.krzeminski.fsynth.types.BoundedWaveform

data class PositionedTrackSegment(
    val trackSegment: BoundedWaveform,
    val startTimeInSeconds: Float
)

val PositionedTrackSegment.endTimeInSeconds: Float
    get() = startTimeInSeconds + trackSegment.duration
