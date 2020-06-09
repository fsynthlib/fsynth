@file:JsModule("@material-ui/core/IconButton")

package it.krzeminski.fsynth.typings.materialui.widgets

import react.RProps
import react.RState
import react.ReactElement

@JsName("default")
external class IconButton : react.Component<IconButtonProps, RState> {
    override fun render(): ReactElement?
}

external interface IconButtonProps : RProps {
    var onClick: () -> Unit
    var disabled: Boolean
}
