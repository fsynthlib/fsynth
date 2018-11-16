@file:JsModule("@material-ui/core/Paper")

package it.krzeminski.fsynth.typings.materialui.widgets

import react.RProps
import react.RState
import react.ReactElement

@JsName("default")
external class Paper : react.Component<PaperProps, RState> {
    override fun render(): ReactElement?
}

external interface PaperProps : RProps {
    var elevation: Int
    var style: dynamic
}
