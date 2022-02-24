package ir.amirab.validator.plugins.string

import ir.amirab.validator.because
import ir.amirab.validator.reason.SingleReason
import ir.amirab.validator.rule
import ir.amirab.validator.thenValid

class StartsWithInvalidReason(val prefix:String): SingleReason

fun startsWith (prefix:String)= rule<String> {
    val valid = it.startsWith(prefix)
    if (valid)
        thenValid()
    else {
        because(AbsentInvalidReason(prefix))
    }
}