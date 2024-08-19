package com.byteapps.geoattendence.Vailidation.UserCase

import android.net.Uri
import com.byteapps.geoattendence.Vailidation.ValidationResult

class ValidateUserImage {
    fun validate(imageUri: Uri?):ValidationResult{
        if (imageUri  == null)
            return ValidationResult(
                success = false,
                errorMessage = "Select an image."
            )

        return ValidationResult(
            success = true
        )
    }
}