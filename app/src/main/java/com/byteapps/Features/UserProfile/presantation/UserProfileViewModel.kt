package com.byteapps.Features.UserProfile.presantation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byteapps.Features.UserProfile.data.UserProfileDTO
import com.byteapps.Features.UserProfile.domain.UserProfileRepository
import com.byteapps.geoattendence.Utils.NavRoutes
import com.byteapps.geoattendence.Utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(private val userProfileRepository: UserProfileRepository): ViewModel() {

    suspend fun createUserProfile(userProfileDTO: UserProfileDTO) = userProfileRepository.createUserProfile(userProfileDTO = userProfileDTO)

    suspend fun uploadUserImage(imageUri: Uri) = userProfileRepository.uploadUserImage(imageUri = imageUri)

    private val _userProfileResultState : MutableStateFlow<UserProfileResultState> = MutableStateFlow(UserProfileResultState())
    val userProfileResultState : StateFlow<UserProfileResultState>get() = _userProfileResultState

    private val _validateUser : MutableStateFlow<ValidateUserResultState> = MutableStateFlow(ValidateUserResultState())
    val validateUser : StateFlow<ValidateUserResultState>get() = _validateUser

    fun fetchUserProfile(){
        viewModelScope.launch {
            userProfileRepository.fetchUserProfile().collect{
                when(it){
                    is ResultState.Loading->{
                        _userProfileResultState.value = UserProfileResultState(isLoading = true)
                    }
                    is ResultState.Error->{
                        _userProfileResultState.value = UserProfileResultState(error = it.message)
                    }
                    is ResultState.Success->{
                        _userProfileResultState.value = UserProfileResultState(userProfileDTO = it.data)
                    }
                }
            }
        }
    }


    fun validateUser(userUID:String,) {

        viewModelScope.launch {
            userProfileRepository.isUserExist(userUID).collect{
                when(it){
                    is ResultState.Loading->{
                        _validateUser.value = ValidateUserResultState(isLoading = true)
                    }
                    is ResultState.Error->{
                        _validateUser.value = ValidateUserResultState(error = it.message)
                    }
                    is ResultState.Success->{
                        _validateUser.value = ValidateUserResultState(navRoute = it.data)

                    }
                }
            }
        }


    }


}
data class UserProfileResultState(
    val isLoading:Boolean = false,
    val userProfileDTO: UserProfileDTO? = null,
    val error:String = ""
)

data class UploadImageResultState(
    val isLoading:Boolean = false,
    val imageUrl: String? = null,
    val error:String = ""
)

data class ValidateUserResultState(
    val isLoading:Boolean = false,
    val navRoute: NavRoutes? = null,
    val error:String = ""
)