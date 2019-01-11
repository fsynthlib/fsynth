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
        assertFailsWith<IllegalArgumentException> {
            readRawVisualisation {
                row(1.0f,  "    X   ")
                row(       "   I  I ")
                row(0.0f,  " III  II")
                row(       "   II I ")
                row(-1.0f, "    X   ")
            }
        }.let { e ->
            assertEquals("X axis not given!", e.message)
        }
    }

    @Test
    fun nothingForXAxisGiven() {
        assertFailsWith<IllegalArgumentException> {
            readRawVisualisation {
                row(1.0f,  "    X   ")
                row(       "   I  I ")
                row(0.0f,  " III  II")
                row(       "   II I ")
                row(-1.0f, "    X   ")
                xAxis {
                }
            }
        }.let { e ->
            assertEquals("X axis markers not given!", e.message)
        }
    }

    @Test
    fun noMarkersForXAxisGiven() {
        assertFailsWith<IllegalArgumentException> {
            readRawVisualisation {
                row(1.0f,  "    X   ")
                row(       "   I  I ")
                row(0.0f,  " III  II")
                row(       "   II I ")
                row(-1.0f, "    X   ")
                xAxis {
                    values(1.0f, 2.0f, 3.0f)
                }
            }
        }.let { e ->
            assertEquals("X axis markers not given!", e.message)
        }
    }

    @Test
    fun noValuesForXAxisGiven() {
        assertFailsWith<IllegalArgumentException> {
            readRawVisualisation {
                row(1.0f,  "    X   ")
                row(       "   I  I ")
                row(0.0f,  " III  II")
                row(       "   II I ")
                row(-1.0f, "    X   ")
                xAxis {
                    markers("|     | ")
                }
            }
        }.let { e ->
            assertEquals("X axis values not given!", e.message)
        }
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
        assertFailsWith<IllegalArgumentException> {
            readRawVisualisation {
            }
        }.let { e ->
            assertEquals("X axis not given!", e.message)
        }
    }

    @Test
    fun xAxisMarkersGivenMoreThanOnce() {
        assertFailsWith<IllegalArgumentException> {
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
        }.let { e ->
            assertEquals("X axis markers given more than once!", e.message)
        }
    }

    @Test
    fun xAxisValuesGivenMoreThanOnce() {
        assertFailsWith<IllegalArgumentException> {
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
        }.let { e ->
            assertEquals("X axis values given more than once!", e.message)
        }
    }

    @Test
    fun xAxisGivenMoreThanOnce() {
        assertFailsWith<IllegalArgumentException> {
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
        }.let { e ->
            assertEquals("X axis given more than once!", e.message)
        }
    }
}
