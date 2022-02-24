package ir.amirab.validator

import ir.amirab.validator.reason.Reason
import ir.amirab.validator.reason.SingleReason
import ir.amirab.validator.result.ValidationResult
import ir.amirab.validator.rule.BaseRule
import ir.amirab.validator.rule.CompositeRule
import ir.amirab.validator.rule.DependentInput
import ir.amirab.validator.rule.DependentRule

/**
 * create [BaseRule] with simple [ValidationResult]
 */
@JvmName("baseRuleWithValidationResult")
fun <T> rule(constrainBlock: suspend BaseRule<T, ValidationResult>.(input: T) -> ValidationResult): BaseRule<T, ValidationResult> {
    return BaseRule(constrainBlock)
}

/**
 * create [BaseRule] with custom (subclass of) [ValidationResult]
 */
@JvmName("baseRuleWithSubclassOfValidationResult")
fun <T, R : ValidationResult> rule(constrainBlock: suspend BaseRule<T, R>.(input: T) -> R): BaseRule<T, R> {
    return BaseRule(constrainBlock)
}
/**
 * create [BaseRule] with custom (subclass of) [ValidationResult]
 */
@JvmName("baseRuleWithSubclassOfValidationResult")
fun <C,T, R : ValidationResult> dependentRule(constrainBlock: suspend DependentRule<C,T, R>.(input: DependentInput<C,T>) -> R): DependentRule<C,T, R> {
    return DependentRule(constrainBlock)
}


/**
 *  create [CompositeRule] this is a combination of
 *  multiple rules to apply on an object properties
 *  they can depend on together with [rule]
 */
fun <C> compositeRule(validateFn: CompositeRule<C>.Scope.() -> Unit): CompositeRule<C> {
    return CompositeRule<C>().apply {
        Scope().apply(validateFn)
    }
}

private class ValidationResultImpl(
    override val isValid: Boolean,
    override val reason: Reason?,
) : ValidationResult


fun Rule<*, ValidationResult>.result(isValid: Boolean, reason: Reason?): ValidationResult {
    return ValidationResultImpl(isValid = isValid, reason = reason)
}
fun Rule<*, ValidationResult>.thenValid(): ValidationResult {
    return result(isValid=true,reason=null)
}

fun Rule<*, ValidationResult>.because(reason: Reason): ValidationResult {
    return result(isValid = false, reason = reason)
}

fun Rule<*, ValidationResult>.because(rawReason: String) =
    because(
        reason = object : SingleReason {
            override val rawReason get() = rawReason
        }
    )

inline fun Rule<*, ValidationResult>.because(crossinline reasonBuilder: () -> String) =
    because(reason = object : SingleReason {
        override val rawReason get() = reasonBuilder()
    })