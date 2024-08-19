package com.byteapps.geoattendence.Vailidation.UserCase

import com.byteapps.geoattendence.Vailidation.ValidationResult

class ValidateOfficeId {

    fun validate(officeID:String):ValidationResult{
        if (officeID.isBlank())
            return ValidationResult(
                success = false,
                errorMessage = "OfficeID can't be null."
            )
        return ValidationResult(
            success = true
        )
    }

}