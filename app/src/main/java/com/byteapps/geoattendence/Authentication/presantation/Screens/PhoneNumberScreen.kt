package com.byteapps.serrvicewala.Authentication.presantation.Screens

import android.app.Activity
import android.inputmethodservice.Keyboard
import android.view.WindowInsets
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.byteapps.geoattendence.R
import com.byteapps.geoattendence.UIComponents.LoadingScreen
import com.byteapps.geoattendence.UIComponents.PrimaryButton
import com.byteapps.geoattendence.Utils.NavRoutes
import com.byteapps.geoattendence.Utils.ResultState
import com.byteapps.geoattendence.ui.theme.Blue
import com.byteapps.geoattendence.ui.theme.DarkWhite
import com.byteapps.geoattendence.ui.theme.Dark_Black
import com.byteappstudio.b2ccart.Authentications.AuthViewModel
import kotlinx.coroutines.launch



@Composable
fun PhoneNumberScreen(
    navHostController: NavHostController,
    authViewModel: AuthViewModel
) {
    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    var phoneNumber by rememberSaveable {
        mutableStateOf("")
    }

    var isLoading by rememberSaveable {
        mutableStateOf(false)
    }
    var isInVailid by rememberSaveable {
        mutableStateOf(false)
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        Modifier
            .imePadding()
            .padding(24.dp)
            .padding(top = 30.dp)
    ) {

        Column(
            Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Image(
                modifier = Modifier.size(200.dp),
                painter = painterResource(id = R.drawable.undraw_access_account_re_8spm),
                contentDescription = null
            )

            Text(
                text = "Enter your phone number. We'll send you a \nverification code.",
                style = MaterialTheme.typography.displaySmall,
                textAlign = TextAlign.Center,
                color = Dark_Black
            )

            Column (verticalArrangement = Arrangement.spacedBy(16.dp)){

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = phoneNumber,
                    onValueChange = {phoneNumber = it},
                    prefix = {
                        Text(text = "+91")
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                PrimaryButton(buttonText = "Verify") {
                    if (phoneNumber.length < 10 || phoneNumber.length > 10){

                        isInVailid = true

                    }else{
                        scope.launch {

                            authViewModel.createUserWithPhone(phoneNumber, context as Activity)
                                .collect{
                                    when(it){
                                        is ResultState.Loading->{
                                            keyboardController?.hide()
                                            isLoading = true
                                        }
                                        is ResultState.Success->{

                                            authViewModel.phoneNumber(phoneNumber)

                                            navHostController.navigate(
                                                NavRoutes.Authentication.OTP.route
                                            )

                                        }
                                        is ResultState.Error->{
                                            isLoading = false
                                            Toast.makeText(
                                                context,
                                                it.message,
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }

                                        else -> {}
                                    }

                                }
                        }
                    }
                }

            }
        }

        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {

            val annotatedString = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(textDecoration = TextDecoration.None, color = Dark_Black)
                ){
                    append("By providing my phone number, I hereby agree\n and accept the\n ")
                }
                withStyle(
                    style = SpanStyle(textDecoration = TextDecoration.None, color = Blue)
                ){
                    append(" Terms of service")
                }
                withStyle(
                    style = SpanStyle(textDecoration = TextDecoration.None, color = Dark_Black)
                ){
                    append(" &")
                }
                withStyle(
                    style = SpanStyle(textDecoration = TextDecoration.None, color = Blue)
                ){
                    append(" Privacy Policy")
                }
            }

            Text(text = annotatedString, style = MaterialTheme.typography.labelMedium, textAlign = TextAlign.Center)
        }

    }

    if (isLoading) LoadingScreen()


}