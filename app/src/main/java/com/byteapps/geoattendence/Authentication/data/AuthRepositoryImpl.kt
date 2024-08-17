package com.byteapps.serrvicewala.Authentication.data

import android.app.Activity
import com.byteapps.geoattendence.Authentication.domain.AuthRepository
import com.byteapps.geoattendence.Utils.ResultState
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val auth: FirebaseAuth) : AuthRepository {


    private lateinit var omVerificationCode: String


    override fun createUserWithPhone(phone: String, activity: Activity): Flow<ResultState<String>> = callbackFlow {

            trySend(ResultState.Loading)


            val onVerificationCallback = object : OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(p0: PhoneAuthCredential) {

                    }

                    override fun onVerificationFailed(p0: FirebaseException) {
                        trySend(ResultState.Error(p0.toString()))
                    }

                    override fun onCodeSent(verificationCode: String, p1: ForceResendingToken, ) {
                        super.onCodeSent(verificationCode, p1)

                        omVerificationCode = verificationCode

                        trySend(ResultState.Success(phone))

                    }

                }

            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber("+91$phone")
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(onVerificationCallback)
                .build()
            verifyPhoneNumber(options)

            awaitClose {
                close()
            }

        }

    override fun signWithCredential(otp: String): Flow<ResultState<User>> = callbackFlow {


        val credential = getCredential(omVerificationCode, otp)


        trySend(ResultState.Loading)

        auth.signInWithCredential(credential)

            .addOnCompleteListener {task->

                if (task.isSuccessful) {

                    val user = task.result?.user?.uid

                    trySend(ResultState.Success(User(user)))

                } else {
                    // Handle error -> task.exception
                }

            }.addOnFailureListener {

                trySend(ResultState.Error(it.toString()))

            }


        awaitClose {
            close()
        }

    }



}
