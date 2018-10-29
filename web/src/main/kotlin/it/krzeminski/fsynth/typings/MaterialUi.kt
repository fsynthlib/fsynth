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

@JsName("IconButton")
external class IconButton : react.Component<IconButtonProps, RState> {
    override fun render(): ReactElement?
}

external interface IconButtonProps : RProps {
    var onClick: () -> Unit
}

@JsName("LinearProgress")
external class LinearProgress : react.Component<LinearProgressProps, RState> {
    override fun render(): ReactElement?
}

external interface LinearProgressProps : RProps {
    var variant: String
    var value: Int
    var valueBuffer: Int
}

@JsName("AppBar")
external class AppBar : react.Component<AppBarProps, RState> {
    override fun render(): ReactElement?
}

external interface AppBarProps : RProps {
    var position: String
}

@JsName("Toolbar")
external class Toolbar : react.Component<RProps, RState> {
    override fun render(): ReactElement?
}

@JsName("Typography")
external class Typography : react.Component<TypographyProps, RState> {
    override fun render(): ReactElement?
}

external interface TypographyProps : RProps {
    var variant: String
    var color: String
}

@JsName("List")
external class List : react.Component<ListProps, RState> {
    override fun render(): ReactElement?
}

external interface ListProps : RProps {

}

@JsName("ListItem")
external class ListItem : react.Component<ListItemProps, RState> {
    override fun render(): ReactElement?
}

external interface ListItemProps : RProps {

}

@JsName("ListItemText")
external class ListItemText : react.Component<ListItemTextProps, RState> {
    override fun render(): ReactElement?
}

external interface ListItemTextProps : RProps {
    var primary: String
    var secondary: String
}

@JsName("ListItemSecondaryAction")
external class ListItemSecondaryAction : react.Component<ListItemSecondaryActionProps, RState> {
    override fun render(): ReactElement?
}

external interface ListItemSecondaryActionProps : RProps {
}

@JsName("Paper")
external class Paper : react.Component<PaperProps, RState> {
    override fun render(): ReactElement?
}

external interface PaperProps : RProps {
    var elevation: Int
    var style: dynamic
}
