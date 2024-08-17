package com.byteapps.serrvicewala.Authentication.presantation.Screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.byteapps.Features.UserProfile.presantation.UserProfileViewModel
import com.byteapps.Features.UserProfile.presantation.ValidateUserResultState
import com.byteapps.geoattendence.UIComponents.KeyboardController
import com.byteapps.geoattendence.UIComponents.LoadingScreen
import com.byteapps.geoattendence.UIComponents.PrimaryButton
import com.byteapps.geoattendence.UIComponents.StatusScreen
import com.byteapps.geoattendence.Utils.NavRoutes
import com.byteapps.geoattendence.Utils.ResultState

import com.byteappstudio.b2ccart.Authentications.AuthViewModel
import com.byteappstudio.blooddonate.Authentications.OtpView
import kotlinx.coroutines.launch


@Composable
fun OTPScreen(
    navHostController: NavHostController,
    authViewModel: AuthViewModel,
    userProfileViewModel: UserProfileViewModel,
) {
    val resultState = userProfileViewModel.validateUser.collectAsState()
    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(textDecoration = TextDecoration.None)) {
            append("Don’t receive OTP code?")
        }
        withStyle(
            style = SpanStyle(
                textDecoration = TextDecoration.None,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        ) {
            append(" Resend")
        }
    }

    val scope = rememberCoroutineScope()
    var isLoading by rememberSaveable { mutableStateOf(false) }
    val authResultState = authViewModel.authStatus.collectAsState()
    val context = LocalContext.current

    authResultState.value.let {
        when {
            it.isLoading -> isLoading = true
            it.error.isNotEmpty() -> StatusScreen(text = it.error)
        }
    }

    var otp by rememberSaveable { mutableStateOf("") }


    LaunchedEffect(resultState.value.isExist) {
        resultState.value.isExist?.let { isExist ->
            if (isExist) {
                navHostController.navigate(NavRoutes.MainScreen.Parent.route)
            } else {
                navHostController.navigate(NavRoutes.ProfileSetup.route)
            }
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(top = 60.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(26.dp)
        ) {
            Text(
                text = "Verify your\nphone number",
                style = MaterialTheme.typography.displayLarge,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                lineHeight = 30.sp,
            )

            Text(
                text = "Enter OTP code sent to +91${authViewModel.phoneNumber.value}",
                style = MaterialTheme.typography.displaySmall
            )

            OtpView(
                onOtpTextChange = { otp = it },
                otpText = otp,
                charColor = MaterialTheme.colorScheme.primary
            )

            Text(
                text = annotatedString,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.clickable {
                    //navHostController.navigate(NestedScreens.Login.PhoneNumber.route)
                }
            )
        }

        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PrimaryButton(buttonText = "Verify") {
                scope.launch {
                    authViewModel.signInWithCredential(code = otp).collect { result ->
                        when (result) {
                            is ResultState.Loading -> isLoading = true
                            is ResultState.Success -> {

                               userProfileViewModel.validateUser(result.data.userUUID.toString())

                            }
                            is ResultState.Error -> {
                                isLoading = false
                                Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
                            }
                            else -> {}
                        }
                    }
                }
            }
        }
    }

    if (isLoading) LoadingScreen()
    KeyboardController(isHide = isLoading)
}
