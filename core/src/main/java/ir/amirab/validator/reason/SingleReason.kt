package ir.amirab.validator.reason

/**
 * this is a primitive reason
 * other reason may contain one or more of this
 */
interface SingleReason: Reason{
    /**
     * this is fallback reason if no translation provided for this rule
     * [rawReason] is provided to translation adapter
     */
    val rawReason: String get() = requireNotNull(this::class.qualifiedName){
        "Single reason subtype is local or a class of an anonymous object " +
                "you have to override SingleReason::rawReason if you want to use anonymous object"
    }
}