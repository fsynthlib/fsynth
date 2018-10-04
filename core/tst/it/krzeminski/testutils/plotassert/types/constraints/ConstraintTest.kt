package it.krzeminski.testutils.plotassert.types.constraints

import it.krzeminski.testutils.plotassert.exceptions.FailedConstraintException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class ConstraintTest {
    @Test
    fun ifConstraintMatches() {
        Constraint(123.45f, object : YValueConstraint() {
            override fun assertMatches(yValue: Float) {
                // Does not throw.
            }
        }).assertMatches { x -> x * 2.0f }
    }

    @Test
    fun ifConstraintDoesNotMatch() {
        try {
            Constraint(123.45f, object : YValueConstraint() {
                override fun assertMatches(yValue: Float) {
                    throw FailedConstraintException("Detailed message")
                }
            }).assertMatches { x -> x * 2.0f }
            fail("Should throw ${FailedConstraintException::class}!")
        } catch (e: FailedConstraintException) {
            assertEquals("For x=123.45: Detailed message", e.message)
        }
    }
}
