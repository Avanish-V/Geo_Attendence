package com.byteapps.geoattendence.Vailidation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byteapps.geoattendence.Vailidation.UserCase.ValidateDegignation
import com.byteapps.geoattendence.Vailidation.UserCase.ValidateDepartment
import com.byteapps.geoattendence.Vailidation.UserCase.ValidateEmail
import com.byteapps.geoattendence.Vailidation.UserCase.ValidateMobile
import com.byteapps.geoattendence.Vailidation.UserCase.ValidateOfficeId
import com.byteapps.geoattendence.Vailidation.UserCase.ValidateUserId
import com.byteapps.geoattendence.Vailidation.UserCase.ValidateUserImage
import com.byteapps.geoattendence.Vailidation.UserCase.ValidateUserName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ProfileFormValidation(
    private val validateUserName: ValidateUserName = ValidateUserName(),
    private val validateUserId: ValidateUserId = ValidateUserId(),
    private val validateUserImage: ValidateUserImage = ValidateUserImage(),
    private val validateOfficeId: ValidateOfficeId = ValidateOfficeId(),
    private val validatedepartment: ValidateDepartment = ValidateDepartment(),
    private val validatedegignation: ValidateDegignation = ValidateDegignation(),
    private val validateemail: ValidateEmail = ValidateEmail(),
    private val validatemobile: ValidateMobile = ValidateMobile()
) : ViewModel() {

    // MutableStateFlow for internal state management
    private val _state = MutableStateFlow(ProfileFormState())
    val state: StateFlow<ProfileFormState> get() = _state

    // Channel for validation events
    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents: Flow<ValidationEvent> = validationEventChannel.receiveAsFlow()

    // Handle UI events
    fun onEvent(event: ProfileFormEvent) {
        when (event) {
            is ProfileFormEvent.UserNameChanged -> {
                _state.value = _state.value.copy(userName = event.userName)
            }
            is ProfileFormEvent.UserImageChanged -> {
                _state.value = _state.value.copy(userImage = event.userImage)
            }
            is ProfileFormEvent.UserIDChanged -> {
                _state.value = _state.value.copy(useID = event.userID)
            }
            is ProfileFormEvent.OfficeIDChanged -> {
                _state.value = _state.value.copy(officeId = event.officeId)
            }
            is ProfileFormEvent.DepartmentChanged -> {
                _state.value = _state.value.copy(department = event.department)
            }
            is ProfileFormEvent.DegignationChanged -> {
                _state.value = _state.value.copy(degignation = event.degignation)
            }
            is ProfileFormEvent.EmailChanged -> {
                _state.value = _state.value.copy(email = event.email)
            }
            is ProfileFormEvent.MobileChanged -> {
                _state.value = _state.value.copy(mobile = event.mobile)
            }
            is ProfileFormEvent.Submit -> {
                saveData()
            }
        }
    }

    // Validate and save data
    private fun saveData() {
        val currentState = _state.value

        val userNameResult = validateUserName.execute(currentState.userName)
        val userImageResult = validateUserImage.validate(currentState.userImage)
        val userIDResult = validateUserId.validate(currentState.useID)
        val officeIDResult = validateOfficeId.validate(currentState.officeId)
        val departmentResult = validatedepartment.execute(currentState.department)
        val degignationResult = validatedegignation.execute(currentState.degignation)
        val mobileResult = validatemobile.validate(currentState.mobile)
        val emailResult = validateemail.validate(currentState.email)

        // Collect all validation results
        val hasError = listOf(
            userNameResult,
            userImageResult,
            userIDResult,
            officeIDResult,
            departmentResult,
            degignationResult,
            mobileResult,
            emailResult
        ).any { !it.success }

        if (hasError) {
            _state.value = currentState.copy(
                userNameError = userNameResult.errorMessage,
                userImageError = userImageResult.errorMessage,
                useIDError = userIDResult.errorMessage,
                officeIDError = officeIDResult.errorMessage,
                departmentError = departmentResult.errorMessage,
                degignationError = degignationResult.errorMessage,
                emailError = emailResult.errorMessage,
                mobileError = mobileResult.errorMessage
            )
            return
        }

        viewModelScope.launch(Dispatchers.Main) {
            validationEventChannel.send(ValidationEvent.Success)
        }
    }

    sealed class ValidationEvent {
        object Success : ValidationEvent()
    }
}
