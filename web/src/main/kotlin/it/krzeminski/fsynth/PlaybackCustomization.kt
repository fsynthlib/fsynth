package it.krzeminski.fsynth

import it.krzeminski.fsynth.types.SynthesisParameters
import it.krzeminski.fsynth.typings.materialSlider
import it.krzeminski.fsynth.typings.materialTypography
import it.krzeminski.fsynth.typings.materialui.widgets.types.Mark
import react.RBuilder
import react.RComponent
import react.RHandler
import react.RProps
import react.RState
import kotlin.math.log
import kotlin.math.pow

class PlaybackCustomization(props: PlaybackCustomizationProps) : RComponent<PlaybackCustomizationProps, RState>(props) {
    override fun RBuilder.render() {
        bitsPerSampleDowncasting()
        tempoChange()
        synthesisSamplesPerSecondMultiplier()
        playbackSamplesPerSecondMultiplier()
    }

    private fun RBuilder.bitsPerSampleDowncasting() {
        materialTypography {
            attrs {
                style = kotlinext.js.js {
                    margin = "10px"
                }
            }
            +"Bits per sample (downcasting)"
        }
        materialSlider {
            attrs {
                min = 1
                max = 32
                value = props.synthesisParameters.downcastToBitsPerSample ?: 32
                marks = arrayOf(
                        Mark(1, "1 bit"),
                        Mark(8, "8 bits"),
                        Mark(16, "16 bits"),
                        Mark(24, "24 bits"),
                        Mark(32, "original"))
                valueLabelDisplay = "auto"
                onChange = { _, newValue ->
                    props.onSynthesisParametersChange(props.synthesisParameters.copy(
                            downcastToBitsPerSample = newValue.toInt().let { if (it != 32) it else null }))
                }
                style = kotlinext.js.js {
                    marginLeft = "20px"
                    width = "350px"
                }
            }
        }
    }

    private fun RBuilder.tempoChange() {
        materialTypography {
            attrs {
                style = kotlinext.js.js {
                    margin = "10px"
                }
            }
            +"Tempo change (beats-per-second offset)"
        }
        materialSlider {
            attrs {
                min = -100
                max = 100
                value = props.synthesisParameters.tempoOffset
                marks = arrayOf(
                        Mark(-100, "-100"),
                        Mark(-50, "-50"),
                        Mark(0, "original"),
                        Mark(50, "+50"),
                        Mark(100, "+100"))
                valueLabelDisplay = "auto"
                onChange = { _, newValue ->
                    props.onSynthesisParametersChange(props.synthesisParameters.copy(
                            tempoOffset = newValue.toInt()))
                }
                style = kotlinext.js.js {
                    marginLeft = "20px"
                    width = "350px"
                }
            }
        }
    }

    private fun RBuilder.synthesisSamplesPerSecondMultiplier() {
        materialTypography {
            attrs {
                style = kotlinext.js.js {
                    margin = "10px"
                }
            }
            +"Synthesis samples-per-second multiplier"
        }
        materialSlider {
            attrs {
                min = -2
                max = 2
                value = fromMultiplierToLogarithmicSliderValue(
                        props.synthesisParameters.synthesisSamplesPerSecondMultiplier)
                marks = arrayOf(
                        Mark(-2, "0.25x"),
                        Mark(-1, "0.5x"),
                        Mark(0, "original"),
                        Mark(1, "2x"),
                        Mark(2, "4x"))
                valueLabelDisplay = "auto"
                valueLabelFormat = { value -> "${fromLogarithmicSliderValueToMultiplier(value.toInt())}x" }
                onChange = { _, newValue ->
                    props.onSynthesisParametersChange(props.synthesisParameters.copy(
                            synthesisSamplesPerSecondMultiplier =
                            fromLogarithmicSliderValueToMultiplier(newValue.toInt())))
                }
                style = kotlinext.js.js {
                    marginLeft = "20px"
                    width = "350px"
                }
            }
        }
    }

    private fun RBuilder.playbackSamplesPerSecondMultiplier() {
        materialTypography {
            attrs {
                style = kotlinext.js.js {
                    margin = "10px"
                }
            }
            +"Playback samples-per-second multiplier"
        }
        materialSlider {
            attrs {
                min = -2
                max = 2
                value = fromMultiplierToLogarithmicSliderValue(
                        props.synthesisParameters.playbackSamplesPerSecondMultiplier)
                marks = arrayOf(
                        Mark(-2, "0.25x"),
                        Mark(-1, "0.5x"),
                        Mark(0, "original"),
                        Mark(1, "2x"),
                        Mark(2, "4x"))
                valueLabelDisplay = "auto"
                valueLabelFormat = { value -> "${fromLogarithmicSliderValueToMultiplier(value.toInt())}x" }
                onChange = { _, newValue ->
                    props.onSynthesisParametersChange(props.synthesisParameters.copy(
                            playbackSamplesPerSecondMultiplier =
                            fromLogarithmicSliderValueToMultiplier(newValue.toInt())))
                }
                style = kotlinext.js.js {
                    marginLeft = "20px"
                    width = "350px"
                }
            }
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
