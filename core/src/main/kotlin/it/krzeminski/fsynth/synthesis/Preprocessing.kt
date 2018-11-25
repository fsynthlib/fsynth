package it.krzeminski.fsynth.synthesis

import it.krzeminski.fsynth.silence
import it.krzeminski.fsynth.synthesis.types.SongForSynthesis
import it.krzeminski.fsynth.synthesis.types.TrackForSynthesis
import it.krzeminski.fsynth.synthesis.types.TrackSegmentForSynthesis
import it.krzeminski.fsynth.types.NoteValue
import it.krzeminski.fsynth.types.Song
import it.krzeminski.fsynth.types.Track
import it.krzeminski.fsynth.types.TrackSegment
import it.krzeminski.fsynth.types.plus

fun Song.preprocessForSynthesis() = SongForSynthesis(tracks = tracks.preprocess(this), volume = volume)

private fun List<Track>.preprocess(song: Song) = map { it.preprocess(song) }

private fun Track.preprocess(song: Song) = TrackForSynthesis(segments = segments.preprocess(song, this))

private fun List<TrackSegment>.preprocess(song: Song, track: Track) = map { it.preprocess(song, track) }

private fun TrackSegment.preprocess(song: Song, track: Track): TrackSegmentForSynthesis {
    when (this) {
        is TrackSegment.SingleNote -> {
            return TrackSegmentForSynthesis(track.instrument(pitch.frequency), value.toSeconds(song.beatsPerMinute))
        }
        is TrackSegment.Chord -> {
            return TrackSegmentForSynthesis(
                    waveform = pitches
                            .map { it.frequency }
                            .map(track.instrument)
                            .reduce { accumulator, current -> accumulator + current },
                    durationInSeconds = value.toSeconds(song.beatsPerMinute))
        }
        is TrackSegment.Pause -> {
            return TrackSegmentForSynthesis(silence, value.toSeconds(song.beatsPerMinute))
        }
    }
}

private fun NoteValue.toSeconds(beatsPerMinute: Int) =
        (this.numerator * BEATS_PER_BAR * SECONDS_IN_MINUTE).toFloat() / (beatsPerMinute * this.denominator).toFloat()

// Constant for now.
// TODO: generalize it (may be not always 4).
private const val BEATS_PER_BAR = 4
private const val SECONDS_IN_MINUTE = 60
