package it.krzeminski.fsynth

import it.krzeminski.fsynth.generated.gitInfo
import it.krzeminski.fsynth.synthesis.durationInSeconds
import it.krzeminski.fsynth.types.Song
import it.krzeminski.fsynth.typings.materialAppBar
import it.krzeminski.fsynth.typings.materialDivider
import it.krzeminski.fsynth.typings.materialIconButton
import it.krzeminski.fsynth.typings.materialList
import it.krzeminski.fsynth.typings.materialListItem
import it.krzeminski.fsynth.typings.materialListItemSecondaryAction
import it.krzeminski.fsynth.typings.materialListItemText
import it.krzeminski.fsynth.typings.materialPaper
import it.krzeminski.fsynth.typings.materialPlayArrowIcon
import it.krzeminski.fsynth.typings.materialSlider
import it.krzeminski.fsynth.typings.materialToolbar
import it.krzeminski.fsynth.typings.materialTypography
import it.krzeminski.fsynth.typings.materialui.widgets.types.Mark
import kotlinext.js.js
import react.RBuilder
import react.RComponent
import react.RHandler
import react.RProps
import react.RState
import react.dom.a
import react.dom.div
import react.setState

class Player(props: PlayerProps) : RComponent<PlayerProps, PlayerState>(props) {
    override fun PlayerState.init(props: PlayerProps) {
        downcastToBitsPerSample = null
    }

    override fun RBuilder.render() {
        materialPaper {
            attrs {
                style = js {
                    width = "100%"
                    maxWidth = "400px"
                    margin = "0 auto"
                }
                elevation = 6
            }
            materialAppBar {
                attrs.position = "static"
                materialToolbar {
                    materialTypography {
                        attrs {
                            variant = "h5"
                            color = "inherit"
                        }
                        +"fsynth player"
                    }
                }
            }
            materialPaper {
                attrs.style = js {
                    margin = "10px"
                    padding = "10px"
                    backgroundColor = "#B00020"
                    color = "white"
                }
                div { +"The sound is synthesized and played after clicking the 'play' button next to the chosen song." }
                div { +"The synthesis may take several seconds, and blocks the UI, so please be patient." }
                div {
                    +"Making this UI non-blocking is in scope of "
                    a("https://github.com/krzema12/fsynth/issues/14") { +"issue #14" }
                    +"."
                }
            }
            props.songs.forEach { song ->
                materialList {
                    materialListItem {
                        materialListItemText {
                            attrs {
                                primary = song.name
                                secondary = song.getHumanFriendlyDuration()
                            }
                        }
                        materialListItemSecondaryAction {
                            materialIconButton {
                                attrs.onClick = {
                                    playSong(song, downcastToBitsPerSample = state.downcastToBitsPerSample)
                                }
                                materialPlayArrowIcon { }
                            }
                        }
                    }
                }
            }
            materialDivider {}
            materialTypography {
                attrs {
                    style = js {
                        margin = "10px"
                    }
                }
                +"Bits per sample (downcasting)"
            }
            materialSlider {
                attrs {
                    min = 1
                    max = 32
                    value = state.downcastToBitsPerSample ?: 32
                    marks = arrayOf(
                            Mark(1, "1 bit"),
                            Mark(8, "8 bits"),
                            Mark(16, "16 bits"),
                            Mark(24, "24 bits"),
                            Mark(32, "original"))
                    valueLabelDisplay = "auto"
                    onChange = { _, newValue ->
                        setState {
                            downcastToBitsPerSample = newValue.toInt()
                                    .let { if (it != 32) it else null }
                        }
                    }
                    style = js {
                        marginLeft = "20px"
                        width = "350px"
                    }
                }
            }
        }
        versionInfo {
            attrs.gitInfo = gitInfo
        }
    }
}

fun RBuilder.player(handler: RHandler<PlayerProps>) = child(Player::class) {
    handler()
}

external interface PlayerProps : RProps {
    var songs: List<Song>
}

external interface PlayerState : RState {
    /**
     * Null means downcasting is disabled.
     */
    var downcastToBitsPerSample: Int?
}

private fun Song.getHumanFriendlyDuration(): String =
        with(durationInSeconds.toInt()) {
            val seconds = this.rem(60)
            val minutes = this / 60
            return "$minutes:${seconds.toString().padStart(2, '0')}"
        }
