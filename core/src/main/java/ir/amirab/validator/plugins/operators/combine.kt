package ir.amirab.validator.plugins.operators

import ir.amirab.validator.Rule
import ir.amirab.validator.result.ValidationResult
import ir.amirab.validator.because
import ir.amirab.validator.reason.Reason
import ir.amirab.validator.reason.ReasonList
import ir.amirab.validator.rule
import ir.amirab.validator.thenValid

/**
 *
 */
fun <T> combine(
    vararg rules: Rule<T, *>,
    check: (results: List<ValidationResult>) -> Boolean,
    getErrors: Rule<T, *>.(results: List<ValidationResult>) -> Reason,
) = rule<T> {
    val results = ArrayList<ValidationResult>(rules.size)
    for ((index, rule) in rules.withIndex()) {
//        this is in order
//        if we plan to change structure we MUST to change list to static array
        results.add(rule.validate(it))
    }
    val result = check(results)
    if (result) {
        thenValid()
    } else {
        because(getErrors(results))
    }
}

/**
 * join reason with a separator reason
 * this is used for combining operands with operator itself
 * implementation may change
 */
fun join(errors: List<Reason>, separator: Reason): ReasonList {
    return ReasonList(
        errors.flatMapIndexed { index: Int, reason: Reason ->
            if (index == 0) {
                listOf(reason)
            } else {
                listOf(separator, reason)
            }
        }
    )
}