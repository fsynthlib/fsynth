package it.krzeminski.testutils.plotassert.types.constraints

import it.krzeminski.testutils.plotassert.types.AxisMarker
import it.krzeminski.testutils.plotassert.types.VisualisationColumn
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ExactValueConstraintTest {
    @Test
    fun singleXCharacterCheckIfMatches() {
        assertTrue(ExactValueConstraintBuilder.columnMatchesThisConstraintType(
                VisualisationColumn("   X ")))
    }

    @Test
    fun manyXCharactersCheckIfMatches() {
        assertFalse(ExactValueConstraintBuilder.columnMatchesThisConstraintType(
                VisualisationColumn("  XX ")))
    }

    @Test
    fun mixedCharactersCheckIfMatches() {
        assertFalse(ExactValueConstraintBuilder.columnMatchesThisConstraintType(
                VisualisationColumn(" IIX ")))
    }

    @Test
    fun noXCharacterCheckIfMatches() {
        assertFalse(ExactValueConstraintBuilder.columnMatchesThisConstraintType(
                VisualisationColumn("   I ")))
    }

    @Test
    fun singleXCharacterBuildConstraint() {
        assertEquals(
                actual = ExactValueConstraintBuilder.buildConstraintFromColumn(
                        column = VisualisationColumn("   X "),
                        yAxisMarkers = listOf(AxisMarker(5.0f, 0), AxisMarker(1.0f, 4))),
                expected = ExactValueConstraint(2.0f))
    }
}
