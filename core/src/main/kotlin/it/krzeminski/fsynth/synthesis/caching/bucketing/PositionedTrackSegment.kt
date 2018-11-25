package it.krzeminski.fsynth.synthesis.caching.bucketing

import it.krzeminski.fsynth.synthesis.types.TrackSegmentForSynthesis

data class PositionedTrackSegment(
        val trackSegment: TrackSegmentForSynthesis,
        val startTimeInSeconds: Float)

val PositionedTrackSegment.endTimeInSeconds: Float
    get() = startTimeInSeconds + trackSegment.durationInSeconds
