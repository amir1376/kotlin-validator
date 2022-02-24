package ir.amirab.validator.plugins.string

import ir.amirab.validator.because
import ir.amirab.validator.reason.SingleReason
import ir.amirab.validator.rule
import ir.amirab.validator.thenValid

class AbsentInvalidReason(val absent:String): SingleReason

fun absent (present:String)= rule<String> {
    val valid = !it.contains(present)
    if (valid)
        thenValid()
    else {
        because(AbsentInvalidReason(present))
    }
}