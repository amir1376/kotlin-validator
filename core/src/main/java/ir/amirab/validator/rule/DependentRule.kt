package ir.amirab.validator.rule

import ir.amirab.validator.Rule
import ir.amirab.validator.result.ValidationResult

/**
 * it means that input of these rules depends on another property
 */
class DependentRule<C,T, R : ValidationResult>(
    val constraintBlock: suspend DependentRule<C,T, R>.(DependentInput<C, T>) -> R
) : Rule<DependentInput<C, T>, R> {
    override suspend fun validate(input: DependentInput<C, T>): R=constraintBlock(input)
}
