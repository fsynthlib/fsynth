@file:JsModule("@material-ui/core/Slider")

package it.krzeminski.fsynth.typings.materialui.widgets

import react.RProps
import react.RState
import react.ReactElement

@JsName("default")
external class Slider : react.Component<SliderProps, RState> {
    override fun render(): ReactElement?
}

external interface SliderProps : RProps {
    var min: Number
    var max: Number
    var value: Number?
    var marks: Boolean?
    var valueLabelDisplay: String?
    var onChange: ((dynamic, Number) -> Unit)?
}
