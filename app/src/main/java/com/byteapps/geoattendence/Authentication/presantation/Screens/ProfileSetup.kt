package com.byteapps.geoattendence.Authentication.presantation.Screens

import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.byteapps.Features.UserProfile.data.UserProfileDTO
import com.byteapps.Features.UserProfile.presantation.UserProfileViewModel
import com.byteapps.geoattendence.ui.theme.Blue
import com.byteapps.geoattendence.R
import com.byteapps.geoattendence.UIComponents.LoadingScreen
import com.byteapps.geoattendence.Utils.NavRoutes
import com.byteapps.geoattendence.Utils.ResultState
import com.byteapps.geoattendence.ui.theme.DarkWhite
import com.byteapps.geoattendence.ui.theme.Dull_White
import com.byteapps.geoattendence.ui.theme.Light_Black
import com.byteapps.geoattendence.ui.theme.Light_Yellow
import com.byteapps.geoattendence.ui.theme.Yellow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSetup(
    navHostController: NavHostController,
    userProfileViewModel: UserProfileViewModel
) {

    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }
    var isWarning by remember {
        mutableStateOf(false)
    }
    var isLoading by remember {
        mutableStateOf(false)
    }

    var userName by remember { mutableStateOf("") }
    var userId by remember { mutableStateOf("") }
    var officeId by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("") }
    var degignation by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }


    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text(text = "Create Profile") },
                actions = {
                    Button(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .shadow(10.dp, spotColor = Blue, ambientColor = Blue),
                        onClick = {

                            scope.launch {
                               imageUri?.let {
                                   userProfileViewModel.uploadUserImage(imageUri = it).collect{
                                       when(it){
                                           is ResultState.Loading->{
                                               isLoading = true
                                           }
                                           is ResultState.Error->{
                                               isLoading = false
                                               Toast.makeText(context,it.message,Toast.LENGTH_LONG).show()
                                               Log.d("ERROR_IS",it.message)
                                           }
                                           is ResultState.Success->{

                                               userProfileViewModel.createUserProfile(
                                                   userProfileDTO = UserProfileDTO(
                                                       userName = userName,
                                                       userId = userId,
                                                       profilePhoto = it.data,
                                                       officeId = officeId,
                                                       department = department,
                                                       designation = degignation,
                                                       email = email,
                                                       mobile = mobile
                                                   )
                                               ).collect{
                                                   when(it){
                                                       is ResultState.Loading->{
                                                           isLoading = true
                                                       }

                                                       is ResultState.Error->{
                                                           isLoading = false
                                                           Toast.makeText(context,it.message,Toast.LENGTH_LONG).show()
                                                           Log.d("ERROR_IS",it.message)
                                                       }

                                                       is ResultState.Success->{
                                                           navHostController.navigate(NavRoutes.MainScreen.Parent.route)
                                                       }
                                                   }
                                               }

                                           }
                                       }
                                   }
                               }
                            }

                        },
                        shape = RoundedCornerShape(5.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Blue,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = "Create")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ){paddingValues ->

        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ){

            item {
                Box(
                    modifier = Modifier
                        .size(150.dp)

                ) {
                    AsyncImage(
                        model = if (imageUri == null) R.drawable.avatar else imageUri,
                        contentDescription = null,
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    IconButton(
                        onClick = { imagePickerLauncher.launch("image/*") },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(40.dp)
                            .border(1.dp, Blue, CircleShape),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Blue
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = Color.White
                        )
                    }
                }

            }

            item {

                Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {

                    CommonTextField(
                        value = userName,
                        onValueChang = {userName = it},
                        label = "Employee Name*",
                        placeholder = "John",
                        isError = false,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        keyboardActions = KeyboardActions(onNext = {})
                    )

                    CommonTextField(
                        value = userId,
                        onValueChang = {userId = it},
                        label = "Employee Id*",
                        placeholder = "Enter your Id",
                        isError = false,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        keyboardActions = KeyboardActions(onNext = {})
                    )

                    Column {
                        CommonTextFieldWithTrailingIcon(
                            value =officeId,
                            onValueChang = {officeId = it},
                            label = "Office Id*",
                            placeholder = "Office/College/University",
                            isError = false,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            keyboardActions = KeyboardActions(onNext = {}),
                            trailingIcon = {
                                IconButton(onClick = { isWarning = !isWarning }) {
                                    Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = Yellow)
                                }
                            }
                        )
                        AnimatedVisibility(visible = isWarning) {
                            Box(modifier = Modifier
                                .padding(top = 10.dp)
                                .fillMaxWidth()
                                .background(color = Light_Yellow, shape = RoundedCornerShape(12.dp))
                                .padding(12.dp)
                            ){

                                Text(text = "Ask office code from your department. Your department verify you by this code.")
                            }
                        }

                    }

                    CommonTextField(
                        value = department,
                        onValueChang = {department = it},
                        label = "Department*",
                        placeholder = "Enter your department",
                        isError = false,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        keyboardActions = KeyboardActions(onNext = {})
                    )
                    CommonTextField(
                        value = degignation,
                        onValueChang = {degignation = it},
                        label = "Designation*",
                        placeholder = "Enter your Designation",
                        isError = false,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        keyboardActions = KeyboardActions(onNext = {})
                    )
                    CommonTextField(
                        value = email,
                        onValueChang = {email = it},
                        label = "Email*",
                        placeholder = "example@gamil.com",
                        isError = false,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        keyboardActions = KeyboardActions(onNext = {})
                    )
                    CommonTextField(
                        value = mobile,
                        onValueChang = {mobile = it},
                        label = "Mobile*",
                        placeholder = "94xxxxxxxx",
                        isError = false,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        keyboardActions = KeyboardActions(onNext = {})
                    )


                }

            }

        }

        if (isLoading) LoadingScreen()


    }

}

@Composable
fun CommonTextField(
    value:String,
    onValueChang:(String)->Unit,
    label:String,
    placeholder:String,
    isError:Boolean,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions
    ) {

    TextField(

        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = {onValueChang(it)},
        label = { Text(text = label)},
        placeholder = { Text(text = placeholder, color = Light_Black)},
        isError = isError,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent
        )

    )


}

@Composable
fun CommonTextFieldWithTrailingIcon(
    value:String,
    onValueChang:(String)->Unit,
    label:String,
    placeholder:String,
    isError:Boolean,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    trailingIcon:@Composable () ->Unit
) {

    TextField(

        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = {onValueChang(it)},
        label = { Text(text = label)},
        placeholder = { Text(text = placeholder, color = Light_Black)},
        isError = isError,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent
        ),
        trailingIcon = {
            trailingIcon()
        }

    )


}
