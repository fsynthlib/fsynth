package it.krzeminski.fsynth

import it.krzeminski.fsynth.generated.gitInfo
import it.krzeminski.fsynth.synthesis.durationInSeconds
import it.krzeminski.fsynth.types.Song
import it.krzeminski.fsynth.types.SynthesisParameters
import it.krzeminski.fsynth.typings.materialAppBar
import it.krzeminski.fsynth.typings.materialCircularProgress
import it.krzeminski.fsynth.typings.materialDivider
import it.krzeminski.fsynth.typings.materialExpandMoreIcon
import it.krzeminski.fsynth.typings.materialExpansionPanel
import it.krzeminski.fsynth.typings.materialExpansionPanelDetails
import it.krzeminski.fsynth.typings.materialExpansionPanelSummary
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.w3c.files.Blob
import react.RBuilder
import react.RComponent
import react.RHandler
import react.RProps
import react.RState
import react.buildElement
import react.dom.div
import react.setState

class Player(props: PlayerProps) : RComponent<PlayerProps, PlayerState>(props), CoroutineScope by MainScope() {
    override fun PlayerState.init(props: PlayerProps) {
        lastSynthesizedAsWaveBlob = null
        currentlySynthesizedSong = null
        currentSynthesisProgress = 0
        synthesisParameters = SynthesisParameters(
                downcastToBitsPerSample = null,
                tempoOffset = 0,
                synthesisSamplesPerSecondMultiplier = 1.0f,
                playbackSamplesPerSecondMultiplier = 1.0f)
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
                        +"fsynth"
                    }
                }
            }
            state.lastSynthesizedAsWaveBlob?.let { lastSongInBase64 ->
                wavesurfer {
                    attrs {
                        waveData = lastSongInBase64
                    }
                }
            }
            materialDivider {}
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
                            if (state.currentlySynthesizedSong == song) {
                                materialCircularProgress {
                                    attrs {
                                        value = state.currentSynthesisProgress
                                        variant = "static"
                                    }
                                }
                            } else {
                                materialIconButton {
                                    attrs {
                                        onClick = {
                                            launch {
                                                setState {
                                                    currentlySynthesizedSong = song
                                                    currentSynthesisProgress = 0
                                                }
                                                val renderedSong = song.renderToAudioBuffer(state.synthesisParameters,
                                                        progressHandler = {
                                                            setState {
                                                                currentSynthesisProgress = it
                                                            }
                                                        })
                                                val songAsWavBlob = Blob(arrayOf(toWav(renderedSong)))
                                                setState {
                                                    lastSynthesizedAsWaveBlob = songAsWavBlob
                                                    currentlySynthesizedSong = null
                                                }
                                            }
                                        }
                                        disabled = state.currentlySynthesizedSong != null
                                    }
                                    materialPlayArrowIcon { }
                                }
                            }
                        }
                    }
                }
            }
            materialDivider {}
            materialExpansionPanel {
                materialExpansionPanelSummary {
                    attrs {
                        expandIcon = buildElement { materialExpandMoreIcon { } }
                    }
                    +"Playback customization"
                }
                materialExpansionPanelDetails {
                    div {
                        playbackCustomization {
                            attrs {
                                synthesisParameters = state.synthesisParameters
                                onSynthesisParametersChange = { newValue ->
                                    setState { synthesisParameters = newValue }
                                }
                            }
                        }
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
    var lastSynthesizedAsWaveBlob: Blob?

    /**
     * Null if no song is currently being synthesized.
     */
    var currentlySynthesizedSong: Song?
    var currentSynthesisProgress: Int
    var synthesisParameters: SynthesisParameters
}

private fun Song.getHumanFriendlyDuration(): String =
        with(durationInSeconds.toInt()) {
            val seconds = this.rem(60)
            val minutes = this / 60
            return "$minutes:${seconds.toString().padStart(2, '0')}"
        }
