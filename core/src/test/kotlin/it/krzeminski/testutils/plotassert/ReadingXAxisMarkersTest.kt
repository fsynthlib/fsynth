package it.krzeminski.testutils.plotassert

import it.krzeminski.testutils.plotassert.types.AxisMarker
import it.krzeminski.testutils.plotassert.types.RawVisualisation
import it.krzeminski.testutils.plotassert.types.RawXAxis
import it.krzeminski.testutils.plotassert.types.VisualisationRow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
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
        assertFailsWith<IllegalArgumentException> {
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
        }.let { e ->
            assertEquals("X axis definition mismatch: found 1 marker(s) but 3 value(s)!", e.message)
        }
    }

    @Test
    fun xAxisValuesIncorrectOrder() {
        assertFailsWith<IllegalArgumentException> {
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
        }.let { e ->
            assertTrue(e.message in setOf(
                    "Given X axis markers should have ascending values (found: 3.0, 2.0)!",
                    "Given X axis markers should have ascending values (found: 3, 2)!"))
        }
    }

    @Test
    fun xAxisTooLittleValues() {
        assertFailsWith<IllegalArgumentException> {
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
        }.let { e ->
            assertEquals("1 X axis marker(s) found, and there should be at least two!", e.message)
        }
    }

    @Test
    fun xAxisIllegalCharactersInMarkersString() {
        assertFailsWith<IllegalArgumentException> {
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
        }.let { e ->
            assertEquals("Illegal characters given in X axis markers string, only ('|', ' ') are allowed!", e.message)
        }
    }
}
