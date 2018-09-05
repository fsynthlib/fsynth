package it.krzeminski.fsynth

import it.krzeminski.fsynth.types.Song
import it.krzeminski.fsynth.types.Track
import it.krzeminski.fsynth.types.Waveform

val Song.waveform: Waveform
    get() = ::getSongWaveformValue

val Song.durationInSeconds: Float
    get() = tracks.map { it.segments.map { it.durationInSeconds }.sum() }.max() ?: 0.0f

private fun Song.getSongWaveformValue(time: Float): Float =
        tracks.map { it.getWaveformValue(time) }.sum()*volume

private fun Track.getWaveformValue(time: Float): Float {
    var cumulatedDuration = 0.0f
    for (segment in segments) {
        if (time in cumulatedDuration..(cumulatedDuration + segment.durationInSeconds)) {
            return segment.waveform(time - cumulatedDuration)
        }
        cumulatedDuration += segment.durationInSeconds
    }
    return 0.0f
}
