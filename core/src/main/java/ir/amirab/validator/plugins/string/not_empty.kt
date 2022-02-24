package ir.amirab.validator.plugins.string

import ir.amirab.validator.because
import ir.amirab.validator.reason.SingleReason
import ir.amirab.validator.rule
import ir.amirab.validator.thenValid

object NotEmptyInvalidReason : SingleReason
val notEmpty = rule<String> {
    if (it.isNotEmpty()) thenValid()
    else {
        because(NotEmptyInvalidReason)
    }
}