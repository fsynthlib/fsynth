package it.krzeminski.fsynth.typings

import org.khronos.webgl.Float32Array

external class AudioBuffer {
    fun copyToChannel(source: Float32Array, channel: Int): Unit = definedExternally
}

external class AudioContext {
    fun createBuffer(numberOfChannels: Int, length: Int, sampleRate: Int): AudioBuffer = definedExternally
    fun createBufferSource(): BufferSource = definedExternally

    val destination: String
        get() = definedExternally
}

external class BufferSource {
    fun connect(destination: String): Unit = definedExternally
    fun start(position: Int): Unit = definedExternally

    var buffer: AudioBuffer
        set(_) = definedExternally
}
