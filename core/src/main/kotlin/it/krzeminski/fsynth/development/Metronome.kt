package it.krzeminski.fsynth.development

import it.krzeminski.fsynth.instruments.synthesizer
import it.krzeminski.fsynth.types.MusicNote.C4
import it.krzeminski.fsynth.types.MusicNote.C5
import it.krzeminski.fsynth.types.NoteValue
import it.krzeminski.fsynth.types.Song
import it.krzeminski.fsynth.types.Track
import it.krzeminski.fsynth.types.TrackSegment

fun Song.add4by4MetronomeTrackForDevelopment(trackSegments: Int) =
        this.copy(tracks = tracks + Track(
                name = "Metronome",
                instrument = ::synthesizer,
                volume = 0.15f,
                segments = generateTrackSegments(trackSegments)))

private fun generateTrackSegments(trackSegments: Int) =
        (0..(trackSegments-1)).asSequence()
                .flatMap {
                    listOf(C5, C4, C4, C4)
                            .flatMap { note ->
                                listOf(
                                        TrackSegment.SingleNote(NoteValue(1, 32), note),
                                        TrackSegment.Pause(NoteValue(7, 32)))
                            }
                            .asSequence()
                }
                .toList()
