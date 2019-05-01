package it.krzeminski.fsynth.synthesis

import it.krzeminski.fsynth.silence
import it.krzeminski.fsynth.synthesis.types.SongForSynthesis
import it.krzeminski.fsynth.synthesis.types.TrackForSynthesis
import it.krzeminski.fsynth.types.BoundedWaveform
import it.krzeminski.fsynth.types.NoteValue
import it.krzeminski.fsynth.types.PositionedBoundedWaveform
import it.krzeminski.fsynth.types.Song
import it.krzeminski.fsynth.types.Track
import it.krzeminski.fsynth.types.TrackSegment
import it.krzeminski.fsynth.types.plus
import it.krzeminski.fsynth.types.times
import kotlin.math.ln
import kotlin.math.pow

fun Song.preprocessForSynthesis() = SongForSynthesis(tracks = tracks.preprocess(this))

private fun List<Track>.preprocess(song: Song) = map { it.preprocess(song) }

private fun Track.preprocess(song: Song) =
        TrackForSynthesis(segments = segments.preprocess(song, this), volume = this.volume)

private fun List<TrackSegment>.preprocess(song: Song, track: Track) =
        preprocess(song, track, segmentsProcessed = emptyList(), segmentsToProcess = this, filledDuration = 0.0f)

private tailrec fun preprocess(
    song: Song,
    track: Track,
    segmentsProcessed: List<PositionedBoundedWaveform>,
    segmentsToProcess: List<TrackSegment>,
    filledDuration: Float
): List<PositionedBoundedWaveform> {
    if (segmentsToProcess.isEmpty())
        return segmentsProcessed

    // This implementation has memory complexity of O(n^2).
    // TODO: use data structures that allow O(n) complexity, in scope of #42.
    val (newBoundedWaveform, noteDuration) =
            segmentsToProcess.first().toBoundedWaveformWithNoteDuration(song, track)
    val newPositionedBoundedWaveform = PositionedBoundedWaveform(newBoundedWaveform, startTime = filledDuration)
    return preprocess(song, track, segmentsProcessed + newPositionedBoundedWaveform,
            segmentsToProcess.drop(1), filledDuration + noteDuration)
}

private fun TrackSegment.toBoundedWaveformWithNoteDuration(song: Song, track: Track): Pair<BoundedWaveform, Float> {
    when (this) {
        is TrackSegment.SingleNote -> {
            val noteDuration = value.toSeconds(song.beatsPerMinute)
            return Pair(track.instrument.waveform(pitch.frequency) *
                    track.instrument.envelope(noteDuration), noteDuration)
        }
        is TrackSegment.Glissando -> {
            val noteDuration = value.toSeconds(song.beatsPerMinute)
            return Pair(
                    { t: Float ->
                        val stretchedTime = stretchTimeForGlissando(
                                transition.startPitch.midiNoteNumber,
                                transition.endPitch.midiNoteNumber,
                                value.toSeconds(song.beatsPerMinute),
                                t)
                        track.instrument.waveform(1.0f)(stretchedTime)
                    } * track.instrument.envelope(noteDuration),
                    noteDuration)
        }
        is TrackSegment.Chord -> {
            val noteDuration = value.toSeconds(song.beatsPerMinute)
            return Pair(
                    (pitches
                            .map { it.frequency }
                            .map(track.instrument.waveform)
                            .reduce { accumulator, current -> accumulator + current }
                            ) * track.instrument.envelope(noteDuration),
                    noteDuration)
        }
        is TrackSegment.Pause -> {
            val noteDuration = value.toSeconds(song.beatsPerMinute)
            return Pair(BoundedWaveform(silence, noteDuration), noteDuration)
        }
    }
}

/**
 * This function calculates what time should be given to a 1-hertz instrument function to sound like glissando with the
 * given parameters. For example, if glissando should start at A4 and end at A5 (double the frequency of A4), the time
 * at the end of glissando (when [t] is near [durationInSeconds]) will flow twice as fast comparing to [t] being near 0.
 *
 * A detailed explanation of this formula can be found at
 * https://github.com/krzema12/fsynth/wiki/Glissando:-explanation-of-implementation
 */
private fun stretchTimeForGlissando(startNoteIndex: Int, endNoteIndex: Int, durationInSeconds: Float, t: Float): Float {
    val a = (endNoteIndex - startNoteIndex).toFloat() / (12.0f * durationInSeconds)
    val b = (startNoteIndex - 69).toFloat() / 12.0f
    val c = 440.0f
    return c * (2.0f.pow(a * t + b) - 2.0f.pow(b)) / (a * ln(2.0f))
}

private fun NoteValue.toSeconds(beatsPerMinute: Int) =
        (this.numerator * BEATS_PER_BAR * SECONDS_IN_MINUTE).toFloat() / (beatsPerMinute * this.denominator).toFloat()

// Constant for now.
// TODO: generalize it (may be not always 4).
private const val BEATS_PER_BAR = 4
private const val SECONDS_IN_MINUTE = 60
