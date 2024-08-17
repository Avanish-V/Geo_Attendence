package com.byteapps.geoattendence.Utils

import com.byteapps.geoattendence.R

data class DrawerItems(
    val drawerTitle:String,
    val drawerIcon:Int,
    val route: NavRoutes,
)

sealed class NavRoutes(val route:String){

    object Authentication:NavRoutes("Authentication"){
        object PhoneNumber:NavRoutes("PhoneNumber")
        object OTP:NavRoutes("OTP")
    }

    object ProfileSetup:NavRoutes("ProfileSetup")

    object MainScreen:NavRoutes("MainScreen"){

        object Parent:NavRoutes("Parent"){
            object Home : NavRoutes("Home")
            object Attendance : NavRoutes("Attendance")
            object ApplyLeave : NavRoutes("Apply Leave")
            object Profile : NavRoutes("Profile")
        }

    }


}


val navItems = listOf<DrawerItems>(

    DrawerItems(
        drawerTitle = "Home",
        drawerIcon = R.drawable.home,
        route = NavRoutes.MainScreen.Parent.Home
    ),
    DrawerItems(
        drawerTitle = "Attendance",
        drawerIcon = R.drawable.booking__1_,
        route = NavRoutes.MainScreen.Parent.Attendance
    ),
    DrawerItems(
        drawerTitle = "Apply Leave",
        drawerIcon = R.drawable.house_leave,
        route = NavRoutes.MainScreen.Parent.ApplyLeave
    ),
    DrawerItems(
        drawerTitle = "Profile",
        drawerIcon = R.drawable.user,
        route = NavRoutes.MainScreen.Parent.Profile
    )
)

data class AttendanceStatusDTO(
    val month:String,
    val data:List<AtteData>
)

data class Date(
    val day:String,
    val week:String
)
data class AtteData(
    val date: Date,
    val checkIn:String,
    val checkOut:String,
    val status:String
)

