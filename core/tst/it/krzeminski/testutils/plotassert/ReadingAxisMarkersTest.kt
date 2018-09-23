package it.krzeminski.testutils.plotassert

import it.krzeminski.testutils.plotassert.types.AxisMarker
import it.krzeminski.testutils.plotassert.types.RawVisualisation
import it.krzeminski.testutils.plotassert.types.VisualisationRow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class ReadingAxisMarkersTest {
    @Test
    fun yAxisCommonCase() {
        assertEquals(
                readYAxisMarkers(
                        RawVisualisation(
                                visualisationRows = listOf(
                                        VisualisationRow("    X   ", 1.0f),
                                        VisualisationRow("   I  I "),
                                        VisualisationRow(" III  II", 0.0f),
                                        VisualisationRow("   II I "),
                                        VisualisationRow("    X   ", -1.0f),
                                        VisualisationRow("    X   "),
                                        VisualisationRow("+      +")
                                ),
                                xAxisMarkerValues = listOf(0.0f, 1.0f)
                        )
                ),
                listOf(
                        AxisMarker(value = 1.0f, characterIndex = 0),
                        AxisMarker(value = 0.0f, characterIndex = 2),
                        AxisMarker(value = -1.0f, characterIndex = 4)
                )
        )
    }

    @Test
    fun yAxisRepeatedValue() {
        try {
            readYAxisMarkers(
                    RawVisualisation(
                            visualisationRows = listOf(
                                    VisualisationRow("    X   ", 1.0f),
                                    VisualisationRow(" III  II", 1.0f)
                            ),
                            xAxisMarkerValues = listOf(0.0f, 1.0f)
                    )
            )
            fail("It should throw ${IllegalArgumentException::class}!")
        } catch (e: IllegalArgumentException) {
            assertEquals(e.message, "Given Y axis markers should have descending values (found: 1.0, 1.0)!")
        }
    }

    @Test
    fun yAxisIncorrectOrder() {
        try {
            readYAxisMarkers(
                    RawVisualisation(
                            visualisationRows = listOf(
                                    VisualisationRow("    X   ", 4.0f),
                                    VisualisationRow(" III  II", 9.0f),
                                    VisualisationRow(" III  II", 2.0f)
                            ),
                            xAxisMarkerValues = listOf(0.0f, 1.0f)
                    )
            )
            fail("It should throw ${IllegalArgumentException::class}!")
        } catch (e: IllegalArgumentException) {
            assertEquals(e.message, "Given Y axis markers should have descending values (found: 4.0, 9.0)!")
        }
    }

    @Test
    fun yAxisNoMarkers() {
        try {
            readYAxisMarkers(
                    RawVisualisation(
                            visualisationRows = listOf(
                                    VisualisationRow("    X   "),
                                    VisualisationRow(" III  II")
                            ),
                            xAxisMarkerValues = listOf(0.0f, 1.0f)
                    )
            )
            fail("It should throw ${IllegalArgumentException::class}!")
        } catch (e: IllegalArgumentException) {
            assertEquals(e.message, "0 Y axis marker(s) found, and there should be at least two!")
        }
    }

    @Test
    fun yAxisOneMarker() {
        try {
            readYAxisMarkers(
                    RawVisualisation(
                            visualisationRows = listOf(
                                    VisualisationRow("    X   "),
                                    VisualisationRow(" III  II", 3.0f)
                            ),
                            xAxisMarkerValues = listOf(0.0f, 1.0f)
                    )
            )
            fail("It should throw ${IllegalArgumentException::class}!")
        } catch (e: IllegalArgumentException) {
            assertEquals(e.message, "1 Y axis marker(s) found, and there should be at least two!")
        }
    }
}
