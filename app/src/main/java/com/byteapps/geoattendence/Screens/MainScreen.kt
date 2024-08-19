package com.byteapps.geoattendence.Screens

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.byteapps.Features.MarkAttendance.presantation.MarkAttendanceViewModel
import com.byteapps.geoattendence.ApplyLeaveScreen
import com.byteapps.geoattendence.Authentication.presantation.Screens.ProfileSetup
import com.byteapps.geoattendence.ProfileScreen
import com.byteapps.geoattendence.R
import com.byteapps.geoattendence.Utils.NavRoutes
import com.byteapps.geoattendence.Utils.navItems
import com.byteapps.geoattendence.ui.theme.Blue
import com.byteapps.geoattendence.ui.theme.DarkWhite
import com.byteapps.geoattendence.ui.theme.Dark_Black
import com.byteapps.geoattendence.ui.theme.Dull_White
import com.byteapps.serrvicewala.LocationPermission.Permission.PermissionDialog
import com.byteapps.serrvicewala.LocationPermission.Permission.StateDialog
import com.byteapps.wiseschool.GeoFencing.Permission.PermissionViewModel
import kotlinx.coroutines.launch

@SuppressLint("RestrictedApi")
@Composable
fun MainScreen(
    permissionViewModel: PermissionViewModel,
    markAttendanceViewModel: MarkAttendanceViewModel,
    context: Context
) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navHostController = rememberNavController()

    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val locationPermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->

            permissionViewModel.onPermissionResult(
                permission = Manifest.permission.ACCESS_FINE_LOCATION,
                isGrant = isGranted,
                context = context,
                alertDialog = {}

            )

        }
    )

    LaunchedEffect (Unit){
        locationPermissionResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

        StateDialog(
        dialogQueue = permissionViewModel.visiblePermissionDialogQueue,
        permissionViewModel = permissionViewModel,

    )

    ModalNavigationDrawer(

        drawerState = drawerState,
        gesturesEnabled = false,
        drawerContent = {

            ModalDrawerSheet(
                modifier = Modifier.padding(end = 80.dp),
                drawerContainerColor = Color.White
            ) {
                LazyColumn(
                    contentPadding = PaddingValues(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    item {

                        Image(
                            painter = painterResource(id = R.drawable.geo_att_logo),
                            contentDescription = "",
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    items(navItems) {

                        NavigationDrawerItem(

                            label = {
                                Text(text = it.drawerTitle, style = MaterialTheme.typography.displaySmall)
                            },
                            selected = currentDestination?.route == it.drawerTitle,
                            onClick = {
                                navHostController.navigate(it.route.route).apply {
                                    scope.launch {
                                        drawerState.close()
                                    }
                                }
                            },
                            shape = RoundedCornerShape(5.dp),
                            icon = {
                                Image(
                                    modifier = Modifier.size(20.dp),
                                    painter = painterResource(id = it.drawerIcon),
                                    contentDescription = "",
                                    colorFilter = ColorFilter.tint(color = if (currentDestination?.route == it.route.route) Color.White else Color.Black)
                                )
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = Blue,
                                unselectedContainerColor = Color.Transparent,
                                unselectedIconColor = Color.Black,
                                selectedTextColor = Color.White
                            )
                        )
                    }
                }
            }
        }

    ) {

        Column(modifier = Modifier.fillMaxSize()) {

            NavHost(modifier = Modifier.weight(1f), navController = navHostController, startDestination = NavRoutes.MainScreen.Parent.Home.route) {

                composable(route = NavRoutes.MainScreen.Parent.Home.route){
                    HomeScreen(
                        markAttendanceViewModel = markAttendanceViewModel,
                        context = context
                    )
                }
                composable(route = NavRoutes.MainScreen.Parent.Attendance.route){
                    AttendanceScreen(navHostController)
                }
                composable(route = NavRoutes.MainScreen.Parent.ApplyLeave.route){
                    ApplyLeaveScreen()
                }
                composable(route = NavRoutes.MainScreen.Parent.Profile.route){
                    ProfileScreen()
                }

            }

            BottomAppBar(containerColor = Color.White) {

                navItems.forEachIndexed { _, drawerItems ->

                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.route == drawerItems.route.route } == true,
                        onClick = {
                            if (currentDestination?.route != drawerItems.route.route) {
                                navHostController.navigate(drawerItems.route.route) {

                                    popUpTo(navHostController.graph.findStartDestination().id) {

                                        saveState = false
                                    }

                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id =  drawerItems.drawerIcon ),
                                contentDescription = null,
                                modifier = Modifier.size(22.dp)
                            )
                        },
                        label = {
                            Text(text = drawerItems.drawerTitle, style = MaterialTheme.typography.titleSmall)
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Dull_White
                        )
                    )
                }
            }
        }
    }
}
