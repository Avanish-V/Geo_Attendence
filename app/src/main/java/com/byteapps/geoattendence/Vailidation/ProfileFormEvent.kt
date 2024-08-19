package com.byteapps.geoattendence.Vailidation

import android.net.Uri

sealed class ProfileFormEvent {

    data class UserNameChanged(val userName:String) : ProfileFormEvent()
    data class UserIDChanged(val userID:String) : ProfileFormEvent()
    data class UserImageChanged(val userImage: Uri?) : ProfileFormEvent()
    data class OfficeIDChanged(val officeId:String) : ProfileFormEvent()
    data class DepartmentChanged(val department:String) : ProfileFormEvent()
    data class DegignationChanged(val degignation:String) : ProfileFormEvent()
    data class MobileChanged(val mobile:String) : ProfileFormEvent()
    data class EmailChanged(val email:String) : ProfileFormEvent()
    object Submit:ProfileFormEvent()
}