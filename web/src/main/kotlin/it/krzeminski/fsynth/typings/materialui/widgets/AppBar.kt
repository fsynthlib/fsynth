@file:JsModule("@material-ui/core/AppBar")

package it.krzeminski.fsynth.typings.materialui.widgets

import react.RProps
import react.RState
import react.ReactElement

@JsName("default")
external class AppBar : react.Component<AppBarProps, RState> {
    override fun render(): ReactElement?
}

external interface AppBarProps : RProps {
    var position: String
}
