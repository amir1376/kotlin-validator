package ir.amirab.validator.translate

import ir.amirab.validator.reason.SingleReason
import kotlin.reflect.KClass

data class FromRawString(val string: String) : TranslateMethod
fun interface FromCallable<T : SingleReason> : TranslateMethod,(T)->String

open class DefaultValidatedTranslationAdapter : DefaultValidatedTranslationBaseAdapter() {

    inline infix fun <reified T : SingleReason> KClass<T>.providedBy(noinline block: (T)-> String) =
        apply {
            extend(T::class, FromCallable(block))
        }
    inline infix fun <reified T : SingleReason> KClass<T>.providedBy(string: String) =
        apply {
            extend(T::class, FromRawString(string))
        }

    override fun retrieve(method: TranslateMethod, reason: SingleReason): String {
        return when (method) {
            is FromRawString -> return method.string
            is FromCallable<*> -> {
//                this is SingleReason
                @Suppress("UNCHECKED_CAST")
                (method as FromCallable<SingleReason>).invoke(reason)
            }
            else -> {
                super.retrieve(method, reason)
            }
        }
    }
}
fun ValidatedTranslation.initWithDefault()= init(DefaultValidatedTranslationAdapter())
