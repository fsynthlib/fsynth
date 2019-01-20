package it.krzeminski.fsynth

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import it.krzeminski.fsynth.generated.gitInfo
import it.krzeminski.fsynth.songs.allSongs
import it.krzeminski.fsynth.songs.getSongByName
import java.time.Instant

fun main(args: Array<String>) = Synthesize().main(args)

private class Synthesize : CliktCommand(name = "fsynth") {
    val songName: String by option(
            "--song",
            help = "Name of the song to play. Available songs: ${getAvailableSongNames()}",
            metavar = "NAME")
            .required()

    override fun run() {
        printIntroduction()

        val songToPlay = getSongByName(songName)
        songToPlay?.playOnJvm(samplesPerSecond = 44100, sampleSizeInBits = 8)
                ?: println("Available songs: ${getAvailableSongNames()}")
    }
}

private fun printIntroduction() {
    println("fsynth by Piotr Krzemiński")
    println("Version ${gitInfo.latestCommit.sha1.substring(0, 8)} " +
            "from ${Instant.ofEpochSecond(gitInfo.latestCommit.timeUnixTimestamp)}")
}

private fun getAvailableSongNames() = allSongs.joinToString { "'${it.name}'" }
