package ir.amirab.validator.plugins.common

import ir.amirab.validator.result.ValidationResult
import ir.amirab.validator.ensureIsValid
import ir.amirab.validator.reason.Reason
import ir.amirab.validator.reason.SingleReason
import ir.amirab.validator.rule
import ir.amirab.validator.rule.BaseRule

class EmailValidationResult(
    override val isValid: Boolean,
    override val reason: Reason?,
    user: String?,
    host: String?
) : ValidationResult {
    private val _host = host
    private val _user = user

    val user: String
        get() {
            ensureIsValid()
            return _user!!
        }

    val host: String
        get() {
            ensureIsValid()
            return _host!!
        }

}
/**
 * Email validation
 */
private val emailRegex by lazy { "([a-zA-Z0-9-._]+)@([a-zA-Z0-9-._]+\\.[a-zA-Z]+)".toRegex() }
object EmailInvalidReason:SingleReason
val email get() = rule<String, EmailValidationResult> {
        val match = emailRegex.matchEntire(it)
        val isValid = match != null
        EmailValidationResult(
            isValid = isValid,
            reason = if (isValid) null else EmailInvalidReason,
            user = match?.groupValues?.getOrNull(1),
            host = match?.groupValues?.getOrNull(2)
        )
    }
class EmailWithHosts(val list: List<String>) :SingleReason
fun BaseRule<String, EmailValidationResult>.withHosts(list: List<String>) =
    rule<String, EmailValidationResult> {
//    Be careful not use validate without label ,can stick into recursive calls
        val upstream = this@withHosts.validate(it)
        if (!upstream.isValid) {
            upstream
        } else {
            val isValid = upstream.host in list
            EmailValidationResult(
                isValid = isValid,
                reason = if (isValid) null else EmailWithHosts(list),
                user = upstream.user,
                host = upstream.host
            )
        }
    }