package it.krzeminski.fsynth

import it.krzeminski.fsynth.typings.WaveSurfer
import kotlinx.css.height
import kotlinx.css.margin
import kotlinx.css.px
import kotlinx.css.width
import kotlinx.html.id
import kotlinext.js.js
import org.w3c.files.Blob
import react.RBuilder
import react.RComponent
import react.RHandler
import react.RProps
import react.RState
import styled.css
import styled.styledDiv

class Wavesurfer(props: WavesurferProps) : RComponent<WavesurferProps, RState>(props) {
    lateinit var waveSurfer: WaveSurfer

    override fun componentDidMount() {
        waveSurfer = WaveSurfer(js {
            container = "#waveform"
            height = 80
            cursorWidth = 1
            progressColor = "#3F51B5"
            waveColor = "#EFEFEF"
        })
        waveSurfer.init()
        props.waveData.let {
            waveSurfer.loadBlob(it)
            waveSurfer.on("ready") {
                waveSurfer.play()
            }
        }
    }

    override fun componentDidUpdate(prevProps: WavesurferProps, prevState: RState, snapshot: Any) {
        if (!shouldReloadAudio(prevProps)) {
            return
        }
        waveSurfer.loadBlob(props.waveData)
        waveSurfer.on("ready") {
            waveSurfer.play()
        }
    }

    override fun RBuilder.render() {
        styledDiv {
            css {
                width = 380.px
                margin(horizontal = 0.px, vertical = 10.px)
                height = 80.px
            }
            attrs.id = "waveform"
        }
    }

    private fun shouldReloadAudio(prevProps: WavesurferProps): Boolean {
        return prevProps.waveData != props.waveData
    }
}

external interface WavesurferProps : RProps {
    var waveData: Blob
}

fun RBuilder.wavesurfer(handler: RHandler<WavesurferProps>) =
        child(Wavesurfer::class) {
            handler()
        }
