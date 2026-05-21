package it.krzeminski.fsynth

import it.krzeminski.fsynth.types.SynthesisParameters
import mui.material.Slider
import mui.material.Typography
import mui.system.SxProps
import mui.system.Theme
import react.FC
import react.Props
import kotlin.math.log
import kotlin.math.pow

val PlaybackCustomization: FC<PlaybackCustomizationProps> = FC { props ->
    Typography {
        sx = js("({ marginLeft: '10px' })").unsafeCast<SxProps<Theme>>()
        +"Bits per sample (downcasting)"
    }
    Slider {
        min = 1.0
        max = 32.0
        value = (props.synthesisParameters.downcastToBitsPerSample ?: 32).toDouble()
        marks = js("""([{ value: 1, label: '1 bit' }, { value: 8, label: '8 bits' }, { value: 16, label: '16 bits' }, { value: 24, label: '24 bits' }, { value: 32, label: 'original' }])""")
        valueLabelDisplay = "auto"
        onChange = { _, values, _ ->
            val v = values[0].toInt()
            props.onSynthesisParametersChange(props.synthesisParameters.copy(
                    downcastToBitsPerSample = if (v != 32) v else null))
        }
        sx = js("({ marginLeft: '35px' })").unsafeCast<SxProps<Theme>>()
    }

    Typography {
        sx = js("({ marginLeft: '10px' })").unsafeCast<SxProps<Theme>>()
        +"Tempo change (beats-per-second offset)"
    }
    Slider {
        min = -100.0
        max = 100.0
        value = props.synthesisParameters.tempoOffset.toDouble()
        marks = js("""([{ value: -100, label: '-100' }, { value: -50, label: '-50' }, { value: 0, label: 'original' }, { value: 50, label: '+50' }, { value: 100, label: '+100' }])""")
        valueLabelDisplay = "auto"
        onChange = { _, values, _ ->
            props.onSynthesisParametersChange(props.synthesisParameters.copy(
                    tempoOffset = values[0].toInt()))
        }
        sx = js("({ marginLeft: '35px' })").unsafeCast<SxProps<Theme>>()
    }

    Typography {
        sx = js("({ marginLeft: '10px' })").unsafeCast<SxProps<Theme>>()
        +"Synthesis samples-per-second multiplier"
    }
    Slider {
        min = -2.0
        max = 2.0
        value = fromMultiplierToLogarithmicSliderValue(props.synthesisParameters.synthesisSamplesPerSecondMultiplier).toDouble()
        marks = js("""([{ value: -2, label: '0.25x' }, { value: -1, label: '0.5x' }, { value: 0, label: 'original' }, { value: 1, label: '2x' }, { value: 2, label: '4x' }])""")
        valueLabelDisplay = "auto"
        onChange = { _, values, _ ->
            props.onSynthesisParametersChange(props.synthesisParameters.copy(
                    synthesisSamplesPerSecondMultiplier =
                    fromLogarithmicSliderValueToMultiplier(values[0].toInt())))
        }
        sx = js("({ marginLeft: '35px' })").unsafeCast<SxProps<Theme>>()
    }

    Typography {
        sx = js("({ marginLeft: '10px' })").unsafeCast<SxProps<Theme>>()
        +"Playback samples-per-second multiplier"
    }
    Slider {
        min = -2.0
        max = 2.0
        value = fromMultiplierToLogarithmicSliderValue(props.synthesisParameters.playbackSamplesPerSecondMultiplier).toDouble()
        marks = js("""([{ value: -2, label: '0.25x' }, { value: -1, label: '0.5x' }, { value: 0, label: 'original' }, { value: 1, label: '2x' }, { value: 2, label: '4x' }])""")
        valueLabelDisplay = "auto"
        onChange = { _, values, _ ->
            props.onSynthesisParametersChange(props.synthesisParameters.copy(
                    playbackSamplesPerSecondMultiplier =
                    fromLogarithmicSliderValueToMultiplier(values[0].toInt())))
        }
        sx = js("({ marginLeft: '35px' })").unsafeCast<SxProps<Theme>>()
    }
}

private fun fromMultiplierToLogarithmicSliderValue(multiplier: Float) =
        log(multiplier, 2.0f)

private fun fromLogarithmicSliderValueToMultiplier(sliderValue: Int) =
        2.0f.pow(sliderValue.toFloat())

external interface PlaybackCustomizationProps : Props {
    var synthesisParameters: SynthesisParameters
    var onSynthesisParametersChange: (SynthesisParameters) -> Unit
}
