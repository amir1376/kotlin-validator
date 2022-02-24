package ir.amirab.validator.translate

import ir.amirab.validator.reason.SingleReason
import kotlin.reflect.KClass

interface TranslateMethod
open class DefaultValidatedTranslationBaseAdapter : ValidatorTranslationAdapter {
    protected val installedTranslations = mutableMapOf<KClass<out SingleReason>, TranslateMethod>()

    fun <T : SingleReason> extend(clazz: KClass<T>, translateMethod: TranslateMethod) {
        installedTranslations[clazz] = translateMethod
    }


    protected open fun retrieve(method: TranslateMethod, reason: SingleReason): String {
        throw NotImplementedError(method::class.qualifiedName + " is not supported by " + this::class.qualifiedName + " if you create new translate method please consider implement this class too")
    }


    override fun translate(reason: SingleReason): String {
        val foundTranslator = installedTranslations[reason::class]
        return if (foundTranslator != null) {
            retrieve(foundTranslator, reason)
        } else {
            reason.rawReason
        }
    }
}
