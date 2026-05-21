package it.krzeminski.fsynth.songs

import it.krzeminski.fsynth.instruments.organs
import it.krzeminski.fsynth.instruments.simpleDecayEnvelopeSynthesizer
import it.krzeminski.fsynth.instruments.synthesizer
import it.krzeminski.fsynth.types.MusicNote
import it.krzeminski.fsynth.types.MusicNote.* // ktlint-disable no-wildcard-imports
import it.krzeminski.fsynth.types.by
import it.krzeminski.fsynth.types.song

val everyBreathYouTake = song(
        name = "The Police - Every Breath You Take",
        beatsPerMinute = 116) {
    track(name = "Guitar", instrument = synthesizer, volume = 0.12f) {
        fun arpeggio(root: MusicNote, third: MusicNote, fifth: MusicNote, octave: MusicNote) {
            note(1 by 8, root)
            note(1 by 8, third)
            note(1 by 8, fifth)
            note(1 by 8, octave)
            note(1 by 8, fifth)
            note(1 by 8, third)
            note(1 by 8, root)
            note(1 by 8, third)
        }

        repeat(2) {
            arpeggio(A3, Csharp4, E4, A4)
            arpeggio(A3, Csharp4, E4, A4)
            arpeggio(Fsharp3, A3, Csharp4, Fsharp4)
            arpeggio(Fsharp3, A3, Csharp4, Fsharp4)
            arpeggio(D3, Fsharp3, A3, D4)
            arpeggio(E3, Gsharp3, B3, E4)
            arpeggio(A3, Csharp4, E4, A4)
            arpeggio(A3, Csharp4, E4, A4)
        }
    }

    track(name = "Bass", instrument = simpleDecayEnvelopeSynthesizer, volume = 0.2f) {
        repeat(2) {
            repeat(8) { note(1 by 8, A1) }
            note(1 by 8, A1)
            note(1 by 8, A1)
            note(1 by 8, A1)
            note(1 by 8, A1)
            note(1 by 8, A1)
            note(1 by 8, A1)
            note(1 by 8, A1)
            note(1 by 8, Csharp2)
            repeat(8) { note(1 by 8, Fsharp1) }
            repeat(8) { note(1 by 8, Fsharp1) }
            repeat(8) { note(1 by 8, D2) }
            repeat(8) { note(1 by 8, E2) }
            repeat(8) { note(1 by 8, A1) }
            note(1 by 8, A1)
            note(1 by 8, A1)
            note(1 by 8, A1)
            note(1 by 8, A1)
            note(1 by 8, A1)
            note(1 by 8, A1)
            note(1 by 8, A1)
            note(1 by 8, Csharp2)
        }
    }

    track(name = "Vocals", instrument = organs, volume = 0.18f) {
        // 8 bars intro (rest)
        repeat(8) { pause(1 by 1) }

        // Verse 1 melody from MIDI track 3, transposed from G to A major
        note(1 by 4, Csharp5)
        note(1 by 4, Csharp5)
        note(1 by 4, B4)
        note(5 by 4, A4)
        note(1 by 2, Csharp5)
        note(1 by 4, B4)
        note(9 by 8, A4)
        note(1 by 8, A4)
        note(1 by 8, A4)
        note(1 by 4, Csharp5)
        note(1 by 4, D5)
        note(1 by 4, A4)
        note(1 by 8, A4)
        note(1 by 8, A4)
        note(1 by 4, D5)
        note(1 by 4, Csharp5)
        note(1 by 4, B4)
        note(1 by 8, B4)
        note(1 by 8, A4)
        note(1 by 4, Csharp5)
        note(1 by 4, A4)
        note(5 by 4, A4)
    }
}
