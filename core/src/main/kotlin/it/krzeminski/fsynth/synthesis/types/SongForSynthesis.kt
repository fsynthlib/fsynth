package it.krzeminski.fsynth.synthesis.types

import it.krzeminski.fsynth.types.PositionedBoundedWaveform

data class SongForSynthesis(val tracks: List<TrackForSynthesis>)

data class TrackForSynthesis(val segments: List<PositionedBoundedWaveform>, val volume: Float)
