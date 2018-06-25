package it.krzeminski.fsynth.songs

import it.krzeminski.fsynth.instruments.organs
import it.krzeminski.fsynth.types.MusicNote.*
import it.krzeminski.fsynth.types.song

// The "volume" parameter is needed to avoid generating values from outside <-1.0; 1.0>.
// TODO: Handle this issue properly, maybe compute maximum volume automatically and scale appropriately.
val simpleDemoSong = song(instrument=::organs, volume=0.3f) {
    note(0.25f, D4)
    note(0.125f, Csharp4)
    note(0.125f, D4)
    note(0.25f, E4)
    note(0.25f, D4)
    pause(0.25f)
    chord(0.25f, A3, D4, Fsharp4)
    chord(0.5f, B3, D4, G4)
}
