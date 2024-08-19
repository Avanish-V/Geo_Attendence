package com.byteapps.geoattendence.Vailidation.UserCase

import com.byteapps.geoattendence.Vailidation.ValidationResult

class ValidateUserName {

    fun execute(userName:String):ValidationResult{
        if (userName.isBlank())
            return ValidationResult(
                success = false,
                errorMessage = "Name can't be null."
            )
        return ValidationResult(
            success = true
        )
    }

}