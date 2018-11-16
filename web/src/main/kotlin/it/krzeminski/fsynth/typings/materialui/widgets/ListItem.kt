@file:JsModule("@material-ui/core/ListItem")

package it.krzeminski.fsynth.typings.materialui.widgets

import react.RProps
import react.RState
import react.ReactElement

@JsName("default")
external class ListItem : react.Component<RProps, RState> {
    override fun render(): ReactElement?
}
