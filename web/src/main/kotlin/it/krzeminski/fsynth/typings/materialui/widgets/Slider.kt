@file:JsModule("@material-ui/core/Slider")

package it.krzeminski.fsynth.typings.materialui.widgets

import it.krzeminski.fsynth.typings.materialui.widgets.types.Mark
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
    var marks: Array<Mark>?
    var valueLabelDisplay: String?
    var onChange: ((dynamic, Number) -> Unit)?
    var style: dynamic
}
