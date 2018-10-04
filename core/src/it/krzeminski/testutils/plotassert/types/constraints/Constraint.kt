package it.krzeminski.testutils.plotassert.types.constraints

import it.krzeminski.testutils.plotassert.exceptions.FailedConstraintException

data class Constraint(
        val x: Float,
        val yValueConstraint: YValueConstraint)
{
    fun assertMatches(functionUnderTest: (Float) -> Float) {
        try {
            yValueConstraint.assertMatches(functionUnderTest(x))
        } catch (e: FailedConstraintException) {
            throw FailedConstraintException("For x=$x: ${e.message}")
        }
    }
}
