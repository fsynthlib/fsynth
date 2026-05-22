package it.krzeminski.fsynth.web.worker

import it.krzeminski.fsynth.songs.allSongs
import it.krzeminski.fsynth.worker.SynthesisRequest
import it.krzeminski.fsynth.worker.SynthesisResponse
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
            val request = e.data.unsafeCast<SynthesisRequest>()
            val songData = SynthesisWorkerImpl.synthesize(request) { progress ->
                val responseMessage = SynthesisResponse(type = "progress", progress = progress)
                self.postMessage(responseMessage)
                println("Response message posted: $responseMessage")
            }
            val responseMessage = SynthesisResponse(type = "result", songData = songData)
            self.postMessage(responseMessage)
            println("Response message posted: $responseMessage")
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
