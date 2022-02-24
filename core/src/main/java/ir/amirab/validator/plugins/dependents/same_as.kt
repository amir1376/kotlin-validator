package ir.amirab.validator.plugins.dependents

import ir.amirab.validator.dependentRule
import ir.amirab.validator.reason.SingleReason
import ir.amirab.validator.result
import ir.amirab.validator.result.ValidationResult
import ir.amirab.validator.rule.DependentRule
import kotlin.reflect.KProperty1

data class SameAsInvalidReason(val propName: String) : SingleReason

fun <C, T> sameAs(prop: KProperty1<C, T>): DependentRule<C, String, ValidationResult> {
    return dependentRule {
        val isValid = it.actualInput == prop.get(it.receiver)
        result(
            isValid,
            if (isValid) {
                null
            } else {
                SameAsInvalidReason(prop.name)
            }
        )
    }
}