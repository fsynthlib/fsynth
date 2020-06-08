package it.krzeminski.fsynth.web.worker

import it.krzeminski.fsynth.songs.allSongs
import it.krzeminski.fsynth.worker.SynthesisRequest
import it.krzeminski.fsynth.worker.SynthesisResponse
import it.krzeminski.fsynth.worker.SynthesisWorker
import org.w3c.dom.DedicatedWorkerGlobalScope

external val self: DedicatedWorkerGlobalScope

fun main() {
    println("Worker starts!")

    self.onmessage = { e ->
        println("Worker got such request: ${JSON.stringify(e.data)}")
        val request = e.data.unsafeCast<SynthesisRequest>()
        SynthesisWorkerImpl.synthesizeAsync(request) { synthesisResponse ->
            self.postMessage(synthesisResponse)
        }
    }
}

object SynthesisWorkerImpl : SynthesisWorker {
    override fun synthesizeAsync(synthesisRequest: SynthesisRequest, responseCallback: (SynthesisResponse) -> Unit) {
        val song = allSongs.find { it.name == synthesisRequest.songName }
        song?.let {
            println("Song found, synthesizing")
            val renderedSong = it.renderToArray(synthesisRequest.synthesisParameters, onProgressChange = { progress ->
                responseCallback(SynthesisResponse(type = "progress", progress = progress))
            })
            println("Synthesis done")
            responseCallback(SynthesisResponse(type = "result", songData = renderedSong))
            println("Response message posted")
        }
    }
}
