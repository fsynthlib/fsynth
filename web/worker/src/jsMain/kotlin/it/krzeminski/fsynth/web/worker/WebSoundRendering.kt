package it.krzeminski.fsynth.web.worker

import it.krzeminski.fsynth.postprocessing.reduceLevelsPerSample
import it.krzeminski.fsynth.renderWithSampleRate
import it.krzeminski.fsynth.types.Song
import it.krzeminski.fsynth.types.SynthesisParameters
import it.krzeminski.fsynth.web.worker.testutils.measureTimeSeconds
import kotlin.math.pow

fun Song.renderToArray(synthesisParameters: SynthesisParameters, onProgressChange: (Int) -> Unit): Array<Float> {
    lateinit var songAsArray: Array<Float>

    val songAfterTempoAdjustment = this.copy(beatsPerMinute = beatsPerMinute + synthesisParameters.tempoOffset)
    val synthesisSamplesPerSecond = (44100.toFloat() * synthesisParameters.synthesisSamplesPerSecondMultiplier).toInt()

    val timeInSeconds = measureTimeSeconds {
        songAsArray = songAfterTempoAdjustment.renderWithSampleRate(
                synthesisSamplesPerSecond, startTime = 0.0f, onProgressChange = onProgressChange)
                .map { it.applyLevelsPerSampleReduction(synthesisParameters.downcastToBitsPerSample) }
                .toList()
                .toTypedArray()
    }
    println("Synthesized in $timeInSeconds s")

    return songAsArray
}

private fun Float.applyLevelsPerSampleReduction(downcastToBitsPerSample: Int?) =
        if (downcastToBitsPerSample != null) {
            reduceLevelsPerSample(2.0f.pow(downcastToBitsPerSample).toInt())
        } else { // No levels-per-sample reduction.
            this
        }
