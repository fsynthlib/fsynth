package it.krzeminski.fsynth

import com.ccfraser.muirwik.components.MSliderMark
import com.ccfraser.muirwik.components.MSliderValueLabelDisplay
import com.ccfraser.muirwik.components.mSlider
import com.ccfraser.muirwik.components.mTypography
import it.krzeminski.fsynth.types.SynthesisParameters
import react.RBuilder
import react.RComponent
import react.RHandler
import react.RProps
import react.RState
import kotlin.math.log
import kotlin.math.pow
import styled.StyleSheet
import kotlinx.css.margin
import kotlinx.css.marginLeft
import kotlinx.css.px
import styled.css

class PlaybackCustomization(props: PlaybackCustomizationProps) : RComponent<PlaybackCustomizationProps, RState>(props) {
    override fun RBuilder.render() {
        bitsPerSampleDowncasting()
        tempoChange()
        synthesisSamplesPerSecondMultiplier()
        playbackSamplesPerSecondMultiplier()
    }

    private object Styles : StyleSheet("PlaybackCustomization", isStatic = true) {
        val sectionHeader by css {
            margin(10.px)
        }
        val slider by css {
            marginLeft = 35.px
        }
    }

    private fun RBuilder.bitsPerSampleDowncasting() {
        mTypography("Bits per sample (downcasting)") {
            css(Styles.sectionHeader)
        }
        mSlider(
                min = 1,
                max = 32,
                value = props.synthesisParameters.downcastToBitsPerSample ?: 32,
                showMarks = true,
                marks = listOf(
                        MSliderMark(1, "1 bit"),
                        MSliderMark(8, "8 bits"),
                        MSliderMark(16, "16 bits"),
                        MSliderMark(24, "24 bits"),
                        MSliderMark(32, "original")),
                valueLabelDisplay = MSliderValueLabelDisplay.auto,
                onChange = { _, newValue ->
                    props.onSynthesisParametersChange(props.synthesisParameters.copy(
                            downcastToBitsPerSample = newValue.toInt().let { if (it != 32) it else null }))
                }) {
            css(Styles.slider)
        }
    }

    private fun RBuilder.tempoChange() {
        mTypography("Tempo change (beats-per-second offset)") {
            css(Styles.sectionHeader)
        }
        mSlider(
                min = -100,
                max = 100,
                value = props.synthesisParameters.tempoOffset,
                showMarks = true,
                marks = listOf(
                        MSliderMark(-100, "-100"),
                        MSliderMark(-50, "-50"),
                        MSliderMark(0, "original"),
                        MSliderMark(50, "+50"),
                        MSliderMark(100, "+100")),
                valueLabelDisplay = MSliderValueLabelDisplay.auto,
                onChange = { _, newValue ->
                    props.onSynthesisParametersChange(props.synthesisParameters.copy(
                            tempoOffset = newValue.toInt()))
                }) {
            css(Styles.slider)
        }
    }

    private fun RBuilder.synthesisSamplesPerSecondMultiplier() {
        mTypography("Synthesis samples-per-second multiplier") {
            css(Styles.sectionHeader)
        }
        mSlider(
                min = -2,
                max = 2,
                value = fromMultiplierToLogarithmicSliderValue(
                        props.synthesisParameters.synthesisSamplesPerSecondMultiplier),
                showMarks = true,
                marks = listOf(
                        MSliderMark(-2, "0.25x"),
                        MSliderMark(-1, "0.5x"),
                        MSliderMark(0, "original"),
                        MSliderMark(1, "2x"),
                        MSliderMark(2, "4x")),
                valueLabelDisplay = MSliderValueLabelDisplay.auto,
                valueLabelFormat = { value, _ -> "${fromLogarithmicSliderValueToMultiplier(value.toInt())}x" },
                onChange = { _, newValue ->
                    props.onSynthesisParametersChange(props.synthesisParameters.copy(
                            synthesisSamplesPerSecondMultiplier =
                            fromLogarithmicSliderValueToMultiplier(newValue.toInt())))
                }) {
            css(Styles.slider)
        }
    }

    private fun RBuilder.playbackSamplesPerSecondMultiplier() {
        mTypography("Playback samples-per-second multiplier") {
            css(Styles.sectionHeader)
        }
        mSlider(
                min = -2,
                max = 2,
                value = fromMultiplierToLogarithmicSliderValue(
                        props.synthesisParameters.playbackSamplesPerSecondMultiplier),
                showMarks = true,
                marks = listOf(
                        MSliderMark(-2, "0.25x"),
                        MSliderMark(-1, "0.5x"),
                        MSliderMark(0, "original"),
                        MSliderMark(1, "2x"),
                        MSliderMark(2, "4x")),
                valueLabelDisplay = MSliderValueLabelDisplay.auto,
                valueLabelFormat = { value, _ -> "${fromLogarithmicSliderValueToMultiplier(value.toInt())}x" },
                onChange = { _, newValue ->
                    props.onSynthesisParametersChange(props.synthesisParameters.copy(
                            playbackSamplesPerSecondMultiplier =
                            fromLogarithmicSliderValueToMultiplier(newValue.toInt())))
                }) {
            css(Styles.slider)
        }
    }

    private fun fromMultiplierToLogarithmicSliderValue(multiplier: Float) =
            log(multiplier, 2.0f)

    private fun fromLogarithmicSliderValueToMultiplier(sliderValue: Int) =
            2.0f.pow(sliderValue.toFloat())
}

external interface PlaybackCustomizationProps : RProps {
    var synthesisParameters: SynthesisParameters
    var onSynthesisParametersChange: (SynthesisParameters) -> Unit
}

fun RBuilder.playbackCustomization(handler: RHandler<PlaybackCustomizationProps>) =
        child(PlaybackCustomization::class) {
            handler()
        }
