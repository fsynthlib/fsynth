package it.krzeminski.fsynth

import it.krzeminski.fsynth.generated.gitInfo
import it.krzeminski.fsynth.synthesis.durationInSeconds
import it.krzeminski.fsynth.types.Song
import it.krzeminski.fsynth.types.SynthesisParameters
import it.krzeminski.fsynth.typings.toWav
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext
import mui.material.Accordion
import mui.material.AccordionDetails
import mui.material.AccordionSummary
import mui.material.AppBar
import mui.material.AppBarPosition
import mui.material.CircularProgress
import mui.material.Divider
import mui.material.Icon
import mui.material.IconButton
import mui.material.List as MuiList
import mui.material.ListItem
import mui.material.ListItemSecondaryAction
import mui.material.ListItemText
import mui.material.Paper
import mui.material.Toolbar
import mui.material.Typography
import mui.system.SxProps
import mui.system.Theme
import org.w3c.files.Blob
import react.FC
import react.Fragment
import react.Props
import react.create
import react.useState

val Player: FC<PlayerProps> = FC { props ->
    var lastSynthesizedAsWaveBlob by useState<Blob?>(null)
    var currentlySynthesizedSong by useState<Song?>(null)
    var currentSynthesisProgress by useState(0)
    var synthesisParameters by useState(SynthesisParameters(
            downcastToBitsPerSample = null,
            tempoOffset = 0,
            synthesisSamplesPerSecondMultiplier = 1.0f,
            playbackSamplesPerSecondMultiplier = 1.0f))

    Paper {
        sx = js("({ maxWidth: 400, margin: '0 auto' })").unsafeCast<SxProps<Theme>>()
        elevation = 6
        AppBar {
            position = AppBarPosition.static
            Toolbar {
                Typography {
                    variant = "h5".unsafeCast<Nothing>()
                    +"fsynth"
                }
            }
        }
        lastSynthesizedAsWaveBlob?.let { blob ->
            Wavesurfer {
                waveData = blob
            }
        }
        Divider {}
        props.songs.forEach { song ->
            MuiList {
                ListItem {
                    ListItemText {
                        primary = Fragment.create { +song.name }
                        secondary = Fragment.create { +getHumanFriendlyDuration(song) }
                    }
                    ListItemSecondaryAction {
                        if (currentlySynthesizedSong == song) {
                            CircularProgress {
                                variant = "determinate".unsafeCast<Nothing>()
                                value = currentSynthesisProgress
                            }
                        } else {
                            IconButton {
                                disabled = currentlySynthesizedSong != null
                                onClick = {
                                    CoroutineScope(EmptyCoroutineContext).launch {
                                        currentlySynthesizedSong = song
                                        currentSynthesisProgress = 0
                                        val renderedSong = song.renderToAudioBuffer(synthesisParameters,
                                                progressHandler = { currentSynthesisProgress = it })
                                        val songAsWavBlob = Blob(arrayOf(toWav(renderedSong)))
                                        lastSynthesizedAsWaveBlob = songAsWavBlob
                                        currentlySynthesizedSong = null
                                    }
                                }
                                Icon {
                                    +"play_arrow"
                                }
                            }
                        }
                    }
                }
            }
        }
        Divider {}
        Accordion {
            AccordionSummary {
                expandIcon = Fragment.create { Icon { +"expand_more" } }
                +"Playback customization"
            }
            AccordionDetails {
                PlaybackCustomization {
                    this.synthesisParameters = synthesisParameters
                    onSynthesisParametersChange = { synthesisParameters = it }
                }
            }
        }
    }
    VersionInfo {
        gitInfo = gitInfo
    }
}

external interface PlayerProps : Props {
    var songs: List<Song>
}

private fun getHumanFriendlyDuration(song: Song): String =
        with(song.durationInSeconds.toInt()) {
            val seconds = this.rem(60)
            val minutes = this / 60
            return "$minutes:${seconds.toString().padStart(2, '0')}"
        }
