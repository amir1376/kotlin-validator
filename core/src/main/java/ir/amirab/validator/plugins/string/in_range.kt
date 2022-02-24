package ir.amirab.validator.plugins.string

import ir.amirab.validator.because
import ir.amirab.validator.reason.SingleReason
import ir.amirab.validator.rule
import ir.amirab.validator.thenValid

fun inRange(intRange: IntRange) = rule<String> {
    val length = it.length
    if (length in intRange) {
        thenValid()
    } else {
        because(InRangeInvalidReason(intRange))
    }
}

//plugins
class InRangeInvalidReason(val range:IntRange): SingleReason