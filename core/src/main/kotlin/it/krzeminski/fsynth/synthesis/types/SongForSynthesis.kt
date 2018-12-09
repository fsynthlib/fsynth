package it.krzeminski.fsynth.synthesis.types

import it.krzeminski.fsynth.types.Waveform

data class SongForSynthesis(val tracks: List<TrackForSynthesis>)

data class TrackForSynthesis(val segments: List<TrackSegmentForSynthesis>, val volume: Float)

data class TrackSegmentForSynthesis(val waveform: Waveform, val durationInSeconds: Float)
