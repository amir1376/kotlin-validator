package ir.amirab.validator.result

import ir.amirab.validator.reason.Reason

/**
 * every rule after validation returns a ValidationResult
 * or a subclass of it
 */
interface ValidationResult{
    val isValid: Boolean
    val reason: Reason?
}

