package it.krzeminski.fsynth.songs

import it.krzeminski.fsynth.instruments.organs
import it.krzeminski.fsynth.silence
import it.krzeminski.fsynth.types.MusicNote.*
import it.krzeminski.fsynth.types.Song
import it.krzeminski.fsynth.types.Waveform
import it.krzeminski.fsynth.types.plus
import it.krzeminski.fsynth.types.times

private val melody: Waveform = { time ->
    when (time) {
        in 0.0f..0.25f -> organs(D4)
        in 0.25f..0.375f -> organs(Csharp4)
        in 0.375f..0.5f -> organs(D4)
        in 0.5f..0.75f -> organs(E4)
        in 0.75f..1.0f -> organs(D4)
        in 1.25f..1.5f -> organs(A3) + organs(D4) + organs(Fsharp4)
        in 1.5f..2.0f -> organs(B3) + organs(D4) + organs(G4)
        else -> silence
    }(time)
}

// Needed to avoid generating values from outside <-1.0; 1.0>, effectively lowering the volume.
// TODO: Handle this issue properly.
private val simpleDemoSongWaveform = 0.3*melody

val simpleDemoSong = Song(waveform = simpleDemoSongWaveform, durationInSeconds = 2.0f)
