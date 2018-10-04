package it.krzeminski.testutils.plotassert

import it.krzeminski.testutils.plotassert.exceptions.FailedConstraintException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class AssertionsTest {
    @Test
    fun assertFunctionConformsToWhenAssertionsAreFulfilled() {
        assertFunctionConformsTo(
                functionUnderTest = { 1.0f },
                visualisation = {
                    row(1.0f,   "XXXXX")
                    row(0.0f,   "     ")
                    xAxis {
                        markers("|   |")
                        values(1.0f, 2.0f)
                    }
                }
        )
    }

    @Test
    fun assertFunctionConformsToWhenOneAssertionFails() {
        try {
            assertFunctionConformsTo(
                    functionUnderTest = { 1.0f },
                    visualisation = {
                        row(1.0f,   "X XXX")
                        row(0.0f,   " X   ")
                        xAxis {
                            markers("|   |")
                            values(1.0f, 2.0f)
                        }
                    }
            )
            fail("Should throw ${FailedConstraintException::class}!")
        } catch (e: FailedConstraintException) {
            assertEquals("For x=1.25: 1.0 is not equal to 0.0!", e.message)
        }
    }
}
