package ir.amirab.validator.plugins.string

import ir.amirab.validator.because
import ir.amirab.validator.reason.SingleReason
import ir.amirab.validator.rule
import ir.amirab.validator.thenValid

private val lowerCaseCharacters by lazy { "[a-z]".toRegex() }

class ContainsAtLeastLowerCaseInvalidReason(val contains:Int, val expected:Int): SingleReason

fun containsAtLeastLowerCase(expected: Int) = rule<String> {
    val r = lowerCaseCharacters.findAll(it)
    val count = r.count()
    val valid = count >= expected
    if (valid)
        thenValid()
    else {
        because(ContainsAtLeastLowerCaseInvalidReason(count, expected))
    }
}