package it.krzeminski.fsynth.typings

import org.w3c.files.Blob

@JsModule("wavesurfer.js")
@JsName("default")
external class WaveSurfer(params: dynamic) {
    fun init(): Unit = definedExternally
    fun loadBlob(blob: Blob): Unit = definedExternally
    fun play(): Unit = definedExternally
    fun on(eventName: String, callback: () -> Unit): Unit = definedExternally
}
