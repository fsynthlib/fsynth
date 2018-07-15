package it.krzeminski.fsynth.typings

import react.RBuilder
import react.RHandler

fun RBuilder.materialButton(handler: RHandler<ButtonProps>) = child(Button::class) {
    attrs {
        this.variant = "raised"
        this.color = "primary"
    }
    handler()
}
