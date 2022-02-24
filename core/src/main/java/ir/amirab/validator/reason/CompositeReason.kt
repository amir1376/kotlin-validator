package ir.amirab.validator.reason

import kotlin.reflect.KProperty1


/**
 * this is for composite validation
 * result of an object that have nested rule on its properties
 * map key is name of that property
 * map value is reason for why that property is invalid
 */
data class CompositeReason<in C>(
    val map: Map<String, Reason>
) : Reason, Map<String, Reason> by map{
    operator fun get(property:KProperty1<out C,*>):Reason?{
        return get(property.name)
    }
}