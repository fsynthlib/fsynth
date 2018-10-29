package it.krzeminski.fsynth.typings

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

fun RBuilder.materialList(handler: RHandler<ListProps>) = child(List::class) {
    handler()
}

fun RBuilder.materialListItem(handler: RHandler<ListItemProps>) = child(ListItem::class) {
    handler()
}

fun RBuilder.materialListItemText(handler: RHandler<ListItemTextProps>) = child(ListItemText::class) {
    handler()
}

fun RBuilder.materialListItemSecondaryAction(handler: RHandler<ListItemSecondaryActionProps>)
        = child(ListItemSecondaryAction::class)
{
    handler()
}

fun RBuilder.materialPaper(handler: RHandler<PaperProps>) = child(Paper::class) {
    handler()
}

fun RBuilder.materialPlayArrowIcon(handler: RHandler<RProps>) = child(PlayArrowIcon::class) {
    handler()
}
