package ir.amirab.validator.plugins.string

import ir.amirab.validator.because
import ir.amirab.validator.plugins.operators.and
import ir.amirab.validator.reason.SingleReason
import ir.amirab.validator.rule
import ir.amirab.validator.thenValid

val numericCharacters by lazy { "[0-9]".toRegex() }

class ContainsAtLeastNumberInvalidReason(val expected:Int, val contains:Int): SingleReason

fun containsAtLeastNumber(expected: Int)= notEmpty and rule<String> {
    val r = numericCharacters.findAll(it)
    val count = r.count()
    val valid = count >= expected
    if (valid)
        thenValid()
    else {
        because(ContainsAtLeastNumberInvalidReason(expected, count))
    }
}