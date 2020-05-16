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
import it.krzeminski.fsynth.typings.materialToolbar
import it.krzeminski.fsynth.typings.materialTypography
import it.krzeminski.fsynth.typings.toWav
import kotlinext.js.js
import kotlinx.html.style
import react.RBuilder
import react.RComponent
import react.RHandler
import react.RProps
import react.RState
import react.dom.a
import react.dom.audio
import react.dom.div
import react.setState

class Player(props: PlayerProps) : RComponent<PlayerProps, PlayerState>(props) {
    override fun PlayerState.init(props: PlayerProps) {
        lastSynthesizedSongAsBase64Wav = null
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
            state.lastSynthesizedSongAsBase64Wav?.let { lastSongInBase64 ->
                audio {
                    attrs {
                        src = lastSongInBase64
                        controls = true
                        autoPlay = true
                        style = js {
                            width = "100%"
                        }
                    }
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
                                    val songAsAudioBuffer = song.renderToAudioBuffer(
                                            downcastToBitsPerSample = state.downcastToBitsPerSample)
                                    val songAsWav = toWav(songAsAudioBuffer)
                                    setState {
                                        lastSynthesizedSongAsBase64Wav = songAsWav.toBase64().base64WavAsDataUrl()
                                    }
                                }
                                materialPlayArrowIcon { }
                            }
                        }
                    }
                }
            }
            materialDivider {}
            playbackCustomization {
                attrs {
                    downcastToBitsPerSample = state.downcastToBitsPerSample
                    onDowncastToBitsPerSampleChange = { newValue ->
                        setState { downcastToBitsPerSample = newValue }
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
     * Null if no song has been synthesized yet.
     */
    var lastSynthesizedSongAsBase64Wav: String?
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
