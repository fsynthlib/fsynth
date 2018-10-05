package it.krzeminski.fsynth.songs

import it.krzeminski.fsynth.instruments.synthesizer
import it.krzeminski.fsynth.types.MusicNote.*
import it.krzeminski.fsynth.types.by
import it.krzeminski.fsynth.types.song

val vanHalenJumpIntro = song(
        name = "Van Halen - Jump (intro)",
        beatsPerMinute = 133,
        volume = 0.15f)
{
    track(name = "Main", instrument = ::synthesizer) {
        repeat(3) {
            pause(1 by 4)
            chord(1 by 8, G4, B4, D5)
            pause(1 by 4)
            chord(1 by 8, G4, C5, E5)
            pause(1 by 4)
            chord(1 by 8, F4, A4, C5)
            pause(1 by 4)
            chord(1 by 8, F4, A4, C5)
            pause(1 by 8)
            chord(1 by 8, G4, B4, D5)
            pause(1 by 8)
            chord(3 by 8, G4, B4, D5)
            chord(1 by 8, G4, C5, E5)
            pause(1 by 4)
            chord(1 by 8, F4, A4, C5)
            pause(1 by 8)
            chord(1 by 4, C4, F4, A4)
            chord(1 by 4, C4, E4, G4)
            chord(5 by 8, C4, D4, G4)
        }

        pause(1 by 4)
        repeat(4) {
            chord(1 by 8, F5, A5, C6)
            pause(1 by 4)
        }
        chord(1 by 4, C5, E5, G5)
        pause(1 by 4)
        chord(1 by 8, F4, A4, C5)
        pause(1 by 4)
        chord(1 by 8, F4, A4, C5)
        pause(1 by 8)
        chord(1 by 4, C4, F4, A4)
        chord(1 by 8, E4, G4)
        pause(1 by 8)
        chord(5 by 8, C4, D4, G4)
    }

    track(name = "Bass", instrument = ::synthesizer) {
        repeat(2) {
            chord(23 by 8, C2, C3)
            chord(1 by 2, F1, F2)
            chord(1 by 2, G1, G2)
            pause(1 by 8)
        }

        repeat(2) {
            repeat(23) {
                chord(1 by 16, C2, C3)
                pause(1 by 16)
            }

            repeat(4) {
                chord(1 by 16, F1, F2)
                pause(1 by 16)
            }
            repeat(5) {
                chord(1 by 16, G1, G2)
                pause(1 by 16)
            }
        }
    }
}
