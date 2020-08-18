package it.krzeminski.fsynth

import com.ccfraser.muirwik.components.MAppBarPosition
import com.ccfraser.muirwik.components.MCircularProgressVariant
import com.ccfraser.muirwik.components.MIconColor
import com.ccfraser.muirwik.components.MTypographyColor
import com.ccfraser.muirwik.components.MTypographyVariant
import com.ccfraser.muirwik.components.button.mIconButton
import com.ccfraser.muirwik.components.expansionpanel.mExpansionPanel
import com.ccfraser.muirwik.components.expansionpanel.mExpansionPanelDetails
import com.ccfraser.muirwik.components.expansionpanel.mExpansionPanelSummary
import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.list.mListItem
import com.ccfraser.muirwik.components.list.mListItemSecondaryAction
import com.ccfraser.muirwik.components.list.mListItemText
import com.ccfraser.muirwik.components.mAppBar
import com.ccfraser.muirwik.components.mCircularProgress
import com.ccfraser.muirwik.components.mDivider
import com.ccfraser.muirwik.components.mIcon
import com.ccfraser.muirwik.components.mPaper
import com.ccfraser.muirwik.components.mToolbar
import com.ccfraser.muirwik.components.mTypography
import it.krzeminski.fsynth.generated.gitInfo
import it.krzeminski.fsynth.synthesis.durationInSeconds
import it.krzeminski.fsynth.types.Song
import it.krzeminski.fsynth.types.SynthesisParameters
import it.krzeminski.fsynth.typings.toWav
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.css.LinearDimension
import kotlinx.css.margin
import kotlinx.css.maxWidth
import kotlinx.css.pc
import kotlinx.css.px
import kotlinx.css.width
import org.w3c.files.Blob
import react.RBuilder
import react.RComponent
import react.RHandler
import react.RProps
import react.RState
import react.buildElement
import react.dom.div
import react.setState
import styled.StyleSheet
import styled.css

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

    private object Styles : StyleSheet("Player", isStatic = true) {
        val backgroundPaper by css {
            width = 100.pc
            maxWidth = 400.px
            margin(vertical = 0.px, horizontal = LinearDimension.auto)
        }
    }

    override fun RBuilder.render() {
        mPaper(elevation = 6) {
            css(Styles.backgroundPaper)
            mAppBar(position = MAppBarPosition.static) {
                mToolbar {
                    mTypography("fsynth", variant = MTypographyVariant.h5, color = MTypographyColor.inherit)
                }
            }
            state.lastSynthesizedAsWaveBlob?.let { lastSongInBase64 ->
                wavesurfer {
                    attrs {
                        waveData = lastSongInBase64
                    }
                }
            }
            mDivider()
            props.songs.forEach { song ->
                mList {
                    mListItem {
                        mListItemText(primary = song.name, secondary = song.getHumanFriendlyDuration())
                        mListItemSecondaryAction {
                            if (state.currentlySynthesizedSong == song) {
                                mCircularProgress(
                                        value = state.currentSynthesisProgress.toDouble(),
                                        variant = MCircularProgressVariant.static)
                            } else {
                                mIconButton(
                                        "play_arrow",
                                        disabled = state.currentlySynthesizedSong != null,
                                        iconColor = if (state.currentlySynthesizedSong == null) null else MIconColor.disabled,
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
                                })
                            }
                        }
                    }
                }
            }
            mDivider()
            mExpansionPanel {
                mExpansionPanelSummary(expandIcon = buildElement { mIcon("expand_more") }) {
                    +"Playback customization"
                }
                mExpansionPanelDetails {
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
