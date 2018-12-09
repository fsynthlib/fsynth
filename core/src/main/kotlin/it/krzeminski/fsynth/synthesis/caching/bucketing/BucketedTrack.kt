package it.krzeminski.fsynth.synthesis.caching.bucketing

data class BucketedTrack(
        /**
         * 0th element of this list contains track segments that exist in the song in the time range
         * [0 s; [bucketSizeInSeconds]), 1st element: [[bucketSizeInSeconds], 2*[bucketSizeInSeconds]), and so on.
         *
         * In order for this bucketing strategy to make sense, this list must have random access in O(1), e.g. be an
         * [ArrayList]. [List] is used here only to improve immutability ([ArrayList] is mutable).
         */
        val buckets: List<TrackBucket>,
        val bucketSizeInSeconds: Float,
        val volume: Float)

typealias TrackBucket = List<PositionedTrackSegment>
