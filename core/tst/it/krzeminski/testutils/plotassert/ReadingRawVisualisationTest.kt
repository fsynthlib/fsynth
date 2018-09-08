package it.krzeminski.testutils.plotassert

import it.krzeminski.testutils.plotassert.types.RawVisualisation
import it.krzeminski.testutils.plotassert.types.VisualisationRow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ReadingRawVisualisationTest {
    @Test
    fun realLifeExample() {
        assertEquals(
                readRawVisualisation {
                    row(1.0f, "    X   ")
                    row("   I  I ")
                    row(0.0f, " III  II")
                    row("   II I ")
                    row(-1.0f, "    X   ")
                    row("+      +")
                    x(0.0f, 1.0f)
                },
                RawVisualisation(
                        visualisationRows = listOf(
                                VisualisationRow("    X   ", 1.0f),
                                VisualisationRow("   I  I "),
                                VisualisationRow(" III  II", 0.0f),
                                VisualisationRow("   II I "),
                                VisualisationRow("    X   ", -1.0f),
                                VisualisationRow("+      +")
                        ),
                        xAxisMarkerValues = listOf(0.0f, 1.0f)
                )
        )
    }

    @Test
    fun randomIncorrectVisualisation() {
        assertEquals(
                readRawVisualisation {
                    row(-12.0f, "foobar XXXXXXXXXXXXX")
                    row(5.3f, "random text (*@(")
                    x(0.0f, 0.0f, 0.0f)
                    row("LOREM IPSUM III XXX")
                },
                RawVisualisation(
                        visualisationRows = listOf(
                                VisualisationRow("foobar XXXXXXXXXXXXX", -12.0f),
                                VisualisationRow("random text (*@(", 5.3f),
                                VisualisationRow("LOREM IPSUM III XXX")
                        ),
                        xAxisMarkerValues = listOf(0.0f, 0.0f, 0.0f)
                )
        )
    }

    @Test
    fun noXMarkersGiven() {
        assertEquals(
                readRawVisualisation {
                    row(1.0f, "    X   ")
                    row("   I  I ")
                    row(0.0f, " III  II")
                    row("   II I ")
                    row(-1.0f, "    X   ")
                    row("+      +")
                },
                RawVisualisation(
                        visualisationRows = listOf(
                                VisualisationRow("    X   ", 1.0f),
                                VisualisationRow("   I  I "),
                                VisualisationRow(" III  II", 0.0f),
                                VisualisationRow("   II I "),
                                VisualisationRow("    X   ", -1.0f),
                                VisualisationRow("+      +")
                        ),
                        xAxisMarkerValues = emptyList()
                )
        )
    }

    @Test
    fun noRowsGiven() {
        assertEquals(
                readRawVisualisation {
                    x(0.0f, 1.0f)
                },
                RawVisualisation(
                        visualisationRows = emptyList(),
                        xAxisMarkerValues = listOf(0.0f, 1.0f)
                )
        )
    }

    @Test
    fun nothingGiven() {
        assertEquals(
                readRawVisualisation {
                },
                RawVisualisation(
                        visualisationRows = emptyList(),
                        xAxisMarkerValues = emptyList()
                )
        )
    }

    @Test
    fun xAxisMarkersGivenMoreThanOnce() {
        assertFailsWith<IllegalArgumentException>("X axis markers given more than once!") {
            readRawVisualisation {
                row(1.0f, "    X   ")
                row("   I  I ")
                row(0.0f, " III  II")
                row("   II I ")
                row(-1.0f, "    X   ")
                x(1.0f, 2.0f, 3.0f)
                x(4.0f, 5.0f, 6.0f)
            }
        }
    }
}
