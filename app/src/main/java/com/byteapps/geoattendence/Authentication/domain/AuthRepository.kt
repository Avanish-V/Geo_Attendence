package com.byteapps.geoattendence.Authentication.domain

import android.app.Activity
import com.byteapps.geoattendence.Utils.ResultState
import com.byteapps.serrvicewala.Authentication.data.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun createUserWithPhone(phone:String, activity: Activity) : Flow<ResultState<String>>

    fun signWithCredential(otp:String) : Flow<ResultState<User>>



}