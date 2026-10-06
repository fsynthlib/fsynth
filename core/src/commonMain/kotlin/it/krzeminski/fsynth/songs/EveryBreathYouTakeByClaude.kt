package it.krzeminski.fsynth.songs

import it.krzeminski.fsynth.effects.envelope.AdsrEnvelopeDefinition
import it.krzeminski.fsynth.effects.envelope.buildEnvelopeFunction
import it.krzeminski.fsynth.instruments.Frequency
import it.krzeminski.fsynth.instruments.Instrument
import it.krzeminski.fsynth.sineWave
import it.krzeminski.fsynth.squareWave
import it.krzeminski.fsynth.types.MusicNote
import it.krzeminski.fsynth.types.MusicNote.* // ktlint-disable no-wildcard-imports
import it.krzeminski.fsynth.types.Waveform
import it.krzeminski.fsynth.types.by
import it.krzeminski.fsynth.types.plus
import it.krzeminski.fsynth.types.song
import it.krzeminski.fsynth.types.times
import it.krzeminski.fsynth.types.to
import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

private val pluckedGuitar = Instrument(
        waveform = { frequency: Frequency ->
            0.6 * sineWave(frequency) +
            0.25 * sineWave(2 * frequency) +
            0.1 * sineWave(3 * frequency) +
            0.05 * squareWave(frequency)
        },
        // Long release lets the arpeggiated notes ring into each other.
        envelope = buildEnvelopeFunction(
                AdsrEnvelopeDefinition(
                        attackTime = 0.005f,
                        decayTime = 0.25f,
                        sustainLevel = 0.35f,
                        releaseTime = 0.6f))
)

private val bass = Instrument(
        waveform = { frequency: Frequency ->
            0.8 * sineWave(frequency) +
            0.15 * sineWave(2 * frequency) +
            0.05 * squareWave(frequency)
        },
        envelope = buildEnvelopeFunction(
                AdsrEnvelopeDefinition(
                        attackTime = 0.01f,
                        decayTime = 0.15f,
                        sustainLevel = 0.6f,
                        releaseTime = 0.04f))
)

private val lead = Instrument(
        waveform = { frequency: Frequency ->
            0.7 * vibratoSineWave(frequency) +
            0.2 * vibratoSineWave(2 * frequency) +
            0.1 * vibratoSineWave(3 * frequency)
        },
        envelope = buildEnvelopeFunction(
                AdsrEnvelopeDefinition(
                        attackTime = 0.03f,
                        decayTime = 0.1f,
                        sustainLevel = 0.8f,
                        releaseTime = 0.12f))
)

private const val VIBRATO_RATE = 5.5f
private const val VIBRATO_DEPTH = 0.006f

private fun vibratoSineWave(frequency: Float): Waveform = { t ->
    sin(2.0 * PI * frequency * t +
            VIBRATO_DEPTH * frequency / VIBRATO_RATE * sin(2.0 * PI * VIBRATO_RATE * t)).toFloat()
}

private val kickDrum = Instrument(
        waveform = { frequency: Frequency -> sineWave(frequency) },
        envelope = buildEnvelopeFunction(
                AdsrEnvelopeDefinition(
                        attackTime = 0.002f,
                        decayTime = 0.13f,
                        sustainLevel = 0.0f,
                        releaseTime = 0.001f))
)

private val snareDrum = Instrument(
        waveform = { frequency: Frequency -> whiteNoise() + 0.4 * sineWave(frequency) },
        envelope = buildEnvelopeFunction(
                AdsrEnvelopeDefinition(
                        attackTime = 0.001f,
                        decayTime = 0.12f,
                        sustainLevel = 0.0f,
                        releaseTime = 0.001f))
)

private val hiHat = Instrument(
        waveform = { _: Frequency -> whiteNoise() },
        envelope = buildEnvelopeFunction(
                AdsrEnvelopeDefinition(
                        attackTime = 0.001f,
                        decayTime = 0.04f,
                        sustainLevel = 0.0f,
                        releaseTime = 0.001f))
)

private fun whiteNoise(): Waveform = with(Random(0)) {
    { _ -> 2.0f * nextFloat() - 1.0f }
}

