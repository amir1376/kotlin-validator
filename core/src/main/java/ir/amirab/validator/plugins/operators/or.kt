package ir.amirab.validator.plugins.operators

import ir.amirab.validator.Rule
import ir.amirab.validator.reason.SingleReason

/**
 * logical or for combine rules
 * if you have only two rules for simplicity you can use [Rule.or]
 * for example
 * val newRule = or(rule1,rule2,rule3)
 */
fun <T> or(vararg rules: Rule<T, *>)=combine(
    rules = rules,
    check = { results ->
        results.any { result ->
            result.isValid
        }
    },
    getErrors = { results ->
        val errors = results.mapNotNull { it.reason }
        join(errors, OrInvalidReason)
    }
)

/**
 * logical or for combine two rules
 * if you have more than two rules consider using [or]
 * for example
 * val newRule = rule1 or rule2
 */
infix fun <T> Rule<T, *>.or(other: Rule<T, *>)=or(this@or, other)

object OrInvalidReason: SingleReason