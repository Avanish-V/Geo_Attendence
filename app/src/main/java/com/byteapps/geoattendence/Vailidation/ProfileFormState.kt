package com.byteapps.geoattendence.Vailidation

import android.net.Uri

data class ProfileFormState(

    val userName:String = "",
    val userNameError : String? = null,

    val userImage:Uri? = null,
    val userImageError : String? = null,

    val useID:String = "",
    val useIDError : String? = null,

    val officeId:String = "",
    val officeIDError : String? = null,

    val department:String = "",
    val departmentError : String? = null,

    val degignation:String = "",
    val degignationError : String? = null,

    val mobile:String = "",
    val mobileError : String? = null,

    val email:String = "",
    val emailError : String? = null,


)
