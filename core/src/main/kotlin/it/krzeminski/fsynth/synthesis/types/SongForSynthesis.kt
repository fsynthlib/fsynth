package it.krzeminski.fsynth.synthesis.types

import it.krzeminski.fsynth.types.BoundedWaveform

data class SongForSynthesis(val tracks: List<TrackForSynthesis>)

data class TrackForSynthesis(val segments: List<BoundedWaveform>, val volume: Float)
