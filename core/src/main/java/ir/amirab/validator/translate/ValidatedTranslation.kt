package ir.amirab.validator.translate

/**
 * before translating any [Reason] you have to initialize [adapter]
 */
object ValidatedTranslation {
    lateinit var adapter: ValidatorTranslationAdapter
    private set
    /**
     * after creating or using an existing adapter
     * you have to provide it (before any translation request) to initialize library
     */
    fun <T:ValidatorTranslationAdapter>init(adapter: T):T{
        synchronized(this){
            this.adapter=adapter
        }
        return adapter
    }
}