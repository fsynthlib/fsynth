package it.krzeminski.fsynth.typings

import org.khronos.webgl.ArrayBuffer

@JsModule("audiobuffer-to-wav")
@JsName("default")
external fun toWav(audioBuffer: AudioBuffer): ArrayBuffer = definedExternally
