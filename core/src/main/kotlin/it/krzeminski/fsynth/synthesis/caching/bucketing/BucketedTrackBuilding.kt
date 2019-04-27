package it.krzeminski.fsynth.synthesis.caching.bucketing

import it.krzeminski.fsynth.synthesis.types.TrackForSynthesis
import it.krzeminski.fsynth.types.BoundedWaveform
import it.krzeminski.fsynth.types.PositionedBoundedWaveform
import it.krzeminski.fsynth.types.endTime
import kotlin.math.ceil

fun TrackForSynthesis.buildBucketedTrack(bucketSizeInSeconds: Float): BucketedTrack {
    val positionedBoundedWaveforms = positionedBoundedWaveformsFrom(segments)
    val numberOfBuckets = calculateNumberOfBuckets(bucketSizeInSeconds)

    val buckets = ArrayList(
            (0 until numberOfBuckets).asSequence()
                    .map { bucketIndex -> segmentsForBucket(positionedBoundedWaveforms, bucketIndex, bucketSizeInSeconds) }
                    .toList())

    return BucketedTrack(buckets, bucketSizeInSeconds, volume)
}

private fun positionedBoundedWaveformsFrom(segments: List<BoundedWaveform>): List<PositionedBoundedWaveform> {
    return segments.fold(emptyList()) { positionedBoundedWaveformsSoFar, currentTrackSegment ->
        val last = positionedBoundedWaveformsSoFar.lastOrNull()
        positionedBoundedWaveformsSoFar.plus(PositionedBoundedWaveform(currentTrackSegment, last?.endTime ?: 0.0f))
    }
}

private fun TrackForSynthesis.calculateNumberOfBuckets(bucketSizeInSeconds: Float) =
        ceil(durationInSeconds / bucketSizeInSeconds).toInt()

private val TrackForSynthesis.durationInSeconds: Float
    get() = this.segments.map { it.duration }.sum()

private fun segmentsForBucket(
    positionedBoundedWaveforms: List<PositionedBoundedWaveform>,
    bucketIndex: Int,
    bucketSizeInSeconds: Float
): List<PositionedBoundedWaveform>
{
    return positionedBoundedWaveforms
            .filter { segment -> segment.belongsToBucket(bucketIndex, bucketSizeInSeconds) }
}

private fun PositionedBoundedWaveform.belongsToBucket(bucketIndex: Int, bucketSizeInSeconds: Float): Boolean {
    val bucketBounds = makeBucketBounds(bucketIndex, bucketSizeInSeconds)
    return startTime inBoundsOf bucketBounds ||
            endTime inBoundsOf bucketBounds ||
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

private infix fun PositionedBoundedWaveform.spansOverWhole(bucketBounds: BucketBounds) =
        startTime <= bucketBounds.startTimeInSeconds && endTime >= bucketBounds.endTimeInSeconds
