package com.byteapps.geoattendence.Vailidation.UserCase

import com.byteapps.geoattendence.Vailidation.ValidationResult

class ValidateMobile {

    fun validate(mobile:String):ValidationResult{

        if (mobile.isBlank())
            return ValidationResult(
                success = false,
                errorMessage = "Mobile can't be null"
            )

        return ValidationResult(
            success = true
        )
    }
}