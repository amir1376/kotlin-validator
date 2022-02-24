package ir.amirab.validator.rule

import ir.amirab.validator.Rule
import ir.amirab.validator.result.ValidationResult
import ir.amirab.validator.reason.CompositeReason
import ir.amirab.validator.result.ObjectValidationResult
import kotlin.reflect.KProperty1


/**
 * it contains two types of rules [DependentRule] , [BaseRule]
 * on validation it hydrates [DependentRule] types with [C] object
 * then create an [ObjectValidationResult]
 */
class CompositeRule<C> : Rule<C, ObjectValidationResult<C>> {
    private val rules: MutableList<Pair<KProperty1<C, Any?>, Rule<Any?, ValidationResult>>> =
        mutableListOf()

    inner class Scope {
        @Suppress("UNCHECKED_CAST")
        private fun <T : Any?,R: ValidationResult> add(ruleWithKey: Pair<KProperty1<C, T>, Rule<T, R>>) {
            rules.add(ruleWithKey as Pair<KProperty1<C, Any?>, Rule<Any?, ValidationResult>>)
        }

        @JvmName("unaryPlusCTCTValidationResult")
        @Suppress("UNCHECKED_CAST")
        private fun <T : Any?,R: ValidationResult> add(ruleWithKey: Pair<KProperty1<C, T>, DependentRule<C, T, R>>) {
            rules.add(ruleWithKey as Pair<KProperty1<C, Any?>, Rule<Any?, ValidationResult>>)
        }


        infix fun <T,R: ValidationResult> KProperty1<C, T>.mustBe(rule: Rule<T, R>) {
            add(this to rule)
        }
        infix fun <T,R: ValidationResult> KProperty1<C, T>.mustBe(rule: DependentRule<C, T, R>) {
            add(this to rule)
        }
    }


    override suspend fun validate(input: C): ObjectValidationResult<C> {
        val results = rules.associate { (prop, rule) ->
//            this is object that have multiple rules applied to each of its properties
            val receiver = input
            when (rule) {
//                dependent rules go here
//                we pass whole object model to this rule as input parameter
//                when we pass DependentInput to DependentRule it can extract
//                other properties by passed object receiver
                is DependentRule<*, *, *> -> {
                    prop.name to rule.validate(
                        DependentInput(
                            receiver = receiver,
                            prop = prop,
                        )
                    )
                }
//                base rules go through here
//                they are just simple rules that depend on nothing
//                only give input to validator
                else -> {
                    prop.name to rule.validate(prop.get(receiver))
                }
            }
        }
        val isValid = results.all { it.value.isValid }
        val reason = if (!isValid) {
            CompositeReason<C>(
                results
                    .filter { (_, result) -> result.reason != null }
                    .mapValues { (_, result) -> result.reason!! }
            )
        } else null
        return ObjectValidationResult(isValid, reason)
    }
}
