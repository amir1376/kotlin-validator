package ir.amirab.validator.translate

import ir.amirab.validator.reason.CompositeReason
import ir.amirab.validator.reason.Reason
import ir.amirab.validator.reason.SingleReason
import ir.amirab.validator.reason.flattenToSingleReason
import ir.amirab.validator.translate.ValidatedTranslation.adapter

/**
 * use for simple inline reasons
 */
fun Reason.translate(): List<String> {
    return flattenToSingleReason().map {
        adapter.translate(it)
    }
}

/**
 * translate a reason and join their result with a separator
 */
fun Reason.translateRawString(separator:String=" ")=translate().joinToString(separator)

/**
 * use for composite reasons
 */
fun CompositeReason<*>.translate(): Map<String, List<String>> {
    return map.mapValues {
        it.value.translate()
    }
}

/**
 * @return translate by deep checking for possible composite rules
 */
fun CompositeReason<*>.translateDeepComposite(): Map<String, Any> {
    return map.mapValues {
        when (val r = it.value) {
            is CompositeReason<*> -> {
                r.translateDeepComposite()
            }
            else -> {
                r.translate()
            }
        }
    }
}

/**
 * this api provide translation mapping
 * if you want custom translation you can provide your own
 * by implementing this interface
 */
interface ValidatorTranslationAdapter {
    /**
     * this method gives a single reason
     * that have
     * - a key for translation
     * - some optional parameters
     */
    fun translate(reason: SingleReason): String
}