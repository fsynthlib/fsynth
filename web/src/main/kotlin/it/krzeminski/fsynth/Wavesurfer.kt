package it.krzeminski.fsynth

import it.krzeminski.fsynth.typings.WaveSurfer
import kotlinext.js.js
import kotlinx.html.id
import kotlinx.html.style
import org.w3c.files.Blob
import react.RBuilder
import react.RComponent
import react.RHandler
import react.RProps
import react.RState
import react.dom.div
import kotlin.browser.window

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
            // HACK: if there's no timeout, the playback doesn't start. Maybe because the component isn't rendered yet.
            window.setTimeout({ waveSurfer.play() }, 300)
        }
    }

    override fun componentDidUpdate(prevProps: WavesurferProps, prevState: RState, snapshot: Any) {
        if (!shouldReloadAudio(prevProps)) {
            return
        }
        waveSurfer.loadBlob(props.waveData)
        // HACK: if there's no timeout, the playback starts for a fraction of a second and the stops.
        window.setTimeout({ waveSurfer.play() }, 300)
    }

    override fun RBuilder.render() {
        div {
            div {
                attrs {
                    id = "waveform"
                    style = js {
                        width = "100%"
                        height = "80px"
                    }
                }
            }
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
