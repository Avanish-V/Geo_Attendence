package com.byteapps.serrvicewala.DI



import com.byteapps.Features.UserProfile.data.UserProfileRepoImpl
import com.byteapps.Features.UserProfile.domain.UserProfileRepository
import com.byteapps.geoattendence.Authentication.domain.AuthRepository
import com.byteapps.serrvicewala.Authentication.data.AuthRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun providesAuthRepository(repo: AuthRepositoryImpl): AuthRepository

    @Binds
    abstract fun providesUserProfileRepository(userProfileRepoImpl: UserProfileRepoImpl):UserProfileRepository

}