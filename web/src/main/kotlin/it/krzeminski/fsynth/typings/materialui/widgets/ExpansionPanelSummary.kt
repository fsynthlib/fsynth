@file:JsModule("@material-ui/core/ExpansionPanelSummary")

package it.krzeminski.fsynth.typings.materialui.widgets

import react.RProps
import react.RState
import react.ReactElement

@JsName("default")
external class ExpansionPanelSummary : react.Component<ExpansionPanelSummaryProps, RState> {
    override fun render(): ReactElement?
}

external interface ExpansionPanelSummaryProps : RProps {
    var expandIcon: ReactElement
}
