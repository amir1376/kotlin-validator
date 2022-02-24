package ir.amirab.validator.plugins.string

import ir.amirab.validator.because
import ir.amirab.validator.reason.SingleReason
import ir.amirab.validator.rule
import ir.amirab.validator.thenValid

private val upperCaseCharacters by lazy { "[A-Z]".toRegex() }

class ContainsAtLeastUpperCaseInvalidReason(val contains:Int, val expected:Int): SingleReason

fun containsAtLeastUpperCase(i: Int) = rule<String> {
    val r = upperCaseCharacters.findAll(it)
    val count = r.count()
    val valid = count >= i
    if (valid)
        thenValid()
    else {
        because(ContainsAtLeastUpperCaseInvalidReason(count, i))
    }
}