package it.krzeminski.fsynth.songs

import it.krzeminski.fsynth.effects.simpleDecayEnvelope
import it.krzeminski.fsynth.instruments.synthesizer
import it.krzeminski.fsynth.types.MusicNote.* // ktlint-disable no-wildcard-imports
import it.krzeminski.fsynth.types.by
import it.krzeminski.fsynth.types.to
import it.krzeminski.fsynth.types.song

val pinkPantherThemeIntro = song(name = "Pink Panther Theme (intro)", beatsPerMinute = 120) {
    track(name = "Main melody", instrument = ::synthesizer, volume = 0.15f) {
        pause(1 by 1)
        pause(1 by 1)
        pause(1 by 1)
        pause(1 by 1)

        pause(11 by 12)
        note(1 by 12, Dsharp4)

        note(5 by 12, E4)
        note(1 by 12, Fsharp4)
        note(5 by 12, G4)
        note(1 by 12, Dsharp4)

        note(2 by 12, E4)
        note(1 by 12, Fsharp4)
        note(2 by 12, G4)
        note(1 by 12, C5)
        note(2 by 12, B4)
        note(1 by 12, E4)
        note(2 by 12, G4)
        note(1 by 12, B4)

        glissando(7 by 12, A4 to Asharp4)
        glissando(1 by 12, G4 to A4)
        note(1 by 12, G4)
        note(1 by 12, E4)
        note(1 by 12, D4)
        note(10 by 12, E4)
        pause(2 by 12)
        note(1 by 12, Dsharp4)

        note(5 by 12, E4)
        note(1 by 12, Fsharp4)
        note(5 by 12, G4)
        note(1 by 12, Dsharp4)

        note(2 by 12, E4)
        note(1 by 12, Fsharp4)
        note(2 by 12, G4)
        note(1 by 12, C5)
        note(2 by 12, B4)
        note(1 by 12, G4)
        note(2 by 12, B4)
        note(1 by 12, E5)

        note(1 by 1, Dsharp5)

        pause(1 by 1)

        pause(3 by 12)
        glissando(2 by 12, D5 to E5)
        note(1 by 12, D5)
        glissando(2 by 12, A4 to B4)
        note(1 by 12, A4)
        note(2 by 12, G4)
        note(1 by 12, E4)

        glissando(3 by 12, Asharp4 to A4)
        glissando(3 by 12, Asharp4 to A4)
        glissando(3 by 12, Asharp4 to A4)
        glissando(3 by 12, Asharp4 to A4)

        note(1 by 12, G4)
        note(1 by 12, E4)
        note(1 by 12, D4)
        note(1 by 12, E4)
        pause(1 by 12)
        note(7 by 12, E4)
    }

    track(name = "Chord background", instrument = ::synthesizer, volume = 0.05f) {
        repeat(2) {
            pause(8 by 12)
            chord(1 by 12, Csharp3, Gsharp3)
            chord(2 by 12, D3, A3)
            chord(1 by 12, Dsharp3, Asharp3)

            chord(1 by 1, E3, B3)
        }

        repeat(2) {
            pause(8 by 12)
            chord(1 by 12, Csharp3, Gsharp3)
            chord(2 by 12, D3, A3)
            chord(1 by 12, Dsharp3, Asharp3)

            chord(1 by 1, E3, B3)
            pause(8 by 12)
            chord(1 by 12, E3, B3)
            chord(2 by 12, Dsharp3, Asharp3)
            chord(1 by 12, D3, A3)

            chord(1 by 1, Csharp3, Gsharp3)
        }

        pause(8 by 12)
        chord(1 by 12, Csharp3, Gsharp3)
        chord(2 by 12, D3, A3)
        chord(1 by 12, Dsharp3, Asharp3)

        chord(1 by 4, E3, B3)
        pause(3 by 4)

        pause(1 by 1)

        chord(1 by 1, E3, B3)
    }

    track(name = "Bass", instrument = simpleDecayEnvelope(::synthesizer), volume = 0.05f) {
        pause(1 by 1)

        repeat(2) {
            note(1 by 2, E3)
            note(1 by 2, B2)
            note(1 by 2, E2)
            pause(1 by 2)
        }

        repeat(2) {
            note(1 by 2, E3)
            note(1 by 2, B2)
            note(1 by 2, E2)
            pause(1 by 2)

            note(1 by 2, C3)
            note(1 by 2, G2)
            note(1 by 2, C2)
            pause(1 by 2)
        }
    }
}
