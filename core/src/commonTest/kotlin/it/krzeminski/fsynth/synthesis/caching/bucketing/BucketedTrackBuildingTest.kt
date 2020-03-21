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
        val segment1 = PositionedBoundedWaveform(BoundedWaveform(silence, 0.5f), 0.0f)
        val segment2 = PositionedBoundedWaveform(BoundedWaveform(silence, 0.5f), 0.5f)
        val segment3 = PositionedBoundedWaveform(BoundedWaveform(silence, 0.5f), 1.0f)
        val trackToBeCached = TrackForSynthesis(
                segments = listOf(segment1, segment2, segment3),
                volume = 1.0f)

        val bucketedTrack = trackToBeCached.buildBucketedTrack(bucketSizeInSeconds = 1.0f)

        assertEquals(2, bucketedTrack.buckets.size)
        assertEquals(
                listOf(segment1, segment2, segment3),
                bucketedTrack.buckets[0])
        assertEquals(
                listOf(segment2, segment3),
                bucketedTrack.buckets[1])
    }

    @Test
    fun segmentSpanningOverThreeBuckets() {
        val segment1 = PositionedBoundedWaveform(BoundedWaveform(silence, 0.5f), 0.0f)
        val segment2 = PositionedBoundedWaveform(BoundedWaveform(silence, 2.0f), 0.5f)
        val trackToBeCached = TrackForSynthesis(
                segments = listOf(segment1, segment2),
                volume = 1.0f)

        val bucketedTrack = trackToBeCached.buildBucketedTrack(bucketSizeInSeconds = 1.0f)

        assertEquals(3, bucketedTrack.buckets.size)
        assertEquals(
                listOf(segment1, segment2),
                bucketedTrack.buckets[0])
        assertEquals(
                listOf(segment2),
                bucketedTrack.buckets[1])
        assertEquals(
                listOf(segment2),
                bucketedTrack.buckets[2])
    }

    @Test
    fun overlappingSegments() {
        val segment1 = PositionedBoundedWaveform(BoundedWaveform(silence, 0.5f), 0.0f)
        val segment2 = PositionedBoundedWaveform(BoundedWaveform(silence, 0.5f), 0.25f)
        val segment3 = PositionedBoundedWaveform(BoundedWaveform(silence, 0.5f), 0.5f)
        val segment4 = PositionedBoundedWaveform(BoundedWaveform(silence, 0.5f), 0.75f)
        val segment5 = PositionedBoundedWaveform(BoundedWaveform(silence, 0.5f), 1.0f)
        val trackToBeCached = TrackForSynthesis(
                segments = listOf(segment1, segment2, segment3, segment4, segment5),
                volume = 1.0f)

        val bucketedTrack = trackToBeCached.buildBucketedTrack(bucketSizeInSeconds = 1.0f)

        assertEquals(2, bucketedTrack.buckets.size)
        assertEquals(
                listOf(segment1, segment2, segment3, segment4, segment5),
                bucketedTrack.buckets[0])
        assertEquals(
                listOf(segment3, segment4, segment5),
                bucketedTrack.buckets[1])
    }

    @Test
    fun segmentLengthEqualToBucketLength() {
        val segment1 = PositionedBoundedWaveform(BoundedWaveform(silence, 1.0f), 0.0f)
        val trackToBeCached = TrackForSynthesis(
                segments = listOf(segment1),
                volume = 1.0f)

        val bucketedTrack = trackToBeCached.buildBucketedTrack(bucketSizeInSeconds = 1.0f)

        assertEquals(1, bucketedTrack.buckets.size)
        assertEquals(
                listOf(segment1),
                bucketedTrack.buckets[0])
    }
}
