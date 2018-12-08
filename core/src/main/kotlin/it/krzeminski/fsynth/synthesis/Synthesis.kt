package it.krzeminski.fsynth.synthesis

import it.krzeminski.fsynth.synthesis.caching.bucketing.BucketedTrack
import it.krzeminski.fsynth.synthesis.caching.bucketing.PositionedTrackSegment
import it.krzeminski.fsynth.synthesis.caching.bucketing.buildBucketedTrack
import it.krzeminski.fsynth.synthesis.types.SongForSynthesis
import it.krzeminski.fsynth.synthesis.types.TrackForSynthesis
import it.krzeminski.fsynth.types.Song
import it.krzeminski.fsynth.types.Waveform

fun SongForSynthesis.buildSongEvaluationFunction(): Waveform {
    val bucketedTracks = tracks.buildBucketedTracks()
    return { t ->
        getSongWaveformValueCached(bucketedTracks, volume, t)
    }
}

val Song.durationInSeconds: Float
    get() = this.preprocessForSynthesis().durationInSeconds

val SongForSynthesis.durationInSeconds: Float
    get() = this.tracks.map { it.segments.map { it.durationInSeconds }.sum() }.max() ?: 0.0f

private fun List<TrackForSynthesis>.buildBucketedTracks(): List<BucketedTrack> =
        this.map { track ->
            buildCachedTrack(track)
        }

/**
 * This value is a trade-off between performance and memory consumption.
 * The larger the buckets are, the longer it takes to search through it for the right track segment.
 * The smaller the buckets are, the more memory they require.
 *
 * It has been empirically proven that going lower with this value (smaller buckets) doesn't help with synthesis
 * performance for generic songs. For some special cases, this value may have to be adjusted.
 */
private const val bucketSizeInSeconds = 1.0f

private fun buildCachedTrack(trackForSynthesis: TrackForSynthesis): BucketedTrack {
    return trackForSynthesis.buildBucketedTrack(bucketSizeInSeconds = bucketSizeInSeconds)
}

private fun getSongWaveformValueCached(bucketedTracks: List<BucketedTrack>, volume: Float, time: Float): Float =
        bucketedTracks.map { it.getWaveformValue(time) }.sum()*volume

private fun BucketedTrack.getWaveformValue(time: Float): Float {
    val whichBucket = (time / bucketSizeInSeconds).toInt()

    if (!containsBucketWithIndex(whichBucket))
        return 0.0f

    for (segmentInBucket in buckets[whichBucket]) {
        if (segmentInBucket playsFor time) {
            val timeRelativeToThisSegment = time - segmentInBucket.startTimeInSeconds
            return segmentInBucket.trackSegment.waveform(timeRelativeToThisSegment)
        }
    }

    return 0.0f
}

private infix fun PositionedTrackSegment.playsFor(time: Float) =
        time >= startTimeInSeconds && time <= (startTimeInSeconds + trackSegment.durationInSeconds)

private fun BucketedTrack.containsBucketWithIndex(whichBucket: Int) =
        whichBucket < buckets.size