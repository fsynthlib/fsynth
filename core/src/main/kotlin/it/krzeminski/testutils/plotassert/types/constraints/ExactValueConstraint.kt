package it.krzeminski.testutils.plotassert.types.constraints

import it.krzeminski.testutils.plotassert.computeValueBounds
import it.krzeminski.testutils.plotassert.exceptions.FailedConstraintException
import it.krzeminski.testutils.plotassert.types.AxisMarker
import it.krzeminski.testutils.plotassert.types.VisualisationColumn

/**
 * Performs an "exactly equal" comparison. If the value differs even on the least significant digit, it's still reported
 * as not equal.
 */
data class ExactValueConstraint(
    private val y: Float
) : YValueConstraint() {
    override fun assertMatches(yValue: Float) {
        if (y != yValue) {
            throw FailedConstraintException("$yValue is not equal to $y!")
        }
    }
}

object ExactValueConstraintBuilder : ConstraintBuilder() {
    override fun columnMatchesThisConstraintType(column: VisualisationColumn): Boolean =
            with(column.characters.groupBy { it }) {
                return keys == setOf(' ', 'X') && this.getValue('X').size == 1
            }

    override fun buildConstraintFromColumn(
        column: VisualisationColumn,
        yAxisMarkers: List<AxisMarker>
    ): YValueConstraint
    {
        val indexOfXCharacter = column.characters.indexOf('X')
        val valueBounds = computeValueBounds(yAxisMarkers, indexOfXCharacter)

        return ExactValueConstraint(valueBounds.center)
    }
}
