package ir.amirab.validator.plugins.string

import ir.amirab.validator.because
import ir.amirab.validator.reason.SingleReason
import ir.amirab.validator.rule
import ir.amirab.validator.thenValid

object EmptyInvalidReason : SingleReason
val empty = rule<String> {
    if (it.isEmpty()) thenValid()
    else because(EmptyInvalidReason)
}