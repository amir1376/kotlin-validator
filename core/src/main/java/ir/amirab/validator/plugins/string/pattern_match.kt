package ir.amirab.validator.plugins.string

import ir.amirab.validator.because
import ir.amirab.validator.reason.SingleReason
import ir.amirab.validator.rule
import ir.amirab.validator.thenValid

data class PatternMatchInvalidReason(val pattern: String) : SingleReason
fun patternMatch(pattern: String) = rule<String> {
    val value = it
    val valid = pattern.toRegex().matches(value)
    if (valid) {
        thenValid()
    } else {
        because(PatternMatchInvalidReason(pattern))
    }
}