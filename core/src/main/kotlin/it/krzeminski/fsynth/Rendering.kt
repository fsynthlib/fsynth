package it.krzeminski.fsynth

import it.krzeminski.fsynth.types.Song

fun Song.renderWithSampleRate(sampleRate: Int): Sequence<Float> {
    val samplesToRender = (sampleRate.toFloat()*durationInSeconds).toInt()
    return (0..samplesToRender).asSequence()
            .map(sampleIndexToTimeInSeconds(sampleRate))
            .map(waveform)
}

private fun sampleIndexToTimeInSeconds(sampleRate: Int) =
        { sampleIndex: Int -> sampleIndex.toFloat()/sampleRate.toFloat() }
