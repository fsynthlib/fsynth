package it.krzeminski.fsynth

import it.krzeminski.fsynth.songs.allSongs
import it.krzeminski.fsynth.songs.getSongByName
import it.krzeminski.fsynth.types.Song

fun main(args: Array<String>) {
    configureOutputFormat()

    val nameOfSongToPlay : String? = if (args.isNotEmpty()) args[0] else null
    val songToPlay : Song? = nameOfSongToPlay?.let { getSongByName(nameOfSongToPlay) }

    songToPlay?.let {
        render8bit(song = it, sampleRate = 44100)
    } ?: println("Available songs: ${allSongs.map { "\"${it.name}\"" }}")
}
