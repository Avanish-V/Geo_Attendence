package com.byteapps.Features.UserProfile.data

import android.net.Uri
import android.util.Log
import com.byteapps.Features.UserProfile.domain.UserProfileRepository
import com.byteapps.geoattendence.Utils.NavRoutes
import com.byteapps.geoattendence.Utils.ResultState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class UserProfileRepoImpl @Inject constructor(private val db:FirebaseFirestore,private val auth:FirebaseAuth,private val storage: FirebaseStorage) : UserProfileRepository{

    override suspend fun createUserProfile(userProfileDTO: UserProfileDTO): Flow<ResultState<Boolean>> = callbackFlow {

        trySend(ResultState.Loading)

        auth.currentUser?.let {
            db.collection("UsersProfile").document(it.uid).set(userProfileDTO)
                .addOnSuccessListener {
                    trySend(ResultState.Success(true))
                }
                .addOnFailureListener {
                    trySend(ResultState.Error(it.message.toString()))
                }
        }

        awaitClose {
            close()
        }

    }

    override suspend fun uploadUserImage(imageUri: Uri): Flow<ResultState<String>> = callbackFlow {

        trySend(ResultState.Loading)

        storage.getReference("UserProfileImages").child(auth.currentUser?.phoneNumber.toString()).putFile(
            imageUri
        )
            .addOnCompleteListener{task->
                if (task.isSuccessful){
                    task.result.storage.downloadUrl.addOnSuccessListener {downloadUrl->
                        trySend(ResultState.Success(downloadUrl.toString()))
                    }.addOnFailureListener {
                        trySend(ResultState.Error(it.message.toString()))
                    }
                }
            }
            .addOnFailureListener{
                trySend(ResultState.Error(it.message.toString()))
            }

        awaitClose {
            close()
        }


    }

    override suspend fun isUserExist(userUID: String): Flow<ResultState<NavRoutes>> = callbackFlow {

        trySend(ResultState.Loading)

        if (auth.currentUser?.uid == null) {
            trySend(ResultState.Success(NavRoutes.Authentication))
            awaitClose { close() }
            return@callbackFlow
        }

        val documentReference = db.collection("UsersProfile").document(userUID)

        documentReference.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    trySend(ResultState.Success(NavRoutes.MainScreen))
                } else {
                    trySend(ResultState.Success(NavRoutes.ProfileSetup))
                }
            }
            .addOnFailureListener { exception ->
                trySend(ResultState.Error(exception.message.toString()))
            }

        awaitClose { close() }
    }


    override fun fetchUserProfile(): Flow<ResultState<UserProfileDTO>> = callbackFlow {

        trySend(ResultState.Loading)

        auth.currentUser?.let { user ->
            db.collection("UsersProfile").document(user.uid)
                .get()
                .addOnSuccessListener {

                    val data = it.toObject(UserProfileDTO::class.java)
                    Log.d("FETCH_USER",data.toString())
                    if (data != null){
                        trySend(ResultState.Success(data))
                    }

                }.addOnFailureListener {
                    trySend(ResultState.Error(it.message.toString()))
                }
        }

        awaitClose {
            close()
        }
    }
}