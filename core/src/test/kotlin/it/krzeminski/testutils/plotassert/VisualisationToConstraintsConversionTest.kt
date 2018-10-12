package it.krzeminski.testutils.plotassert

import it.krzeminski.testutils.plotassert.types.RawVisualisation
import it.krzeminski.testutils.plotassert.types.RawXAxis
import it.krzeminski.testutils.plotassert.types.VisualisationRow
import it.krzeminski.testutils.plotassert.types.constraints.Constraint
import it.krzeminski.testutils.plotassert.types.constraints.ExactValueConstraint
import it.krzeminski.testutils.plotassert.types.constraints.VerticalRangeConstraint
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.fail

class VisualisationToConstraintsConversionTest {
    @Test
    fun simpleLinearFunction() {
        assertEquals(
                actual = RawVisualisation(
                        visualisationRows = listOf(
                                VisualisationRow("  I", 4.0f),
                                VisualisationRow(" XI"),
                                VisualisationRow("X  ", 2.0f)
                        ),
                        xAxis = RawXAxis(
                                markers =        "| |",
                                values = listOf(-1.0f, 1.0f)))
                        .toConstraints(),
                expected = listOf(
                        Constraint(x = -1.0f, yValueConstraint = ExactValueConstraint(y = 2.0f)),
                        Constraint(x = 0.0f, yValueConstraint = ExactValueConstraint(y = 3.0f)),
                        Constraint(x = 1.0f, yValueConstraint = VerticalRangeConstraint(minY = 2.5f, maxY = 4.5f))
                )
        )
    }

    @Test
    fun someColumnsMissingConstraints() {
        assertEquals(
                actual = RawVisualisation(
                        visualisationRows = listOf(
                                VisualisationRow("   ", 4.0f),
                                VisualisationRow(" X "),
                                VisualisationRow("   ", 2.0f)
                        ),
                        xAxis = RawXAxis(
                                markers =        "| |",
                                values = listOf(-1.0f, 1.0f)))
                        .toConstraints(),
                expected = listOf(
                        Constraint(x = 0.0f, yValueConstraint = ExactValueConstraint(y = 3.0f))
                )
        )
    }

    @Test
    fun rowsHaveDifferentNumberOfCharacters() {
        assertFailsWith<IllegalArgumentException> {
            RawVisualisation(
                    visualisationRows = listOf(
                            VisualisationRow(" ", 4.0f),
                            VisualisationRow(" X"),
                            VisualisationRow("X  ", 2.0f)
                    ),
                    xAxis = RawXAxis(
                            markers = "| |",
                            values = listOf(-1.0f, 1.0f)))
                    .toConstraints()
        }.let { e ->
            assertEquals("Visualisation rows and the X axis markers string must have the same length!", e.message)
        }
    }

    @Test
    fun xAxisMarkersStringHasDifferentNumberOfCharacters() {
        assertFailsWith<IllegalArgumentException> {
            RawVisualisation(
                    visualisationRows = listOf(
                            VisualisationRow("  X", 4.0f),
                            VisualisationRow(" X "),
                            VisualisationRow("X  ", 2.0f)
                    ),
                    xAxis = RawXAxis(
                            markers =        "|    |",
                            values = listOf(-1.0f, 1.0f)))
                    .toConstraints()
        }.let { e ->
            assertEquals("Visualisation rows and the X axis markers string must have the same length!", e.message)
        }
    }

    @Test
    fun xAxisNotProvided() {
        assertFailsWith<IllegalArgumentException> {
            RawVisualisation(
                    visualisationRows = listOf(
                            VisualisationRow("  X", 4.0f),
                            VisualisationRow(" X "),
                            VisualisationRow("X  ", 2.0f)
                    ),
                    xAxis = null)
                    .toConstraints()
        }.let { e ->
            assertEquals("X axis should be given!", e.message)
        }
    }

    @Test
    fun xAxisMarkersNotProvided() {
        assertFailsWith<IllegalArgumentException> {
            RawVisualisation(
                    visualisationRows = listOf(
                            VisualisationRow("  X", 4.0f),
                            VisualisationRow(" X "),
                            VisualisationRow("X  ", 2.0f)
                    ),
                    xAxis = RawXAxis(markers = null))
                    .toConstraints()
        }.let { e ->
            assertEquals("X axis markers should be given!", e.message)
        }
    }
}
