package it.krzeminski.testutils.plotassert

import it.krzeminski.testutils.plotassert.types.RawVisualisation
import it.krzeminski.testutils.plotassert.types.RawXAxis
import it.krzeminski.testutils.plotassert.types.VisualisationRow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ReadingRawVisualisationTest {
    @Test
    fun realLifeExample() {
        assertEquals(
                actual = readRawVisualisation {
                    row(1.0f,   "    X   ")
                    row(        "   I  I ")
                    row(0.0f,   " III  II")
                    row(        "   II I ")
                    row(-1.0f,  "    X   ")
                    xAxis {
                        markers("|     | ")
                        values( 0.0f, 1.0f)
                    }
                },
                expected = RawVisualisation(
                        visualisationRows = listOf(
                                VisualisationRow("    X   ", 1.0f),
                                VisualisationRow("   I  I "),
                                VisualisationRow(" III  II", 0.0f),
                                VisualisationRow("   II I "),
                                VisualisationRow("    X   ", -1.0f)
                        ),
                        xAxis = RawXAxis(
                                markers = "|     | ",
                                values = listOf(0.0f, 1.0f))
                )
        )
    }

    @Test
    fun randomIncorrectVisualisation() {
        assertEquals(
                actual = readRawVisualisation {
                    row(-12.0f, "foobar XXXXXXXXXXXXX")
                    row(5.3f, "random text (*@(")
                    xAxis {
                        markers("lol!!!!!!!!!")
                        values(0.0f, 0.0f, 0.0f)
                    }
                    row("LOREM IPSUM III XXX")
                },
                expected = RawVisualisation(
                        visualisationRows = listOf(
                                VisualisationRow("foobar XXXXXXXXXXXXX", -12.0f),
                                VisualisationRow("random text (*@(", 5.3f),
                                VisualisationRow("LOREM IPSUM III XXX")
                        ),
                        xAxis = RawXAxis(
                                markers = "lol!!!!!!!!!",
                                values = listOf(0.0f, 0.0f, 0.0f))
                )
        )
    }

    @Test
    fun noXAxisGiven() {
        assertEquals(
                actual = readRawVisualisation {
                    row(1.0f,  "    X   ")
                    row(       "   I  I ")
                    row(0.0f,  " III  II")
                    row(       "   II I ")
                    row(-1.0f, "    X   ")
                },
                expected = RawVisualisation(
                        visualisationRows = listOf(
                                VisualisationRow("    X   ", 1.0f),
                                VisualisationRow("   I  I "),
                                VisualisationRow(" III  II", 0.0f),
                                VisualisationRow("   II I "),
                                VisualisationRow("    X   ", -1.0f)
                        ),
                        xAxis = null)
                )
    }

    @Test
    fun nothingForXAxisGiven() {
        assertEquals(
                actual = readRawVisualisation {
                    row(1.0f,  "    X   ")
                    row(       "   I  I ")
                    row(0.0f,  " III  II")
                    row(       "   II I ")
                    row(-1.0f, "    X   ")
                    xAxis {
                    }
                },
                expected = RawVisualisation(
                        visualisationRows = listOf(
                                VisualisationRow("    X   ", 1.0f),
                                VisualisationRow("   I  I "),
                                VisualisationRow(" III  II", 0.0f),
                                VisualisationRow("   II I "),
                                VisualisationRow("    X   ", -1.0f)
                        ),
                        xAxis = RawXAxis())
        )
    }

    @Test
    fun noMarkersForXAxisGiven() {
        assertEquals(
                actual = readRawVisualisation {
                    row(1.0f,  "    X   ")
                    row(       "   I  I ")
                    row(0.0f,  " III  II")
                    row(       "   II I ")
                    row(-1.0f, "    X   ")
                    xAxis {
                        values(1.0f, 2.0f, 3.0f)
                    }
                },
                expected = RawVisualisation(
                        visualisationRows = listOf(
                                VisualisationRow("    X   ", 1.0f),
                                VisualisationRow("   I  I "),
                                VisualisationRow(" III  II", 0.0f),
                                VisualisationRow("   II I "),
                                VisualisationRow("    X   ", -1.0f)
                        ),
                        xAxis = RawXAxis(
                                values = listOf(1.0f, 2.0f, 3.0f)))
        )
    }

    @Test
    fun noValuesForXAxisGiven() {
        assertEquals(
                actual = readRawVisualisation {
                    row(1.0f,  "    X   ")
                    row(       "   I  I ")
                    row(0.0f,  " III  II")
                    row(       "   II I ")
                    row(-1.0f, "    X   ")
                    xAxis {
                        markers("|     | ")
                    }
                },
                expected = RawVisualisation(
                        visualisationRows = listOf(
                                VisualisationRow("    X   ", 1.0f),
                                VisualisationRow("   I  I "),
                                VisualisationRow(" III  II", 0.0f),
                                VisualisationRow("   II I "),
                                VisualisationRow("    X   ", -1.0f)
                        ),
                        xAxis = RawXAxis(
                                markers = "|     | "))
        )
    }

    @Test
    fun noRowsGiven() {
        assertEquals(
                actual = readRawVisualisation {
                    xAxis {
                        markers("|   |")
                        values(0.0f, 1.0f)
                    }
                },
                expected = RawVisualisation(
                        visualisationRows = emptyList(),
                        xAxis = RawXAxis(
                                markers = "|   |",
                                values = listOf(0.0f, 1.0f))
                )
        )
    }

    @Test
    fun nothingGiven() {
        assertEquals(
                actual = readRawVisualisation {
                },
                expected = RawVisualisation(
                        visualisationRows = emptyList(),
                        xAxis = null)
        )
    }

    @Test
    fun xAxisMarkersGivenMoreThanOnce() {
        assertFailsWith<IllegalArgumentException>("X axis markers given more than once!") {
            readRawVisualisation {
                row(1.0f,  "    X   ")
                row(       "   I  I ")
                row(0.0f,  " III  II")
                row(       "   II I ")
                row(-1.0f, "    X   ")
                xAxis {
                    markers("|      |")
                    markers("   |    ")
                    values(1.0f, 2.0f, 3.0f)
                }
            }
        }
    }

    @Test
    fun xAxisValuesGivenMoreThanOnce() {
        assertFailsWith<IllegalArgumentException>("X axis values given more than once!") {
            readRawVisualisation {
                row(1.0f,  "    X   ")
                row(       "   I  I ")
                row(0.0f,  " III  II")
                row(       "   II I ")
                row(-1.0f, "    X   ")
                xAxis {
                    markers("|      |")
                    values(1.0f, 2.0f, 3.0f)
                    values(4.0f, 5.0f, 6.0f)
                }
            }
        }
    }

    @Test
    fun xAxisGivenMoreThanOnce() {
        assertFailsWith<IllegalArgumentException>("X axis given more than once!") {
            readRawVisualisation {
                row(1.0f,  "    X   ")
                row(       "   I  I ")
                row(0.0f,  " III  II")
                row(       "   II I ")
                row(-1.0f, "    X   ")
                xAxis {
                    markers("|      |")
                    values(1.0f, 2.0f, 3.0f)
                }
                xAxis {
                    markers("|      |")
                    values(4.0f, 5.0f, 6.0f)
                }
            }
        }
    }
}
