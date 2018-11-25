package it.krzeminski.fsynth.synthesis.types

import it.krzeminski.fsynth.types.Waveform

data class SongForSynthesis(val tracks: List<TrackForSynthesis>, val volume: Float)

data class TrackForSynthesis(val segments: List<TrackSegmentForSynthesis>)

data class TrackSegmentForSynthesis(val waveform: Waveform, val durationInSeconds: Float)
