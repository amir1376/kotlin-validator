package ir.amirab.validator.plugins.common

import ir.amirab.validator.because
import ir.amirab.validator.reason.SingleReason
import ir.amirab.validator.rule
import ir.amirab.validator.thenValid

class OneOfInvalidReason<T>(val enums: List<T>) : SingleReason

/**
 * check input for existence in [list]
 */
fun <T> oneOf(list: List<T>) = rule<T> {
    if (it in list) {
        thenValid()
    } else {
        because(OneOfInvalidReason(list))
    }
}

/**
 * check input for existence in [items]
 */
fun <T> oneOf(vararg items: T) = oneOf(items.toList())

/**
 * check input for existence in provided enum [E]
 * C the type of enum
 * R expected type
 * usage :
 * enum class Gender{ Male ,Female}
 * onOf<Gender,String>("Female"){it.name}
 *
 * note this is just for example
 * you can use [oneOf] that has one type parameter it uses EnumType::name transformation by default
 * @param transform transform enum to reach the expected property
 */
inline fun <reified E : Enum<E>, R> oneOf(crossinline transform: (E) -> R) = with(
    enumValues<E>().map(transform)
) {
    oneOf(this)
}


/**
 * shorthand for oneOf with name transformation
 */
inline fun <reified C : Enum<C>> oneOf() = oneOf<C, String> { it.name }
