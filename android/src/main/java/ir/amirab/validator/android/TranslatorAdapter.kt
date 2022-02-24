package ir.amirab.validator.android

import android.content.Context
import ir.amirab.validator.R
import ir.amirab.validator.plugins.operators.AndInvalidReason
import ir.amirab.validator.plugins.string.InRangeInvalidReason
import ir.amirab.validator.plugins.operators.OrInvalidReason
import ir.amirab.validator.plugins.common.EmailInvalidReason
import ir.amirab.validator.plugins.common.EmailWithHosts
import ir.amirab.validator.plugins.common.EqualsInvalidReason
import ir.amirab.validator.plugins.common.OneOfInvalidReason
import ir.amirab.validator.plugins.dependents.SameAsInvalidReason
import ir.amirab.validator.plugins.string.*
import ir.amirab.validator.reason.SingleReason
import ir.amirab.validator.translate.DefaultValidatedTranslationAdapter
import ir.amirab.validator.translate.TranslateMethod
import ir.amirab.validator.translate.ValidatedTranslation
import kotlin.reflect.KClass

/**
 * android specific translate methods that use string resource
 * they are provided in android translation adapter
 * @see [AndroidTranslationAdapter.providedByResource]
 */
class FromStringResource(val stringRes: Int, vararg val args: Any?) : TranslateMethod
fun interface CallableReturnsStringResource<T : SingleReason>
    : TranslateMethod, (T) -> FromStringResource

/**
 * this is a default translation for android system backed by string resources
 * developers can call this to apply default translation for android
 * or / then override anything they wish
 * by one of [AndroidTranslationAdapter.providedBy]
 * or [AndroidTranslationAdapter.providedByResource] methods
 */
fun AndroidTranslationAdapter.applyDefaultTranslations() = apply {
    registerStrings()
    registerCommons()
    registerOperators()
    registerDependents()
}

fun AndroidTranslationAdapter.registerStrings() {
    NotEmptyInvalidReason::class providedByResource (R.string.validator_must_not_empty)
    EmptyInvalidReason::class providedByResource (R.string.validator_must_empty)
    PatternMatchInvalidReason::class providedByResource {
        FromStringResource(R.string.validator_must_pattern_patch,pattern)
    }
    InRangeInvalidReason::class providedByResource {
        FromStringResource(R.string.validator_must_in_range, range.first, range.last)
    }
    ContainsAtLeastLowerCaseInvalidReason::class providedByResource {
        FromStringResource(R.string.validator_must_contains_at_least_lowercase, expected)
    }
    ContainsAtLeastUpperCaseInvalidReason::class providedByResource {
        FromStringResource(R.string.validator_must_contains_at_least_upper_case, expected)
    }
    ContainsAtLeastNumberInvalidReason::class providedByResource {
        FromStringResource(R.string.validator_must_contains_at_least_number, expected)
    }
    PresentInvalidReason::class providedByResource {
        FromStringResource(R.string.validator_must_present, present)
    }
    AbsentInvalidReason::class providedByResource {
        FromStringResource(R.string.validator_must_absent, absent)
    }
    StartsWithInvalidReason::class providedByResource {
        FromStringResource(R.string.validator_must_starts_with, prefix)
    }
    StartsWithEnglishCharacterInvalidReason::class providedByResource R.string.validator_must_starts_with_eng_char
    EndsWithInvalidReason::class providedByResource {
        FromStringResource(R.string.validator_must_starts_with, postfix)
    }
}

fun AndroidTranslationAdapter.registerCommons() {
    EqualsInvalidReason::class providedByResource {
        FromStringResource(
            R.string.validator_must_be_equal_to, mustBeEqualTo.toString()
        )
    }
    OneOfInvalidReason::class providedByResource {
        FromStringResource(
            R.string.validator_must_be_one_of,
            enums.joinToString(",").run { "($this)" })
    }
    EmailInvalidReason::class providedByResource (R.string.validator_must_email)
    EmailWithHosts::class providedByResource {
        FromStringResource(
            R.string.validator_must_email_host,
            list.joinToString(",").run { "($this)" })
    }
}

fun AndroidTranslationAdapter.registerDependents() {
    SameAsInvalidReason::class providedByResource {
        FromStringResource(R.string.validator_must_same_as, propName)
    }
}

fun AndroidTranslationAdapter.registerOperators() {
    OrInvalidReason::class providedByResource R.string.validator_or
    AndInvalidReason::class providedByResource R.string.validator_and
}


class AndroidTranslationAdapter(
    private val context: Context
) : DefaultValidatedTranslationAdapter() {

    override fun retrieve(method: TranslateMethod, reason: SingleReason): String {
        return when (method) {
            is FromStringResource -> {
                context.getString(method.stringRes, method.args)
            }
            is CallableReturnsStringResource<*> -> {
                val resource =
                    (method as CallableReturnsStringResource<SingleReason>).invoke(reason)
                context.getString(resource.stringRes, *resource.args)
            }
            else -> super.retrieve(method, reason)
        }
    }

    inline infix fun <reified T : SingleReason> KClass<T>.providedByResource(stringRes: Int) =
        apply {
            extend(T::class, FromStringResource(stringRes))
        }

    @JvmName("providedByResourceBasedOnCallable")
    inline infix fun <reified T : SingleReason> KClass<T>.providedByResource(noinline resourceBasedOnCallable: T.() -> FromStringResource) =
        apply {
            extend(T::class, CallableReturnsStringResource(resourceBasedOnCallable))
        }

}

fun ValidatedTranslation.initDefaultAndroidAdapter(context:Context)=
    init(AndroidTranslationAdapter(context))
fun ValidatedTranslation.android(): AndroidTranslationAdapter {
    when (val adapter = adapter) {
        is AndroidTranslationAdapter -> {
            return adapter
        }
        else -> {
            throw Exception("adapter was set but not android adapter it is ${adapter::class.qualifiedName}")
        }
    }
}
