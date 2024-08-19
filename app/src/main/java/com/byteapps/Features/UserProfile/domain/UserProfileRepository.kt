package com.byteapps.Features.UserProfile.domain

import android.net.Uri
import com.byteapps.Features.UserProfile.data.UserProfileDTO
import com.byteapps.geoattendence.Utils.NavRoutes
import com.byteapps.geoattendence.Utils.ResultState
import kotlinx.coroutines.flow.Flow

interface UserProfileRepository {

   suspend fun createUserProfile(userProfileDTO: UserProfileDTO):Flow<ResultState<Boolean>>

   suspend fun uploadUserImage(imageUri: Uri):Flow<ResultState<String>>

   suspend fun isUserExist(userUID:String):Flow<ResultState<NavRoutes>>

   fun fetchUserProfile():Flow<ResultState<UserProfileDTO>>
}