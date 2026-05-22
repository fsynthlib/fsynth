package it.krzeminski.fsynth.web.worker

import it.krzeminski.fsynth.songs.allSongs
import it.krzeminski.fsynth.types.SynthesisParameters
import it.krzeminski.fsynth.worker.SynthesisRequest
import it.krzeminski.fsynth.worker.SynthesisWorker
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.w3c.dom.DedicatedWorkerGlobalScope

external val self: DedicatedWorkerGlobalScope

fun main() {
    println("Worker starts!")

    self.onmessage = { e ->
        GlobalScope.launch {
            println("Worker got such request: ${JSON.stringify(e.data)}")
            val data = e.data.unsafeCast<dynamic>()
            val songName = data.songName as String
            val params = data.synthesisParameters
            val synthesisParameters = SynthesisParameters(
                downcastToBitsPerSample = params.downcastToBitsPerSample as? Int,
                tempoOffset = params.tempoOffset as Int,
                synthesisSamplesPerSecondMultiplier = params.synthesisSamplesPerSecondMultiplier as Float,
                playbackSamplesPerSecondMultiplier = params.playbackSamplesPerSecondMultiplier as Float
            )
            val request = SynthesisRequest(songName, synthesisParameters)
            val songData = SynthesisWorkerImpl.synthesize(request) { progress ->
                val responseMessage = js("({})")
                responseMessage.type = "progress"
                responseMessage.progress = progress
                self.postMessage(responseMessage)
                println("Response message posted: $progress progress")
            }
            val responseMessage = js("({})")
            responseMessage.type = "result"
            responseMessage.songData = songData
            self.postMessage(responseMessage)
            println("Response message posted: result")
        }
    }
}

object SynthesisWorkerImpl : SynthesisWorker {
    override suspend fun synthesize(
        synthesisRequest: SynthesisRequest,
        progressHandler: (Int) -> Unit
    ): Array<Float> {
        val song = allSongs.find { it.name == synthesisRequest.songName }
        return song?.let {
            println("Song found, synthesizing")
            val renderedSong = it.renderToArray(synthesisRequest.synthesisParameters, onProgressChange = { progress ->
                progressHandler(progress)
            })
            println("Synthesis done")
            return renderedSong
        } ?: emptyArray() // TODO: proper error handling on the worker side.
    }
}
