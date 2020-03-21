package it.krzeminski.fsynth.types

data class BoundedWaveform(
    val waveform: Waveform,
    val duration: Float
)

operator fun BoundedWaveform.plus(otherBoundedWaveform: BoundedWaveform): BoundedWaveform {
    require(this.duration == otherBoundedWaveform.duration) {
        "Adding waveforms with different durations is not supported!"
    }
    return BoundedWaveform(
            waveform = { time -> this.waveform(time) + otherBoundedWaveform.waveform(time) },
            duration = this.duration)
}