/**
 * An arrangement of the first part of the song (intro, two verses, bridge, third verse and a short outro),
 * in the original key of A flat major.
 * The guitar riff, bass line, chords and drum pattern are taken from a karaoke MIDI file, and the vocal melody is
 * taken from another MIDI file, where it's played by a pan flute.
 */
val everyBreathYouTakeByClaude = song(
        name = "The Police - Every Breath You Take (Claude's version)",
        beatsPerMinute = 115) {
    track(name = "Guitar", instrument = pluckedGuitar, volume = 0.13f) {
        // Andy Summers' add9 arpeggio: root, fifth, ninth, fifth, third (or octave), ninth, fifth, ninth.
        fun riff(vararg notes: MusicNote) = notes.forEach { note(1 by 8, it) }
        fun aFlat() = riff(Gsharp2, Dsharp3, Asharp3, Dsharp3, C4, Asharp3, Dsharp3, Asharp3)
        fun fMinor() = riff(F2, C3, G3, C3, Gsharp3, G3, C3, G3)
        fun dFlat() = riff(Csharp3, Gsharp3, Dsharp4, Csharp3, Csharp4, Gsharp3, Csharp3, Gsharp3)
        fun dFlat7() = riff(Csharp3, Gsharp3, Csharp4, Csharp3, B3, Gsharp3, Csharp3, Gsharp3)
        fun eFlat() = riff(Dsharp3, Asharp3, F4, Dsharp3, Dsharp4, Asharp3, Dsharp3, Asharp3)
        fun bFlat() = riff(Asharp2, F3, C4, F3, D4, C4, F3, C4)

        // Intro
        aFlat(); aFlat(); fMinor(); fMinor(); dFlat(); eFlat()
        // Verse 1
        aFlat(); aFlat(); aFlat(); aFlat(); fMinor(); fMinor(); dFlat(); eFlat(); fMinor(); fMinor()
        // Verse 2
        aFlat(); aFlat(); fMinor(); fMinor(); dFlat(); eFlat(); aFlat(); aFlat()
        // Bridge
        dFlat(); dFlat7(); aFlat(); aFlat(); bFlat(); bFlat(); eFlat(); eFlat()
        // Verse 3
        aFlat(); aFlat(); fMinor(); fMinor(); dFlat(); eFlat(); fMinor(); fMinor()
        // Outro
        aFlat(); aFlat()
        chord(1 by 1, Gsharp2, Dsharp3, Asharp3, C4)
    }

    track(name = "Bass", instrument = bass, volume = 0.22f) {
        fun bar(root: MusicNote, lastEighth: MusicNote = root) {
            repeat(7) { note(1 by 8, root) }
            note(1 by 8, lastEighth)
        }
        fun aFlat(lastEighth: MusicNote = Gsharp1) = bar(Gsharp1, lastEighth)
        fun fMinor() = bar(F1)
        fun dFlat() = bar(Csharp2)
        fun eFlat() = bar(Dsharp2)
        fun bFlat() = bar(Asharp1)
        fun fMinorTurnaround() {
            bar(F2)
            repeat(6) { note(1 by 8, F2) }
            note(1 by 8, Dsharp2)
            note(1 by 8, Dsharp2)
        }

        // Intro
        aFlat(); aFlat(); fMinor(); fMinor(); dFlat(); eFlat()
        // Verse 1
        aFlat(); aFlat(); aFlat(); aFlat(); fMinor(); fMinor(); dFlat(); eFlat(); fMinorTurnaround()
        // Verse 2
        aFlat(); aFlat(); fMinor(); fMinor(); dFlat(); eFlat(); aFlat(); aFlat(lastEighth = C2)
        // Bridge
        dFlat()
        note(1 by 8, Csharp2)
        repeat(6) { note(1 by 8, B1) }
        note(1 by 8, Csharp2)
        aFlat(); aFlat(); bFlat(); bFlat(); eFlat(); eFlat()
        // Verse 3
        aFlat(); aFlat(); fMinor(); fMinor(); bar(Csharp2, lastEighth = Dsharp2); eFlat(); fMinorTurnaround()
        // Outro
        aFlat(); aFlat()
        note(1 by 1, Gsharp1)
    }

    track(name = "Kick drum", instrument = kickDrum, volume = 0.4f) {
        repeat(42) {
            glissando(1 by 16, G2 to A0)
            pause(5 by 16)
            glissando(1 by 16, G2 to A0)
            pause(1 by 16)
            glissando(1 by 16, G2 to A0)
            pause(7 by 16)
        }
        glissando(1 by 16, G2 to A0)
    }

    track(name = "Snare drum", instrument = snareDrum, volume = 0.12f) {
        repeat(42) {
            pause(1 by 4)
            note(1 by 16, G3)
            pause(7 by 16)
            note(1 by 16, G3)
            pause(3 by 16)
        }
    }

    track(name = "Hi-hat", instrument = hiHat, volume = 0.04f) {
        // Only in the bridge.
        repeat(24) { pause(1 by 1) }
        repeat(8 * 8) {
            note(1 by 16, C4)
            pause(1 by 16)
        }
    }

    track(name = "Vocals", instrument = lead, volume = 0.16f) {
        // "Every breath you take", "every move you make", and so on - all lines start the same way.
        fun everyThingYouDo() {
            pause(1 by 4)
            note(1 by 8, C5)
            note(1 by 8, Csharp5)
            note(1 by 4, C5)
            note(1 by 8, Asharp4)
        }
        fun everyBondYouBreak() {
            pause(1 by 8)
            note(1 by 8, Gsharp4)
            note(1 by 8, Gsharp4)
            note(1 by 4, C5)
            note(1 by 4, Csharp5)
            note(1 by 8, Gsharp4)
        }
        fun everyStepYouTake() {
            pause(1 by 8)
            note(1 by 8, Gsharp4)
            note(1 by 8, Gsharp4)
            note(1 by 4, Csharp5)
            note(1 by 4, C5)
            note(1 by 8, Asharp4)
        }
        fun illBeWatchingYou() {
            pause(1 by 8)
            note(1 by 8, Asharp4)
            note(1 by 8, Gsharp4)
            note(1 by 8, C5)
            note(1 by 4, Gsharp4)
            note(1 by 8, F4)
            note(1 by 4, Dsharp4)
        }

        // Intro
        repeat(7) { pause(1 by 1) }

        // Verse 1
        everyThingYouDo()
        note(5 by 8, Gsharp4)
        pause(1 by 2)
        everyThingYouDo()
        note(1 by 4, Gsharp4)
        note(1 by 4, F4)
        pause(5 by 8)
        everyBondYouBreak()
        everyStepYouTake()
        illBeWatchingYou()
        pause(7 by 8)

        // Verse 2
        everyThingYouDo()
        note(1 by 4, Gsharp4)
        note(3 by 4, C5)
        pause(1 by 8)
        everyThingYouDo()
        note(1 by 4, Gsharp4)
        note(1 by 2, F4)
        pause(3 by 8)
        everyBondYouBreak()
        everyStepYouTake()
        illBeWatchingYou()
        pause(7 by 8)

        // Bridge: "Oh can't you see, you belong to me"
        pause(1 by 4)
        note(1 by 4, Gsharp4)
        note(1 by 4, C5)
        note(1 by 8, Dsharp5)
        note(1 by 4, Csharp5)
        note(7 by 8, Dsharp5)
        pause(1 by 4)
        note(1 by 8, F5)
        note(1 by 8, F5)
        note(3 by 8, Dsharp5)
        note(1 by 16, Asharp4)
        note(3 by 16, Gsharp4)
        note(7 by 8, C5)
        // "How my poor heart aches, with every step you take"
        pause(1 by 4)
        note(1 by 8, Fsharp5)
        note(1 by 8, Dsharp5)
        note(1 by 4, Gsharp5)
        note(1 by 8, Gsharp5)
        note(3 by 16, F5)
        note(1 by 8, Gsharp5)
        note(1 by 8, Dsharp5)
        note(3 by 4, F5)
        pause(3 by 16)
        note(1 by 4, Asharp5)
        note(1 by 8, G5)
        note(1 by 4, F5)
        note(9 by 8, Dsharp5)

        // Verse 3
        everyThingYouDo()
        note(9 by 8, Gsharp4)
        everyThingYouDo()
        note(1 by 4, Gsharp4)
        note(1 by 4, F4)
        pause(5 by 8)
        everyBondYouBreak()
        everyStepYouTake()
        illBeWatchingYou()
    }
}
