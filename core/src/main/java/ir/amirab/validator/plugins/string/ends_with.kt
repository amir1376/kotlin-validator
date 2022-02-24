package ir.amirab.validator.plugins.string

import ir.amirab.validator.because
import ir.amirab.validator.reason.SingleReason
import ir.amirab.validator.rule
import ir.amirab.validator.thenValid

class EndsWithInvalidReason(val postfix:String): SingleReason

fun endsWith (postfix:String)= rule<String> {
    val valid = it.endsWith(postfix)
    if (valid)
        thenValid()
    else {
        because(EndsWithInvalidReason(postfix))
    }
}