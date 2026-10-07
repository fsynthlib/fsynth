package it.krzeminski.fsynth.typings

import org.w3c.files.Blob

@JsModule("wavesurfer.js")
@JsName("default")
external class WaveSurfer(params: WaveSurferOptions) {
    fun loadBlob(blob: Blob): Unit = definedExternally
    fun play(): Unit = definedExternally
    fun on(eventName: String, callback: () -> Unit): Unit = definedExternally
}

external interface WaveSurferOptions {
    var container: String
    var height: Int
    var cursorWidth: Int
    var progressColor: String
    var waveColor: String
}
