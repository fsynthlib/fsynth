package it.krzeminski.testutils.plotassert.exceptions

class FailedConstraintException(override val message: String, override val cause: Throwable? = null):
        Exception(message, cause)
