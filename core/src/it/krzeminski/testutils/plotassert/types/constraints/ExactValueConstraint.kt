package it.krzeminski.testutils.plotassert.types.constraints

import it.krzeminski.testutils.plotassert.computeValueBounds
import it.krzeminski.testutils.plotassert.exceptions.FailedConstraintException
import it.krzeminski.testutils.plotassert.types.AxisMarker
import it.krzeminski.testutils.plotassert.types.VisualisationColumn
import it.krzeminski.utils.almostEqualTo

/**
 * Performs an "almost equal" comparison.
 * Sometimes, when the user performs several operations on float numbers, the values get a bit off. This assertion is
 * meant to reflect it, actually checking if the values are really close to each other (~0.000001 of difference).
 */
data class ExactValueConstraint(
        val y: Float) : YValueConstraint()
{
    override fun assertMatches(yValue: Float) {
        if (!(y almostEqualTo yValue)) {
            throw FailedConstraintException("$yValue is not equal to $y!")
        }
    }
}

object ExactValueConstraintBuilder : ConstraintBuilder()
{
    override fun columnMatchesThisConstraintType(column: VisualisationColumn): Boolean =
            with (column.characters.groupBy { it }) {
                return keys == setOf(' ', 'X') && this['X']?.size == 1
            }

    override fun buildConstraintFromColumn(
            column: VisualisationColumn, yAxisMarkers: List<AxisMarker>): YValueConstraint
    {
        val indexOfXCharacter = column.characters.indexOf('X')
        val valueBounds = computeValueBounds(yAxisMarkers, indexOfXCharacter)

        return ExactValueConstraint(valueBounds.center)
    }
}
