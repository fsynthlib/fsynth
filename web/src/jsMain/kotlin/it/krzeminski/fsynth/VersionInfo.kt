package it.krzeminski.fsynth

import it.krzeminski.fsynth.generated.gitInfo
import it.krzeminski.gitinfo.types.GitInfo
import kotlin.js.Date
import react.FC
import react.Props
import react.dom.html.ReactHTML.a
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.span

val VersionInfo: FC<VersionInfoProps> = FC { props ->
    div {
        style = js("({ color: '#555', fontSize: '0.8rem', textAlign: 'center', marginTop: '10px' })").unsafeCast<csstype.Properties>()
        +"Version "
        span {
            style = js("({ fontFamily: '\"monospace\", sans-serif' })").unsafeCast<csstype.Properties>()
            a {
                href = "https://github.com/fsynthlib/fsynth/commit/${gitInfo.latestCommit.sha1}"
                +gitInfo.latestCommit.sha1.substring(0, 8)
            }
        }
        +" from ${Date(gitInfo.latestCommit.timeUnixTimestamp * 1000).toUTCString()}"
    }
}

external interface VersionInfoProps : Props {
    var gitInfo: GitInfo
}
