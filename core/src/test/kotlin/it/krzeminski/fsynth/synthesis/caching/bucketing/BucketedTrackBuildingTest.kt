package it.krzeminski.fsynth.synthesis.caching.bucketing

import it.krzeminski.fsynth.silence
import it.krzeminski.fsynth.synthesis.types.TrackForSynthesis
import it.krzeminski.fsynth.synthesis.types.TrackSegmentForSynthesis
import kotlin.test.Test
import kotlin.test.assertEquals

class BucketedTrackBuildingTest {
    @Test
    fun simpleTrack() {
        val segment1 = TrackSegmentForSynthesis(silence, 0.5f)
        val segment2 = TrackSegmentForSynthesis(silence, 0.5f)
        val segment3 = TrackSegmentForSynthesis(silence, 0.5f)
        val trackToBeCached = TrackForSynthesis(
                segments = listOf(segment1, segment2, segment3))

        val bucketedTrack = trackToBeCached.buildBucketedTrack(bucketSizeInSeconds = 1.0f)

        assertEquals(2, bucketedTrack.buckets.size)
        assertEquals(
                listOf(
                        PositionedTrackSegment(trackSegment = segment1, startTimeInSeconds = 0.0f),
                        PositionedTrackSegment(trackSegment = segment2, startTimeInSeconds = 0.5f),
                        PositionedTrackSegment(trackSegment = segment3, startTimeInSeconds = 1.0f)),
                bucketedTrack.buckets[0])
        assertEquals(
                listOf(
                        PositionedTrackSegment(trackSegment = segment2, startTimeInSeconds = 0.5f),
                        PositionedTrackSegment(trackSegment = segment3, startTimeInSeconds = 1.0f)),
                bucketedTrack.buckets[1])
    }

    @Test
    fun segmentSpanningOverThreeBuckets() {
        val segment1 = TrackSegmentForSynthesis(silence, 0.5f)
        val segment2 = TrackSegmentForSynthesis(silence, 2.0f)
        val trackToBeCached = TrackForSynthesis(
                segments = listOf(segment1, segment2))

        val bucketedTrack = trackToBeCached.buildBucketedTrack(bucketSizeInSeconds = 1.0f)

        assertEquals(3, bucketedTrack.buckets.size)
        assertEquals(
                listOf(
                        PositionedTrackSegment(trackSegment = segment1, startTimeInSeconds = 0.0f),
                        PositionedTrackSegment(trackSegment = segment2, startTimeInSeconds = 0.5f)),
                bucketedTrack.buckets[0])
        assertEquals(
                listOf(
                        PositionedTrackSegment(trackSegment = segment2, startTimeInSeconds = 0.5f)),
                bucketedTrack.buckets[1])
        assertEquals(
                listOf(
                        PositionedTrackSegment(trackSegment = segment2, startTimeInSeconds = 0.5f)),
                bucketedTrack.buckets[2])
    }

    @Test
    fun segmentLengthEqualToBucketLength() {
        val segment1 = TrackSegmentForSynthesis(silence, 1.0f)
        val trackToBeCached = TrackForSynthesis(
                segments = listOf(segment1))

        val bucketedTrack = trackToBeCached.buildBucketedTrack(bucketSizeInSeconds = 1.0f)

        assertEquals(1, bucketedTrack.buckets.size)
        assertEquals(
                listOf(
                        PositionedTrackSegment(trackSegment = segment1, startTimeInSeconds = 0.0f)),
                bucketedTrack.buckets[0])
    }
}
