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
import it.krzeminski.fsynth.types.endTime
import it.krzeminski.fsynth.types.plus
import kotlin.math.ln
import kotlin.math.pow

fun Song.preprocessForSynthesis() = SongForSynthesis(tracks = tracks.preprocess(this))

private fun List<Track>.preprocess(song: Song) = map { it.preprocess(song) }

private fun Track.preprocess(song: Song) =
        TrackForSynthesis(segments = segments.preprocess(song, this), volume = this.volume)

private fun List<TrackSegment>.preprocess(song: Song, track: Track) =
        map { it.toBoundedWaveform(song, track) }
                .addPositions()

private fun List<BoundedWaveform>.addPositions(): List<PositionedBoundedWaveform> {
    // An assumption is made here that the N-th BoundedWaveform's end is N+1-th BoundedWaveform's beginning.
    // It will change when BoundedWaveform's length is not equal to TrackSegment's length, which is the case for
    // envelopes, when the sound still plays after a note is released.
    return this.fold(emptyList()) { positionedBoundedWaveformsSoFar, currentTrackSegment ->
        val last = positionedBoundedWaveformsSoFar.lastOrNull()
        positionedBoundedWaveformsSoFar + PositionedBoundedWaveform(currentTrackSegment, last?.endTime ?: 0.0f)
    }
}

private fun TrackSegment.toBoundedWaveform(song: Song, track: Track): BoundedWaveform {
    when (this) {
        is TrackSegment.SingleNote -> {
            return BoundedWaveform(track.instrument.waveform(pitch.frequency), value.toSeconds(song.beatsPerMinute))
        }
        is TrackSegment.Glissando -> {
            return BoundedWaveform(
                    waveform = { t: Float ->
                        val stretchedTime = stretchTimeForGlissando(
                                transition.startPitch.midiNoteNumber,
                                transition.endPitch.midiNoteNumber,
                                value.toSeconds(song.beatsPerMinute),
                                t)
                        track.instrument.waveform(1.0f)(stretchedTime)
                    },
                    duration = value.toSeconds(song.beatsPerMinute))
        }
        is TrackSegment.Chord -> {
            return BoundedWaveform(
                    waveform = pitches
                            .map { it.frequency }
                            .map(track.instrument.waveform)
                            .reduce { accumulator, current -> accumulator + current },
                    duration = value.toSeconds(song.beatsPerMinute))
        }
        is TrackSegment.Pause -> {
            return BoundedWaveform(silence, value.toSeconds(song.beatsPerMinute))
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
