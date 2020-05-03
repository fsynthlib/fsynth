@file:JsModule("@material-ui/core/Typography")

package it.krzeminski.fsynth.typings.materialui.widgets

import react.RProps
import react.RState
import react.ReactElement

@JsName("default")
external class Typography : react.Component<TypographyProps, RState> {
    override fun render(): ReactElement?
}

external interface TypographyProps : RProps {
    var variant: String
    var color: String
    var style: dynamic
}
