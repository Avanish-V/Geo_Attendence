package com.byteapps.geoattendence.Vailidation.UserCase

import com.byteapps.geoattendence.Vailidation.ValidationResult

class ValidateDepartment {

    fun execute(department:String):ValidationResult{
        if (department.isBlank())
            return ValidationResult(
                success = false,
                errorMessage = "Department can't be null."
            )
        return ValidationResult(
            success = true
        )
    }

}