package it.krzeminski.testutils.plotassert.types.constraints

import it.krzeminski.testutils.plotassert.computeValueBounds
import it.krzeminski.testutils.plotassert.exceptions.FailedConstraintException
import it.krzeminski.testutils.plotassert.types.AxisMarker
import it.krzeminski.testutils.plotassert.types.VisualisationColumn

data class VerticalRangeConstraint(
        val minY: Float,
        val maxY: Float) : YValueConstraint()
{
    override fun assertMatches(yValue: Float) {
        if (yValue !in minY..maxY) {
            throw FailedConstraintException("$yValue is not between $minY and $maxY!")
        }
    }
}

object VerticalRangeConstraintBuilder : ConstraintBuilder()
{
    override fun columnMatchesThisConstraintType(column: VisualisationColumn): Boolean {
        val onlyLegalCharacters =  setOf(' ', 'I').containsAll(column.characters.groupBy { it }.keys)
        val noGapsBetweenLetters =
            column.characters
                    .mapIndexedNotNull { index, character -> if (character == 'I') index else null }
                    .zipWithNext { a, b -> b - a }
                    .all { difference -> difference == 1 }

        return onlyLegalCharacters && noGapsBetweenLetters
    }

    override fun buildConstraintFromColumn(
            column: VisualisationColumn, yAxisMarkers: List<AxisMarker>): YValueConstraint
    {
        val indexOfFirstCharacter = column.characters.indexOfFirst { it == 'I' }
        val indexOfLastCharacter = column.characters.indexOfLast { it == 'I' }
        val firstCharacterValueBounds = computeValueBounds(yAxisMarkers, indexOfFirstCharacter)
        val lastCharacterValueBounds = computeValueBounds(yAxisMarkers, indexOfLastCharacter)

        return VerticalRangeConstraint(
                minY = lastCharacterValueBounds.lowerBound, maxY = firstCharacterValueBounds.upperBound)
    }
}
