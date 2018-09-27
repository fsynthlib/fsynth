package it.krzeminski.testutils.plotassert

import it.krzeminski.testutils.plotassert.types.AxisMarker
import it.krzeminski.testutils.plotassert.types.RawVisualisation
import it.krzeminski.testutils.plotassert.types.RawXAxis
import it.krzeminski.testutils.plotassert.types.VisualisationRow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class ReadingXAxisMarkersTest {
    @Test
    fun xAxisCommonCase() {
        assertEquals(
                readXAxisMarkers(
                        RawVisualisation(
                                visualisationRows = listOf(
                                        VisualisationRow("    X   ", 1.0f),
                                        VisualisationRow("   I  I ", -1.0f)
                                ),
                                xAxis = RawXAxis(
                                        markers = "  | |  |",
                                        values = listOf(-2.0f, 1.0f, 4.5f))
                        )
                ),
                listOf(
                        AxisMarker(value = -2.0f, characterIndex = 2),
                        AxisMarker(value = 1.0f, characterIndex = 4),
                        AxisMarker(value = 4.5f, characterIndex = 7)
                )
        )
    }

    @Test
    fun xAxisNumberOfMarkersNotEqualToNumberOfValues() {
        try {
            readXAxisMarkers(
                    RawVisualisation(
                            visualisationRows = listOf(
                                    VisualisationRow("    X   ", 1.0f),
                                    VisualisationRow("   I  I ", -1.0f)
                            ),
                            xAxis = RawXAxis(
                                    markers = "  | ",
                                    values = listOf(3.0f, 2.0f, 1.0f))
                    )
            )
            fail("It should throw ${IllegalArgumentException::class}!")
        } catch (e: IllegalArgumentException) {
            assertEquals(e.message, "X axis definition mismatch: found 1 marker(s) but 3 value(s)!")
        }
    }

    @Test
    fun xAxisValuesIncorrectOrder() {
        try {
            readXAxisMarkers(
                    RawVisualisation(
                            visualisationRows = listOf(
                                    VisualisationRow("    X   ", 1.0f),
                                    VisualisationRow("   I  I ", -1.0f)
                            ),
                            xAxis = RawXAxis(
                                    markers = "  | |  |",
                                    values = listOf(3.0f, 2.0f, 1.0f))
                    )
            )
            fail("It should throw ${IllegalArgumentException::class}!")
        } catch (e: IllegalArgumentException) {
            assertEquals(e.message, "Given X axis markers should have descending values (found: 3.0, 2.0)!")
        }
    }

    @Test
    fun xAxisMarkersStringIsNull() {
        try {
            readXAxisMarkers(
                    RawVisualisation(
                            visualisationRows = listOf(
                                    VisualisationRow("    X   ", 1.0f),
                                    VisualisationRow("   I  I ", -1.0f)
                            ),
                            xAxis = RawXAxis(
                                    markers = null,
                                    values = listOf(-2.0f, 1.0f, 4.5f))
                    )
            )
            fail("It should throw ${IllegalArgumentException::class}!")
        } catch (e: IllegalArgumentException) {
            assertEquals(e.message, "You must specify X axis markers!")
        }
    }

    @Test
    fun xAxisIsNull() {
        try {
            readXAxisMarkers(
                    RawVisualisation(
                            visualisationRows = listOf(
                                    VisualisationRow("    X   ", 1.0f),
                                    VisualisationRow("   I  I ", -1.0f)
                            ),
                            xAxis = null
                    )
            )
            fail("It should throw ${IllegalArgumentException::class}!")
        } catch (e: IllegalArgumentException) {
            assertEquals(e.message, "You must specify X axis!")
        }
    }

    @Test
    fun xAxisTooLittleValues() {
        try {
            readXAxisMarkers(
                    RawVisualisation(
                            visualisationRows = listOf(
                                    VisualisationRow("    X   ", 1.0f),
                                    VisualisationRow("   I  I ", -1.0f)
                            ),
                            xAxis = RawXAxis(
                                    markers = "  |     ",
                                    values = listOf(-2.0f))
                    )
            )
            fail("It should throw ${IllegalArgumentException::class}!")
        } catch (e: IllegalArgumentException) {
            assertEquals(e.message, "1 X axis marker(s) found, and there should be at least two!")
        }
    }

    @Test
    fun xAxisIllegalCharactersInMarkersString() {
        try {
            readXAxisMarkers(
                    RawVisualisation(
                            visualisationRows = listOf(
                                    VisualisationRow("    X   ", 1.0f),
                                    VisualisationRow("   I  I ", -1.0f)
                            ),
                            xAxis = RawXAxis(
                                    markers = " I| | X|",
                                    values = listOf(-2.0f, 1.0f, 4.5f))
                    )
            )
            fail("It should throw ${IllegalArgumentException::class}!")
        } catch (e: IllegalArgumentException) {
            assertEquals(e.message, "Illegal characters given in X axis markers string, only ('|', ' ') are allowed!")
        }
    }
}
