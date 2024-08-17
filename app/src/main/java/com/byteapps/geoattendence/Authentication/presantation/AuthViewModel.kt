package com.byteappstudio.b2ccart.Authentications

import android.app.Activity
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byteapps.geoattendence.Authentication.domain.AuthRepository
import com.byteapps.serrvicewala.Authentication.data.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class AuthViewModel @Inject constructor(private val repo : AuthRepository): ViewModel() {


    var phoneNumber : MutableState<String> = mutableStateOf("")
    val number : State<String> = phoneNumber

    private val _authStatus : MutableStateFlow<AuthStatusResultState> = MutableStateFlow(AuthStatusResultState())
    val authStatus : MutableStateFlow<AuthStatusResultState> = _authStatus


    fun createUserWithPhone(phone:String, activity: Activity) = repo.createUserWithPhone(phone,activity)

    fun signInWithCredential(code:String) = repo.signWithCredential(code)

    fun phoneNumber(phone:String){
        phoneNumber.value = phone
    }


}

data class AuthStatusResultState(
    val isLoading : Boolean = false,
    val authStatus : User? = null,
    val error : String = ""
)