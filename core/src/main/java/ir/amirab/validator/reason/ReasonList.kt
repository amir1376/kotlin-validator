package ir.amirab.validator.reason

/**
 * this is reason produced by logical operators
 * represents a list of [Reason]
 */
data class ReasonList(
    val reasons:List<Reason>
) : Reason