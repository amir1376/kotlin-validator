package ir.amirab.validator

import ir.amirab.validator.result.ValidationResult

interface Rule<in INPUT,out RESULT: ValidationResult>{
    suspend fun validate(input:INPUT):RESULT
}