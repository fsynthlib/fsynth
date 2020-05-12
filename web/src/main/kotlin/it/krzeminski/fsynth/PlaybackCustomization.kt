package it.krzeminski.fsynth

import it.krzeminski.fsynth.typings.materialSlider
import it.krzeminski.fsynth.typings.materialTypography
import it.krzeminski.fsynth.typings.materialui.widgets.types.Mark
import react.RBuilder
import react.RComponent
import react.RHandler
import react.RProps
import react.RState

class PlaybackCustomization(props: PlaybackCustomizationProps) : RComponent<PlaybackCustomizationProps, RState>(props) {
    override fun RBuilder.render() {
        bitsPerSampleDowncasting()
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
                value = props.downcastToBitsPerSample ?: 32
                marks = arrayOf(
                        Mark(1, "1 bit"),
                        Mark(8, "8 bits"),
                        Mark(16, "16 bits"),
                        Mark(24, "24 bits"),
                        Mark(32, "original"))
                valueLabelDisplay = "auto"
                onChange = { _, newValue ->
                    props.onDowncastToBitsPerSampleChange(newValue.toInt().let { if (it != 32) it else null })
                }
                style = kotlinext.js.js {
                    marginLeft = "20px"
                    width = "350px"
                }
            }
        }
    }
}

external interface PlaybackCustomizationProps : RProps {
    var downcastToBitsPerSample: Int?
    var onDowncastToBitsPerSampleChange: (Int?) -> Unit
}

fun RBuilder.playbackCustomization(handler: RHandler<PlaybackCustomizationProps>) =
        child(PlaybackCustomization::class) {
            handler()
        }
