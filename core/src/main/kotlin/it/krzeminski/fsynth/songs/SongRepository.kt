package it.krzeminski.fsynth.songs

// It has to be defined as a lazily initialized structure, not just a list.
// There's a bug in Kotlin JS that for a regular list, can generate an incorrect JavaScript for initializing the list.
// The order which Kotlin JS takes into account is defined by the names of the files where the objects are defined,
// and in what order they appear when sorted alphabetically. Because of this, this very file appears before
// 'VanHalenJumpIntro.kt', so 'vanHalenJumpIntro' constant was not yet defined, and in 'allSongs' list, the list
// contained 'simpleDemoSong' and 'undefined'.
val allSongs by lazy {
    listOf(
            simpleDemoSong,
            vanHalenJumpIntro,
            pinkPantherThemeIntro)
}
