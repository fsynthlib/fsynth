package it.krzeminski.fsynth

import it.krzeminski.fsynth.synthesis.buildSongEvaluationFunction
import it.krzeminski.fsynth.synthesis.durationInSeconds
import it.krzeminski.fsynth.synthesis.preprocessForSynthesis
import it.krzeminski.fsynth.synthesis.types.SongForSynthesis
import it.krzeminski.fsynth.types.Song

fun Song.renderWithSampleRate(sampleRate: Int, startTime: Float = 0.0f, onProgressChange: (Int) -> Unit = {}) =
        this.preprocessForSynthesis()
                .renderWithSampleRate(sampleRate, startTime, onProgressChange)

fun SongForSynthesis.renderWithSampleRate(
    sampleRate: Int,
    startTime: Float,
    onProgressChange: (Int) -> Unit = {}
): Sequence<Float> {
    val startSample = (sampleRate.toFloat() * startTime).toInt()
    val endSample = (sampleRate.toFloat() * durationInSeconds).toInt()
    val samplesToRender = endSample - startSample
    val songEvaluationFunction = buildSongEvaluationFunction()
    return (startSample..endSample).asSequence().chunked(samplesToRender / 10).map { chunk ->
        onProgressChange((chunk[0] - startSample)*100 / samplesToRender)
        chunk
                .map(sampleIndexToTimeInSeconds(sampleRate))
                .map(songEvaluationFunction)
    }.flatten()
}

private fun sampleIndexToTimeInSeconds(sampleRate: Int) = { sampleIndex: Int -> sampleIndex.toFloat() / sampleRate.toFloat() }
