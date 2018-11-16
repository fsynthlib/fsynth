@file:JsModule("@material-ui/core/List")

package it.krzeminski.fsynth.typings.materialui.widgets

import react.RProps
import react.RState
import react.ReactElement

@JsName("default")
external class List : react.Component<RProps, RState> {
    override fun render(): ReactElement?
}
