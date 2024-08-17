package com.byteapps.geoattendence

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.byteapps.Features.UserProfile.presantation.UserProfileViewModel
import com.byteapps.geoattendence.Authentication.presantation.Screens.ProfileSetup
import com.byteapps.geoattendence.Screens.MainScreen
import com.byteapps.geoattendence.Screens.AttendanceScreen
import com.byteapps.geoattendence.Screens.HomeScreen
import com.byteapps.geoattendence.UIComponents.LoadingScreen
import com.byteapps.geoattendence.UIComponents.StatusScreen
import com.byteapps.geoattendence.Utils.Date
import com.byteapps.geoattendence.Utils.NavRoutes
import com.byteapps.geoattendence.Utils.navItems
import com.byteapps.geoattendence.ui.theme.Blue
import com.byteapps.geoattendence.ui.theme.DarkSky
import com.byteapps.geoattendence.ui.theme.DarkWhite
import com.byteapps.geoattendence.ui.theme.Dark_Black
import com.byteapps.geoattendence.ui.theme.Dark_Voilet
import com.byteapps.geoattendence.ui.theme.Dull_White
import com.byteapps.geoattendence.ui.theme.GeoAttendenceTheme
import com.byteapps.geoattendence.ui.theme.Light_Black
import com.byteapps.geoattendence.ui.theme.Light_Pink
import com.byteapps.geoattendence.ui.theme.Light_Yellow
import com.byteapps.geoattendence.ui.theme.Pink
import com.byteapps.geoattendence.ui.theme.Yellow
import com.byteapps.serrvicewala.Authentication.presantation.Screens.OTPScreen
import com.byteapps.serrvicewala.Authentication.presantation.Screens.PhoneNumberScreen
import com.byteapps.wiseschool.GeoFencing.Permission.PermissionViewModel
import com.byteappstudio.b2ccart.Authentications.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val userProfileViewModel: UserProfileViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()
    private val permissionViewModel:PermissionViewModel by viewModels()
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()

        // Use SplashScreen to ensure all necessary checks are done before displaying any content
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition {
            userProfileViewModel.validateUser.value.isLoading
        }

        auth = FirebaseAuth.getInstance()
        val context = applicationContext

        setContent {


            // Launch the user validation in a LaunchedEffect
            LaunchedEffect(context) {
                val currentUserUid = auth.currentUser?.uid
                if (currentUserUid != null) {
                    userProfileViewModel.validateUser(currentUserUid)
                }
            }

            val resultState = userProfileViewModel.validateUser.collectAsState()

            val isLoggedIn by remember {
                mutableStateOf(auth.currentUser?.uid?.isNotEmpty() == true)
            }

            GeoAttendenceTheme {

                when {
                    resultState.value.isLoading -> {
                        // Display loading screen while data is being loaded
                        LoadingScreen()
                    }
                    resultState.value.error.isNotEmpty() -> {
                        // Handle error state (e.g., show an error message)
                        StatusScreen(text = resultState.value.error)
                    }
                    resultState.value.isExist != null -> {
                        val isExist = resultState.value.isExist
                        val navHostController = rememberNavController()

                        val startDestination = when {
                            !isLoggedIn -> NavRoutes.Authentication.route
                            isExist == false -> NavRoutes.ProfileSetup.route
                            else -> NavRoutes.MainScreen.route
                        }

                        // Log to check what the startDestination is set to
                        Log.d("Navigation", "Start destination: $startDestination")

                        NavHost(
                            navController = navHostController,
                            startDestination = startDestination,
                            enterTransition = { EnterTransition.None },
                            exitTransition = { ExitTransition.None },
                            popEnterTransition = { EnterTransition.None },
                            popExitTransition = { ExitTransition.None },
                        ) {
                            // Define your navigation structure here
                            navigation(
                                startDestination = NavRoutes.Authentication.PhoneNumber.route,
                                route = NavRoutes.Authentication.route,
                                enterTransition = { EnterTransition.None },
                                exitTransition = { ExitTransition.None },
                                popEnterTransition = { EnterTransition.None },
                                popExitTransition = { ExitTransition.None }
                            ) {
                                composable(route = NavRoutes.Authentication.PhoneNumber.route) {
                                    PhoneNumberScreen(
                                        navHostController = navHostController,
                                        authViewModel = authViewModel
                                    )
                                }
                                composable(route = NavRoutes.Authentication.OTP.route) {
                                    OTPScreen(
                                        navHostController = navHostController,
                                        authViewModel = authViewModel,
                                        userProfileViewModel = userProfileViewModel
                                    )
                                }
                            }

                            composable(route = NavRoutes.ProfileSetup.route,
                                enterTransition = { EnterTransition.None },
                                exitTransition = { ExitTransition.None },
                                popEnterTransition = { EnterTransition.None },
                                popExitTransition = { ExitTransition.None }
                            ) {
                                ProfileSetup(
                                    navHostController = navHostController,
                                    userProfileViewModel = userProfileViewModel
                                )
                            }

                            navigation(
                                startDestination = NavRoutes.MainScreen.Parent.route,
                                route = NavRoutes.MainScreen.route,
                                enterTransition = { EnterTransition.None },
                                exitTransition = { ExitTransition.None },
                                popEnterTransition = { EnterTransition.None },
                                popExitTransition = { ExitTransition.None }
                            ) {
                                composable(NavRoutes.MainScreen.Parent.route) {
                                    MainScreen(
                                        permissionViewModel = permissionViewModel,
                                        context = context
                                    )
                                }
                            }

                            navigation(
                                startDestination = NavRoutes.MainScreen.Parent.Home.route,
                                route = NavRoutes.MainScreen.Parent.route,
                                enterTransition = { EnterTransition.None },
                                exitTransition = { ExitTransition.None },
                                popEnterTransition = { EnterTransition.None },
                                popExitTransition = { ExitTransition.None }
                            ) {
                                composable(route = NavRoutes.MainScreen.Parent.Home.route) {
                                    HomeScreen()
                                }
                                composable(route = NavRoutes.MainScreen.Parent.Attendance.route) {
                                    AttendanceScreen(navHostController = navHostController)
                                }
                                composable(route = NavRoutes.MainScreen.Parent.ApplyLeave.route) {
                                    ApplyLeaveScreen()
                                }
                                composable(route = NavRoutes.MainScreen.Parent.Profile.route) {
                                    ProfileScreen()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}







@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    Text(text = "Profile")
}


@Composable
fun ApplyLeaveScreen(modifier: Modifier = Modifier) {
    Text(text = "Apply Leave")
}

@Composable
fun Status(icon: Int, text: String, statusText: String) {

    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {

        Image(
            modifier = Modifier.size(32.dp),
            painter = painterResource(id = icon),
            contentDescription = null
        )

        Text(
            text = text.ifEmpty { "--:--" },
            style = MaterialTheme.typography.titleSmall,
            color = Light_Black
        )

        Text(text = statusText, style = MaterialTheme.typography.displaySmall, color = Light_Black)
    }

}

@Composable
fun AttendanceStatus() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(12.dp)
            )
    ) {


        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Column {
                Text(text = "Attendance", style = MaterialTheme.typography.displayMedium)
                Text(
                    text = "Current Month",
                    style = MaterialTheme.typography.titleSmall,
                    color = Light_Black
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(Modifier.fillMaxWidth()) {

                AttendanceStatusSingle(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                        .background(
                            color = Yellow,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    primaryColor = Yellow,
                    secondaryColor = Light_Yellow,
                    statusText = "Present",
                    attendanceCount = "08"
                )
                Spacer(modifier = Modifier.width(20.dp))
                AttendanceStatusSingle(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                        .background(
                            color = Pink,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    primaryColor = Pink,
                    secondaryColor = Light_Pink,
                    statusText = "Late In",
                    attendanceCount = "08"
                )
            }

            Row(Modifier.fillMaxWidth()) {

                AttendanceStatusSingle(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                        .background(
                            color = Blue,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    primaryColor = Blue,
                    secondaryColor = Dull_White,
                    statusText = "Absent",
                    attendanceCount = "08"
                )
                Spacer(modifier = Modifier.width(20.dp))
                AttendanceStatusSingle(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                        .background(
                            color = DarkSky,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    primaryColor = DarkSky,
                    secondaryColor = DarkWhite,
                    statusText = "Leave",
                    attendanceCount = "08"
                )
            }
        }

    }

}


@Composable
fun AttendanceStatusSingle(
    modifier: Modifier = Modifier,
    primaryColor: Color,
    secondaryColor: Color,
    statusText: String,
    attendanceCount: String
) {

    Box(modifier = modifier) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 5.dp)
                .background(
                    color = secondaryColor,
                    shape = RoundedCornerShape(6.dp)
                ),
        ) {

            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = attendanceCount,
                    style = MaterialTheme.typography.displayLarge,
                    color = primaryColor
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.displayMedium,
                        color = primaryColor
                    )

                    Image(
                        modifier = Modifier.size(18.dp),
                        painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(color = primaryColor)
                    )
                }

            }

        }

    }


}


@Composable
fun SingleDayStatus(date: Date, checkIn: String, checkOut: String, status: String) {

    Column {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {

                Box(
                    modifier = Modifier.border(
                        width = 1.dp,
                        color = Light_Black,
                        shape = RoundedCornerShape(5.dp)
                    ),
                ) {

                    Column(
                        modifier = Modifier.padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = date.day, style = MaterialTheme.typography.displayMedium)
                        Text(
                            text = date.week,
                            style = MaterialTheme.typography.displaySmall,
                            color = Light_Black
                        )
                    }
                }

            }

            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {

                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {

                    Image(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = R.drawable.check_in),
                        contentDescription = null
                    )

                    Text(
                        text = checkIn,
                        style = MaterialTheme.typography.titleSmall,
                        color = Dark_Black
                    )

                    Text(
                        text = "Check In",
                        style = MaterialTheme.typography.titleSmall,
                        color = Light_Black
                    )
                }

            }

            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {

                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {

                    Image(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = R.drawable.check_out),
                        contentDescription = null
                    )

                    Text(
                        text = checkOut,
                        style = MaterialTheme.typography.titleSmall,
                        color = Dark_Black
                    )

                    Text(
                        text = "Check Out",
                        style = MaterialTheme.typography.titleSmall,
                        color = Light_Black
                    )
                }

            }

            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {

                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {

                    Image(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = R.drawable.total_hours),
                        contentDescription = null
                    )

                    Text(
                        text = "09:02 AM",
                        style = MaterialTheme.typography.titleSmall,
                        color = Dark_Black
                    )

                    Text(
                        text = "Hours",
                        style = MaterialTheme.typography.titleSmall,
                        color = Light_Black
                    )
                }
            }

            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {

                Text(
                    modifier = Modifier
                        .background(
                            color = if (status == "Late In") Light_Pink else if (status == "Present") DarkWhite else if (status == "Absent") DarkWhite else if (status == "Early Leave") Light_Yellow else Color.Transparent,
                            shape = RoundedCornerShape(5.dp)
                        )
                        .padding(5.dp),
                    text = status,
                    color = if (status == "Late In") Pink else if (status == "Present") Blue else if (status == "Absent") Dark_Voilet else if (status == "Early Leave") Yellow else Color.Transparent,
                    style = MaterialTheme.typography.titleSmall
                )

            }

        }
        HorizontalDivider(modifier = Modifier.padding(top = 12.dp))
    }

}



