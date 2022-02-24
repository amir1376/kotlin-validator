package ir.amirab.validator.rule

import kotlin.reflect.KProperty1

data class DependentInput<C, out T>(
    val receiver: C,
    val prop: KProperty1<C, T>,
) {
    val actualInput get():T = prop.get(receiver)
}