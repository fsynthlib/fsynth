package it.krzeminski.fsynth.synthesis.caching.bucketing

import it.krzeminski.fsynth.silence
import it.krzeminski.fsynth.synthesis.types.TrackForSynthesis
import it.krzeminski.fsynth.types.BoundedWaveform
import it.krzeminski.fsynth.types.PositionedBoundedWaveform
import kotlin.test.Test
import kotlin.test.assertEquals

class BucketedTrackBuildingTest {
    @Test
    fun simpleTrack() {
        val segment1 = BoundedWaveform(silence, 0.5f)
        val segment2 = BoundedWaveform(silence, 0.5f)
        val segment3 = BoundedWaveform(silence, 0.5f)
        val trackToBeCached = TrackForSynthesis(
                segments = listOf(segment1, segment2, segment3),
                volume = 1.0f)

        val bucketedTrack = trackToBeCached.buildBucketedTrack(bucketSizeInSeconds = 1.0f)

        assertEquals(2, bucketedTrack.buckets.size)
        assertEquals(
                listOf(
                        PositionedBoundedWaveform(boundedWaveform = segment1, startTime = 0.0f),
                        PositionedBoundedWaveform(boundedWaveform = segment2, startTime = 0.5f),
                        PositionedBoundedWaveform(boundedWaveform = segment3, startTime = 1.0f)),
                bucketedTrack.buckets[0])
        assertEquals(
                listOf(
                        PositionedBoundedWaveform(boundedWaveform = segment2, startTime = 0.5f),
                        PositionedBoundedWaveform(boundedWaveform = segment3, startTime = 1.0f)),
                bucketedTrack.buckets[1])
    }

    @Test
    fun segmentSpanningOverThreeBuckets() {
        val segment1 = BoundedWaveform(silence, 0.5f)
        val segment2 = BoundedWaveform(silence, 2.0f)
        val trackToBeCached = TrackForSynthesis(
                segments = listOf(segment1, segment2),
                volume = 1.0f)

        val bucketedTrack = trackToBeCached.buildBucketedTrack(bucketSizeInSeconds = 1.0f)

        assertEquals(3, bucketedTrack.buckets.size)
        assertEquals(
                listOf(
                        PositionedBoundedWaveform(boundedWaveform = segment1, startTime = 0.0f),
                        PositionedBoundedWaveform(boundedWaveform = segment2, startTime = 0.5f)),
                bucketedTrack.buckets[0])
        assertEquals(
                listOf(
                        PositionedBoundedWaveform(boundedWaveform = segment2, startTime = 0.5f)),
                bucketedTrack.buckets[1])
        assertEquals(
                listOf(
                        PositionedBoundedWaveform(boundedWaveform = segment2, startTime = 0.5f)),
                bucketedTrack.buckets[2])
    }

    @Test
    fun segmentLengthEqualToBucketLength() {
        val segment1 = BoundedWaveform(silence, 1.0f)
        val trackToBeCached = TrackForSynthesis(
                segments = listOf(segment1),
                volume = 1.0f)

        val bucketedTrack = trackToBeCached.buildBucketedTrack(bucketSizeInSeconds = 1.0f)

        assertEquals(1, bucketedTrack.buckets.size)
        assertEquals(
                listOf(
                        PositionedBoundedWaveform(boundedWaveform = segment1, startTime = 0.0f)),
                bucketedTrack.buckets[0])
    }
}
