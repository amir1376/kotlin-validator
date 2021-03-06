package ir.amirab.validator.plugins.string

import ir.amirab.validator.because
import ir.amirab.validator.reason.SingleReason
import ir.amirab.validator.rule
import ir.amirab.validator.thenValid

class PresentInvalidReason(val present:String): SingleReason

fun present (value:String)= rule<String> {
    val valid = it.contains(value)
    if (valid)
        thenValid()
    else {
        because(PresentInvalidReason(value))
    }
}