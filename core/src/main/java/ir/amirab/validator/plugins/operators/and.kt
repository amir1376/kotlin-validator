package ir.amirab.validator.plugins.operators

import ir.amirab.validator.Rule
import ir.amirab.validator.plugins.operators.AndInvalidReason
import ir.amirab.validator.reason.SingleReason

/**
 * logical and for combine rules
 * if you have only two rules for simplicity you can use [Rule.and]
 * for example
 * val newRule = and(rule1,rule2,rule3)
 */
fun <T> and(vararg rules: Rule<T, *>) = combine(
    rules = rules,
    check = { results ->
        results.all { it.isValid }
    },
    getErrors = { results ->
        val errors = results.mapNotNull { it.reason }
        join(errors, AndInvalidReason)
    }
)

/**
 * logical and for combine two rules
 * if you have more than one consider using [and]
 * for example
 * val newRule = rule1 and rule2
 */
infix fun <T> Rule<T, *>.and(other: Rule<T, *>) = and(this@and, other)

object AndInvalidReason: SingleReason