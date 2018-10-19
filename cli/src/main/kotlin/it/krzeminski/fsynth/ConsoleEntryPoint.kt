package it.krzeminski.fsynth

import it.krzeminski.fsynth.songs.allSongs
import it.krzeminski.fsynth.songs.getSongByName
import it.krzeminski.fsynth.types.Song

fun main(args: Array<String>) {
    val nameOfSongToPlay : String? = if (args.isNotEmpty()) args[0] else null
    val songToPlay : Song? = nameOfSongToPlay?.let { getSongByName(nameOfSongToPlay) }

    songToPlay?.playOnJvm(samplesPerSecond = 44100, sampleSizeInBits = 8)
            ?: println("Available songs: ${allSongs.joinToString { "'${it.name}'" }}")
}
