package ir.amirab.validator.plugins.common

import ir.amirab.validator.because
import ir.amirab.validator.reason.SingleReason
import ir.amirab.validator.rule
import ir.amirab.validator.thenValid

data class EqualsInvalidReason<T>(val mustBeEqualTo:T):SingleReason

fun <T>equals(expected:T)= rule<T> {
    if (expected==it)
        thenValid()
    else
        because(EqualsInvalidReason(expected))
}