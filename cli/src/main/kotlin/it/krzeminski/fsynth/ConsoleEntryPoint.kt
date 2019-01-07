package it.krzeminski.fsynth

import it.krzeminski.fsynth.generated.gitInfo
import it.krzeminski.fsynth.songs.allSongs
import it.krzeminski.fsynth.songs.getSongByName
import it.krzeminski.fsynth.types.Song
import java.time.Instant

fun main(args: Array<String>) {
    printIntroduction()

    val nameOfSongToPlay : String? = if (args.isNotEmpty()) args[0] else null
    val songToPlay : Song? = nameOfSongToPlay?.let { getSongByName(nameOfSongToPlay) }

    songToPlay?.playOnJvm(samplesPerSecond = 44100, sampleSizeInBits = 8)
            ?: println("Available songs: ${allSongs.joinToString { "'${it.name}'" }}")
}

private fun printIntroduction() {
    println("fsynth by Piotr Krzemiński")
    println("Version ${gitInfo.latestCommit.sha1.substring(0, 8)} " +
            "from ${Instant.ofEpochSecond(gitInfo.latestCommit.timeUnixTimestamp)}")
}
