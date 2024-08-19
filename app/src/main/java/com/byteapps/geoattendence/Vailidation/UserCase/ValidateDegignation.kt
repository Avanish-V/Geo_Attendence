package com.byteapps.geoattendence.Vailidation.UserCase

import com.byteapps.geoattendence.Vailidation.ValidationResult

class ValidateDegignation {

    fun execute(degignation:String):ValidationResult{
        if (degignation.isBlank())
            return ValidationResult(
                success = false,
                errorMessage = "Degignation can't be null."
            )
        return ValidationResult(
            success = true
        )
    }

}