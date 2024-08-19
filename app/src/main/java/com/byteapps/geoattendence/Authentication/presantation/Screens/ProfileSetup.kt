package com.byteapps.geoattendence.Authentication.presantation.Screens

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.byteapps.Features.UserProfile.data.UserProfileDTO
import com.byteapps.Features.UserProfile.presantation.UserProfileViewModel
import com.byteapps.geoattendence.ui.theme.Blue
import com.byteapps.geoattendence.R
import com.byteapps.geoattendence.UIComponents.LoadingScreen
import com.byteapps.geoattendence.Utils.NavRoutes
import com.byteapps.geoattendence.Utils.ResultState
import com.byteapps.geoattendence.Vailidation.ProfileFormEvent
import com.byteapps.geoattendence.Vailidation.ProfileFormValidation
import com.byteapps.geoattendence.ui.theme.Light_Black
import com.byteapps.geoattendence.ui.theme.Light_Yellow
import com.byteapps.geoattendence.ui.theme.Pink
import com.byteapps.geoattendence.ui.theme.Yellow

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSetup(
    navHostController: NavHostController,
    userProfileViewModel: UserProfileViewModel
) {

    val validationViewModel = viewModel<ProfileFormValidation>()
    val state = validationViewModel.state.collectAsState()

    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    var imageUri by remember {
       mutableStateOf<Uri?>(null)
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
        validationViewModel.onEvent(ProfileFormEvent.UserImageChanged(uri))
    }

    var isWarning by remember {
        mutableStateOf(false)
    }
    var isLoading by remember {
        mutableStateOf(false)
    }



    LaunchedEffect(context) {
        validationViewModel.validationEvents.collect { event ->
            when (event) {
                is ProfileFormValidation.ValidationEvent.Success -> {
                    if (imageUri != null) {
                        userProfileViewModel.uploadUserImage(imageUri = imageUri!!).collect { uploadResult ->
                            when (uploadResult) {
                                is ResultState.Loading -> {
                                    isLoading = true
                                }
                                is ResultState.Error -> {
                                    isLoading = false
                                    Toast.makeText(context, uploadResult.message, Toast.LENGTH_LONG).show()
                                }
                                is ResultState.Success -> {
                                    userProfileViewModel.createUserProfile(
                                        userProfileDTO = UserProfileDTO(
                                            userName = state.value.userName,
                                            userId = state.value.useID,
                                            profilePhoto = uploadResult.data,
                                            officeId = state.value.officeId,
                                            department = state.value.department,
                                            designation = state.value.degignation,
                                            email = state.value.email,
                                            mobile = state.value.mobile
                                        )
                                    ).collect { createResult ->
                                        when (createResult) {
                                            is ResultState.Loading -> {
                                                isLoading = true
                                            }
                                            is ResultState.Error -> {
                                                isLoading = false
                                                Toast.makeText(context, createResult.message, Toast.LENGTH_LONG).show()
                                            }
                                            is ResultState.Success -> {
                                                navHostController.navigate(NavRoutes.MainScreen.Parent.route)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        Toast.makeText(context, "Please select an image.", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }


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

                            validationViewModel.onEvent(ProfileFormEvent.Submit)

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
                        model = state.value.userImage ?: R.drawable.avatar,
                        contentDescription = null,
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    IconButton(
                        onClick = { imagePickerLauncher.launch("image/*")},
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(40.dp)
                            .border(
                                1.dp,
                                if (state.value.userImageError != null) Pink else Blue,
                                CircleShape
                            ),
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
                        value = state.value.userName,
                        onValueChang = {validationViewModel.onEvent(ProfileFormEvent.UserNameChanged(it))},
                        label = "Employee Name*",
                        placeholder = "John",
                        isError = state.value.userNameError != null,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        keyboardActions = KeyboardActions(onNext = {})
                    )

                    CommonTextField(
                        value = state.value.useID,
                        onValueChang = {validationViewModel.onEvent(ProfileFormEvent.UserIDChanged(it))},
                        label = "Employee Id*",
                        placeholder = "Enter your Id",
                        isError = state.value.useIDError != null,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        keyboardActions = KeyboardActions(onNext = {})
                    )

                    Column {
                        CommonTextFieldWithTrailingIcon(
                            value =state.value.officeId,
                            onValueChang = {validationViewModel.onEvent(ProfileFormEvent.OfficeIDChanged(it))},
                            label = "Office Id*",
                            placeholder = "Office/College/University",
                            isError = state.value.officeIDError != null,
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
                        value = state.value.department,
                        onValueChang = {validationViewModel.onEvent(ProfileFormEvent.DepartmentChanged(it))},
                        label = "Department*",
                        placeholder = "Enter your department",
                        isError = state.value.departmentError != null,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        keyboardActions = KeyboardActions(onNext = {})
                    )
                    CommonTextField(
                        value = state.value.degignation,
                        onValueChang = {validationViewModel.onEvent(ProfileFormEvent.DegignationChanged(it))},
                        label = "Designation*",
                        placeholder = "Enter your Designation",
                        isError = state.value.degignationError != null,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        keyboardActions = KeyboardActions(onNext = {})
                    )
                    CommonTextField(
                        value = state.value.email,
                        onValueChang = {validationViewModel.onEvent(ProfileFormEvent.EmailChanged(it))},
                        label = "Email*",
                        placeholder = "example@gamil.com",
                        isError = state.value.emailError != null,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        keyboardActions = KeyboardActions(onNext = {})
                    )
                    CommonTextField(
                        value = state.value.mobile,
                        onValueChang = {validationViewModel.onEvent(ProfileFormEvent.MobileChanged(it))},
                        label = "Mobile*",
                        placeholder = "94xxxxxxxx",
                        isError = state.value.mobileError != null,
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
            unfocusedContainerColor = Color.Transparent,
            errorContainerColor = Color.Transparent
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
            unfocusedContainerColor = Color.Transparent,
            errorContainerColor = Color.Transparent
        ),
        trailingIcon = {
            trailingIcon()
        }

    )


}
