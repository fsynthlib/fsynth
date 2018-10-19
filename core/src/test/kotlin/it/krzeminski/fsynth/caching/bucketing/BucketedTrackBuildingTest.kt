package it.krzeminski.fsynth.caching.bucketing

import it.krzeminski.fsynth.silence
import it.krzeminski.fsynth.types.Track
import it.krzeminski.fsynth.types.TrackSegment
import kotlin.test.Test
import kotlin.test.assertEquals

class BucketedTrackBuildingTest {
    @Test
    fun simpleTrack() {
        val segment1 = TrackSegment(silence, 0.5f)
        val segment2 = TrackSegment(silence, 0.5f)
        val segment3 = TrackSegment(silence, 0.5f)
        val trackToBeCached = Track(
                segments = listOf(segment1, segment2, segment3),
                name = "Test track")

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
        val segment1 = TrackSegment(silence, 0.5f)
        val segment2 = TrackSegment(silence, 2.0f)
        val trackToBeCached = Track(
                segments = listOf(segment1, segment2),
                name = "Test track")

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
        val segment1 = TrackSegment(silence, 1.0f)
        val trackToBeCached = Track(
                segments = listOf(segment1),
                name = "Test track")

        val bucketedTrack = trackToBeCached.buildBucketedTrack(bucketSizeInSeconds = 1.0f)

        assertEquals(1, bucketedTrack.buckets.size)
        assertEquals(
                listOf(
                        PositionedTrackSegment(trackSegment = segment1, startTimeInSeconds = 0.0f)),
                bucketedTrack.buckets[0])
    }
}
