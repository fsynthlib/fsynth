@file:JsModule("@material-ui/core/ListItemText")

package it.krzeminski.fsynth.typings.materialui.widgets

import react.RProps
import react.RState
import react.ReactElement

@JsName("default")
external class ListItemText : react.Component<ListItemTextProps, RState> {
    override fun render(): ReactElement?
}

external interface ListItemTextProps : RProps {
    var primary: String
    var secondary: String
}
