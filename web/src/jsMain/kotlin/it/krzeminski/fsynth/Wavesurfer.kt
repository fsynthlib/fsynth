package it.krzeminski.fsynth

import it.krzeminski.fsynth.typings.WaveSurfer
import it.krzeminski.fsynth.typings.WaveSurferOptions
import org.w3c.files.Blob
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.useEffect
import react.useRef
import web.dom.ElementId

val Wavesurfer: FC<WavesurferProps> = FC { props ->
    val waveSurferRef = useRef<WaveSurfer>(null)

    useEffect {
        val ws = WaveSurfer(js("({ container: '#waveform', height: 80, cursorWidth: 1, progressColor: '#3F51B5', waveColor: '#EFEFEF' })").unsafeCast<WaveSurferOptions>())
        ws.loadBlob(props.waveData)
        ws.on("ready") {
            ws.play()
        }
        waveSurferRef.current = ws
    }

    useEffect(props.waveData) {
        waveSurferRef.current?.let { ws ->
            ws.loadBlob(props.waveData)
            ws.on("ready") {
                ws.play()
            }
        }
    }

    div {
        style = js("({ width: '380px', margin: '10px 0px', height: '80px' })").unsafeCast<csstype.Properties>()
        id = "waveform".unsafeCast<ElementId>()
    }
}

external interface WavesurferProps : Props {
    var waveData: Blob
}
