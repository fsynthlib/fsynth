package it.krzeminski.fsynth.web.worker.typings

import org.khronos.webgl.Float32Array

external class AudioBuffer {
    fun copyToChannel(source: Float32Array, channel: Int): Unit = definedExternally
}

external class BufferSource {
    fun connect(destination: String): Unit = definedExternally
    fun start(position: Int): Unit = definedExternally

    var buffer: AudioBuffer
        set(_) = definedExternally
}
