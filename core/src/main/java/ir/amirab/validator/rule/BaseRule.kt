package ir.amirab.validator.rule

import ir.amirab.validator.Rule
import ir.amirab.validator.result.ValidationResult


/**
 * BaseRule class
 * please consider use [rule] function overloads to add your own rules
 * this is for future compatibility
 * it has simple responsibility gets input [T] returns a [ValidationResult]
 * @param constrainBlock checks input and validates it
 * then returns a [ValidationResult] to corresponding to
 * passed input parameter
 */
open class BaseRule<T,R: ValidationResult> constructor(
    protected val constrainBlock:suspend BaseRule< T,R>.(input: T) -> R
) : Rule<T, R> {
    override suspend fun validate(input: T): R = constrainBlock(input)
}

