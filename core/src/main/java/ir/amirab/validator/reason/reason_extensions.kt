package ir.amirab.validator.reason

/**
 * make reason flatten to [SingleReason]
 * this is used for translation
 */
internal fun Reason.flattenToSingleReason(): List<SingleReason> {
    return when(this){
        is SingleReason ->{
            listOf(this)
        }
        is ReasonList ->{
            reasons.map {
                it.flattenToSingleReason()
            }.flatten()
        }
//        WARNING composite reason keys is ignored this method is only for simple rules
        is CompositeReason<*> ->{
            map.values.map {
                it.flattenToSingleReason()
            }.flatten()
        }
    }
}
