package com.byteapps.geoattendence.Vailidation.UserCase

import com.byteapps.geoattendence.Vailidation.ValidationResult

class ValidateUserId {

    fun validate(userId:String):ValidationResult{
        if (userId.isBlank())
            return ValidationResult(
                success = false,
                errorMessage = "UserId can't be null"
            )

        return ValidationResult(
            success = true
        )
    }
}