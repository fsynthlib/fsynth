@file:JsModule("@material-ui/core/CircularProgress")

package it.krzeminski.fsynth.typings.materialui.widgets

import react.RProps
import react.RState
import react.ReactElement

@JsName("default")
external class CircularProgress : react.Component<CircularProgressProps, RState> {
    override fun render(): ReactElement?
}

external interface CircularProgressProps : RProps {
    var value: Number
    var variant: String
}
