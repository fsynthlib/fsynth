package it.krzeminski.fsynth.caching.bucketing

import it.krzeminski.fsynth.types.TrackSegment

data class PositionedTrackSegment(
        val trackSegment: TrackSegment,
        val startTimeInSeconds: Float)

val PositionedTrackSegment.endTimeInSeconds: Float
    get() = startTimeInSeconds + trackSegment.durationInSeconds
