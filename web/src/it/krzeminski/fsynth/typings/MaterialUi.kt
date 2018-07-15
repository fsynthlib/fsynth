@file:JsModule("@material-ui/core")

package it.krzeminski.fsynth.typings

import react.RProps
import react.RState
import react.ReactElement

@JsName("Button")
external class Button : react.Component<ButtonProps, RState> {
    override fun render(): ReactElement?
}

external interface ButtonProps : RProps {
    var id: String
    var variant: String
    var color: String
    var onClick: () -> Unit
}
