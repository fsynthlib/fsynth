package it.krzeminski.fsynth.types

import kotlin.test.Test
import kotlin.test.assertEquals

class MusicNoteTest {
    @Test fun testFrequencyForA4() = assertEquals(440.0f, MusicNote.A4.frequency)
}