package it.krzeminski.testutils.plotassert

import it.krzeminski.testutils.plotassert.types.AxisMarker
import it.krzeminski.testutils.plotassert.types.VisualisationColumn
import it.krzeminski.testutils.plotassert.types.constraints.YValueConstraint
import it.krzeminski.testutils.plotassert.types.constraints.ConstraintBuilder
import it.krzeminski.testutils.plotassert.types.constraints.getAvailableConstraintBuilders

/**
 * Returns null when the given column doesn't specify any constraint.
 */
fun mapVisualisationColumnToConstraint(
        column: VisualisationColumn,
        yAxisMarkers: List<AxisMarker>,
        getAvailableConstraintBuilders:
        () -> List<ConstraintBuilder> = ::getAvailableConstraintBuilders): YValueConstraint?
{
    if (onlySpaces(column)) {
        return null
    }
    val buildersAcceptingThisColumn = getAvailableConstraintBuilders().filter { builder ->
        builder.columnMatchesThisConstraintType(column)
    }
    require(buildersAcceptingThisColumn.size == 1) {
        if (buildersAcceptingThisColumn.size > 1) {
            "Ambiguous constraint; more than 1 constraint type matches: $buildersAcceptingThisColumn"
        } else {
            "No constraints match this visualisation column: ${column.characters}"
        }
    }
    val builderToUse = buildersAcceptingThisColumn.first()
    return builderToUse.buildConstraintFromColumn(column, yAxisMarkers)
}

private fun onlySpaces(column: VisualisationColumn) =
        column.characters.groupBy { it }.keys == setOf(' ')
