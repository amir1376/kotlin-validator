package ir.amirab.validator.plugins.string

import ir.amirab.validator.*
import ir.amirab.validator.reason.SingleReason
import ir.amirab.validator.rule


private val startWithEngChar by lazy { "$[a-zA-Z]+".toRegex() }
object StartsWithEnglishCharacterInvalidReason:SingleReason
fun startsWithEnglishCharacter (prefix:String)=rule<String>{
    val valid = startWithEngChar.matchEntire(it)
    if (valid!=null)
        thenValid()
    else {
        because(StartsWithEnglishCharacterInvalidReason)
    }
}