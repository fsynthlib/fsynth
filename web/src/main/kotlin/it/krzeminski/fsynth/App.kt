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
import kotlinext.js.js
import react.RBuilder
import react.RComponent
import react.RHandler
import react.RProps
import react.RState
import react.dom.a
import react.dom.div
import react.setState

class App(props: AppProps) : RComponent<AppProps, AppState>(props) {
    override fun AppState.init(props: AppProps) {
        bitsPerSample = 32 // Full resolution provided by Float.
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
                                    playSong(song, bitsPerSample = state.bitsPerSample)
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
                +"Bits per sample"
            }
            materialSlider {
                attrs {
                    min = 1
                    max = 32
                    value = state.bitsPerSample
                    marks = true
                    valueLabelDisplay = "auto"
                    onChange = { _, newValue ->
                        setState { bitsPerSample = newValue.toInt() }
                    }
                }
            }
        }
        versionInfo {
            attrs.gitInfo = gitInfo
        }
    }
}

fun RBuilder.app(handler: RHandler<AppProps>) = child(App::class) {
    handler()
}

external interface AppProps : RProps {
    var songs: List<Song>
}

external interface AppState : RState {
    var bitsPerSample: Int
}

private fun Song.getHumanFriendlyDuration(): String =
        with(durationInSeconds.toInt()) {
            val seconds = this.rem(60)
            val minutes = this / 60
            return "$minutes:${seconds.toString().padStart(2, '0')}"
        }
