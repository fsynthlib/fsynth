package it.krzeminski.fsynth.synthesis.caching.bucketing

import it.krzeminski.fsynth.synthesis.types.TrackForSynthesis
import it.krzeminski.fsynth.synthesis.types.TrackSegmentForSynthesis
import kotlin.math.ceil

fun TrackForSynthesis.buildBucketedTrack(bucketSizeInSeconds: Float): BucketedTrack {
    val positionedTrackSegments = positionedTrackSegmentsFrom(segments)
    val numberOfBuckets = calculateNumberOfBuckets(bucketSizeInSeconds)

    val buckets = ArrayList(
            (0 until numberOfBuckets).asSequence()
                    .map { bucketIndex -> segmentsForBucket(positionedTrackSegments, bucketIndex, bucketSizeInSeconds) }
                    .toList())

    return BucketedTrack(buckets, bucketSizeInSeconds, volume)
}

private fun positionedTrackSegmentsFrom(segments: List<TrackSegmentForSynthesis>): List<PositionedTrackSegment> {
    return segments.fold(emptyList()) { positionedTrackSegmentsSoFar, currentTrackSegment ->
        val last = positionedTrackSegmentsSoFar.lastOrNull()
        positionedTrackSegmentsSoFar.plus(PositionedTrackSegment(currentTrackSegment, last?.endTimeInSeconds ?: 0.0f))
    }
}

private fun TrackForSynthesis.calculateNumberOfBuckets(bucketSizeInSeconds: Float) =
        ceil(durationInSeconds / bucketSizeInSeconds).toInt()

private val TrackForSynthesis.durationInSeconds: Float
    get() = this.segments.map { it.durationInSeconds }.sum()

private fun segmentsForBucket(
    positionedTrackSegments: List<PositionedTrackSegment>,
    bucketIndex: Int,
    bucketSizeInSeconds: Float
): List<PositionedTrackSegment>
{
    return positionedTrackSegments
            .filter { segment -> segment.belongsToBucket(bucketIndex, bucketSizeInSeconds) }
}

private fun PositionedTrackSegment.belongsToBucket(bucketIndex: Int, bucketSizeInSeconds: Float): Boolean {
    val bucketBounds = makeBucketBounds(bucketIndex, bucketSizeInSeconds)
    return startTimeInSeconds inBoundsOf bucketBounds ||
            endTimeInSeconds inBoundsOf bucketBounds ||
            this spansOverWhole bucketBounds
}

private data class BucketBounds(
    val startTimeInSeconds: Float,
    val endTimeInSeconds: Float
)

private fun makeBucketBounds(bucketIndex: Int, bucketSizeInSeconds: Float): BucketBounds {
    val startTimeInSeconds = bucketSizeInSeconds * bucketIndex.toFloat()
    return BucketBounds(
            startTimeInSeconds = startTimeInSeconds,
            endTimeInSeconds = startTimeInSeconds + bucketSizeInSeconds)
}

private infix fun Float.inBoundsOf(bucketBounds: BucketBounds) =
        this >= bucketBounds.startTimeInSeconds && this <= bucketBounds.endTimeInSeconds

private infix fun PositionedTrackSegment.spansOverWhole(bucketBounds: BucketBounds) =
        startTimeInSeconds <= bucketBounds.startTimeInSeconds && endTimeInSeconds >= bucketBounds.endTimeInSeconds
