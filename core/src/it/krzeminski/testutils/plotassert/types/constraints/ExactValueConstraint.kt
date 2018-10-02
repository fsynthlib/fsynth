package it.krzeminski.testutils.plotassert.types.constraints

import it.krzeminski.testutils.plotassert.computeValueBounds
import it.krzeminski.testutils.plotassert.types.AxisMarker
import it.krzeminski.testutils.plotassert.types.VisualisationColumn

data class ExactValueConstraint(
        val y: Float) : YValueConstraint()

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
