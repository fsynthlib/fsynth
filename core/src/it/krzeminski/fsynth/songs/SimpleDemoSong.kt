package it.krzeminski.fsynth.songs

import it.krzeminski.fsynth.instruments.organs
import it.krzeminski.fsynth.types.MusicNote.*
import it.krzeminski.fsynth.types.by
import it.krzeminski.fsynth.types.song

// The "volume" parameter is needed to avoid generating values from outside <-1.0; 1.0>.
// TODO: Handle this issue properly, maybe compute maximum volume automatically and scale appropriately.
val simpleDemoSong = song(
        beatsPerMinute = 120,
        instrument=::organs,
        volume=0.3f)
{
    note(1 by 8, D4)
    note(1 by 16, Csharp4)
    note(1 by 16, D4)
    note(1 by 8, E4)
    note(1 by 8, D4)

    pause(1 by 8)
    chord(1 by 8, A3, D4, Fsharp4)
    chord(1 by 4, B3, D4, G4)
}
