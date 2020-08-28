package it.krzeminski.fsynth.types

data class SynthesisParameters(
    /**
     * Null means downcasting is disabled.
     */
    val downcastToBitsPerSample: Int?,
    val tempoOffset: Int,
    val synthesisSamplesPerSecondMultiplier: Float,
    val playbackSamplesPerSecondMultiplier: Float
)
