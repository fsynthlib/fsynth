package it.krzeminski.fsynth.instruments

import it.krzeminski.fsynth.types.Waveform
import kotlin.random.Random

fun cymbals(@Suppress("UNUSED_PARAMETER") frequencyUnused: Float): Waveform {
    with(Random(0)) {
        return { _ -> this.nextFloat() }
    }
}
