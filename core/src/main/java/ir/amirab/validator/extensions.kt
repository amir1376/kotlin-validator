package ir.amirab.validator

import ir.amirab.validator.result.ValidationResult

suspend fun <T> Rule<T, *>.validateOrNull(input: T): T? {
    return when {
        validate(input).isValid -> input
        else -> null
    }
}


fun ValidationResult.ensureIsValid() {
    if (!isValid) {
        throw IllegalStateException("${this::class.simpleName} is not valid!")
    }
}

