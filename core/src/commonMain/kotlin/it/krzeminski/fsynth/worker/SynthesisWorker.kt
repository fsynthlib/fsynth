package it.krzeminski.fsynth.worker

import it.krzeminski.fsynth.types.SynthesisParameters

interface SynthesisWorker {
    suspend fun synthesize(synthesisRequest: SynthesisRequest, progressHandler: (Int) -> Unit): Array<Float>
}

data class SynthesisRequest(
        // Song type cannot be used here because it cannot be serialized when sending through the Web Workers API.
        // It cannot be serialized probably because it contains an 'instrument' field which is a function, which is not
        // serializable. It also means that only songs defined in 'allSongs' constant in the 'core' project can be
        // synthesized now.
        // See https://developer.mozilla.org/en-US/docs/Web/API/Web_Workers_API/Structured_clone_algorithm
    val songName: String,
    val synthesisParameters: SynthesisParameters
)

data class SynthesisResponse(
    val type: String,
    val songData: Array<Float>? = null,
    val progress: Int? = null
)
