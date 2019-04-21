package it.krzeminski.fsynth

import it.krzeminski.fsynth.synthesis.buildSongEvaluationFunction
import it.krzeminski.fsynth.synthesis.durationInSeconds
import it.krzeminski.fsynth.synthesis.preprocessForSynthesis
import it.krzeminski.fsynth.synthesis.types.SongForSynthesis
import it.krzeminski.fsynth.types.Song

fun Song.renderWithSampleRate(sampleRate: Int, startTime: Float) =
        this.preprocessForSynthesis()
                .renderWithSampleRate(sampleRate, startTime)

fun SongForSynthesis.renderWithSampleRate(sampleRate: Int, startTime: Float): Sequence<Float> {
    val startSample = (sampleRate.toFloat() * startTime).toInt()
    val samplesToRender = (sampleRate.toFloat() * durationInSeconds).toInt()
    val songEvaluationFunction = buildSongEvaluationFunction()
    return (startSample..samplesToRender).asSequence()
            .map(sampleIndexToTimeInSeconds(sampleRate))
            .map(songEvaluationFunction)
}

private fun sampleIndexToTimeInSeconds(sampleRate: Int) =
        { sampleIndex: Int -> sampleIndex.toFloat() / sampleRate.toFloat() }
