package it.krzeminski.fsynth

import it.krzeminski.fsynth.synthesis.buildSongEvaluationFunction
import it.krzeminski.fsynth.synthesis.durationInSeconds
import it.krzeminski.fsynth.synthesis.preprocessForSynthesis
import it.krzeminski.fsynth.synthesis.types.SongForSynthesis
import it.krzeminski.fsynth.types.Song

fun Song.renderWithSampleRate(sampleRate: Int) =
        this.preprocessForSynthesis()
                .renderWithSampleRate(sampleRate)

fun SongForSynthesis.renderWithSampleRate(sampleRate: Int): Sequence<Float> {
    val samplesToRender = (sampleRate.toFloat() * durationInSeconds).toInt()
    val songEvaluationFunction = buildSongEvaluationFunction()
    return (0..samplesToRender).asSequence()
            .map(sampleIndexToTimeInSeconds(sampleRate))
            .map(songEvaluationFunction)
}

private fun sampleIndexToTimeInSeconds(sampleRate: Int) =
        { sampleIndex: Int -> sampleIndex.toFloat() / sampleRate.toFloat() }
