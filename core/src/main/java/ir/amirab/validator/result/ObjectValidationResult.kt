package ir.amirab.validator.result

import ir.amirab.validator.reason.CompositeReason
import ir.amirab.validator.reason.Reason
import kotlin.reflect.KProperty1

class ObjectValidationResult<in C>(
    override val isValid: Boolean,
    override val reason: CompositeReason<C>?
) : ValidationResult{
    operator fun get(property: KProperty1<out C, *>): Reason? {
        @Suppress("UNCHECKED_CAST")
        return reason?.let {
            it[property]
        }
    }
}