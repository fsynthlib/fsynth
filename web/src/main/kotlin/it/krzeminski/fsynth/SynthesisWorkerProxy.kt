package it.krzeminski.fsynth

import it.krzeminski.fsynth.types.Song
import it.krzeminski.fsynth.types.SynthesisParameters
import it.krzeminski.fsynth.typings.AudioBuffer
import it.krzeminski.fsynth.typings.AudioContext
import it.krzeminski.fsynth.worker.SynthesisRequest
import it.krzeminski.fsynth.worker.SynthesisResponse
import it.krzeminski.fsynth.worker.SynthesisWorker
import org.khronos.webgl.Float32Array
import org.w3c.dom.Worker
import kotlin.browser.window
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun Song.renderToAudioBuffer(
    synthesisParameters: SynthesisParameters,
    progressHandler: (Int) -> Unit
): AudioBuffer {
    val synthesisRequest = SynthesisRequest(name, synthesisParameters)
    val startTime = window.performance.now()
    val songData = SynthesisWorkerProxy.synthesize(synthesisRequest, progressHandler)

    val renderedSong = Float32Array(songData)
    val context = AudioContext()
    val playbackSamplesPerSecond =
            (44100.toFloat() * synthesisParameters.playbackSamplesPerSecondMultiplier).toInt()
    val audioContextBuffer = createAudioContextBuffer(context, renderedSong, playbackSamplesPerSecond)

    println("Synthesis time with worker overhead: ${(window.performance.now() - startTime) / 1000.0} s")

    return audioContextBuffer
}

private object SynthesisWorkerProxy : SynthesisWorker {
    private val workerJs = Worker("worker.js")

    override suspend fun synthesize(
        synthesisRequest: SynthesisRequest,
        progressHandler: (Int) -> Unit
    ): Array<Float> = suspendCoroutine { continuation ->
        with(workerJs) {
            postMessage(synthesisRequest)
            onmessage = { event ->
                val response = event.data.unsafeCast<SynthesisResponse>()
                when (response.type) {
                    "progress" -> progressHandler(response.progress!!)
                    "result" -> continuation.resume(response.songData!!)
                    else -> throw IllegalArgumentException("${response.type} not handled!")
                }
            }
        }
    }
}

private fun createAudioContextBuffer(context: AudioContext, buffer: Float32Array, samplesPerSecond: Int): AudioBuffer {
    val contextBuffer = context.createBuffer(
            numberOfChannels = 1, length = buffer.length, sampleRate = samplesPerSecond)
    contextBuffer.copyToChannel(buffer, 0)
    return contextBuffer
}
