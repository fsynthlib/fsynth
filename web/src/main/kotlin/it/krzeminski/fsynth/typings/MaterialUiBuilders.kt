package it.krzeminski.fsynth.typings

import it.krzeminski.fsynth.typings.materialui.icons.PlayArrowIcon
import it.krzeminski.fsynth.typings.materialui.widgets.AppBar
import it.krzeminski.fsynth.typings.materialui.widgets.AppBarProps
import it.krzeminski.fsynth.typings.materialui.widgets.CircularProgress
import it.krzeminski.fsynth.typings.materialui.widgets.CircularProgressProps
import it.krzeminski.fsynth.typings.materialui.widgets.Divider
import it.krzeminski.fsynth.typings.materialui.widgets.IconButton
import it.krzeminski.fsynth.typings.materialui.widgets.IconButtonProps
import it.krzeminski.fsynth.typings.materialui.widgets.List
import it.krzeminski.fsynth.typings.materialui.widgets.ListItem
import it.krzeminski.fsynth.typings.materialui.widgets.ListItemSecondaryAction
import it.krzeminski.fsynth.typings.materialui.widgets.ListItemText
import it.krzeminski.fsynth.typings.materialui.widgets.ListItemTextProps
import it.krzeminski.fsynth.typings.materialui.widgets.Paper
import it.krzeminski.fsynth.typings.materialui.widgets.PaperProps
import it.krzeminski.fsynth.typings.materialui.widgets.Slider
import it.krzeminski.fsynth.typings.materialui.widgets.SliderProps
import it.krzeminski.fsynth.typings.materialui.widgets.Toolbar
import it.krzeminski.fsynth.typings.materialui.widgets.Typography
import it.krzeminski.fsynth.typings.materialui.widgets.TypographyProps
import react.RBuilder
import react.RHandler
import react.RProps

fun RBuilder.materialIconButton(handler: RHandler<IconButtonProps>) = child(IconButton::class) {
    handler()
}

fun RBuilder.materialAppBar(handler: RHandler<AppBarProps>) = child(AppBar::class) {
    handler()
}

fun RBuilder.materialToolbar(handler: RHandler<RProps>) = child(Toolbar::class) {
    handler()
}

fun RBuilder.materialTypography(handler: RHandler<TypographyProps>) = child(Typography::class) {
    handler()
}

fun RBuilder.materialList(handler: RHandler<RProps>) = child(List::class) {
    handler()
}

fun RBuilder.materialListItem(handler: RHandler<RProps>) = child(ListItem::class) {
    handler()
}

fun RBuilder.materialListItemText(handler: RHandler<ListItemTextProps>) = child(ListItemText::class) {
    handler()
}

fun RBuilder.materialListItemSecondaryAction(handler: RHandler<RProps>) = child(ListItemSecondaryAction::class) {
    handler()
}

fun RBuilder.materialPaper(handler: RHandler<PaperProps>) = child(Paper::class) {
    handler()
}

fun RBuilder.materialPlayArrowIcon(handler: RHandler<RProps>) = child(PlayArrowIcon::class) {
    handler()
}

fun RBuilder.materialSlider(handler: RHandler<SliderProps>) = child(Slider::class) {
    handler()
}

fun RBuilder.materialDivider(handler: RHandler<RProps>) = child(Divider::class) {
    handler()
}

fun RBuilder.materialCircularProgress(handler: RHandler<CircularProgressProps>) = child(CircularProgress::class) {
    handler()
}
