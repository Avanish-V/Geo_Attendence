package com.byteapps.geoattendence.Vailidation.UserCase

import com.byteapps.geoattendence.Vailidation.ValidationResult

class ValidateEmail {

    fun validate(email:String):ValidationResult{
        if (email.isBlank())
            return ValidationResult(
                success = false,
                errorMessage = "Email can't be null"
            )

        return ValidationResult(
            success = true
        )
    }
}