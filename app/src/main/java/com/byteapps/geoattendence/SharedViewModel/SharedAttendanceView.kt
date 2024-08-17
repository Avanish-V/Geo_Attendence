package com.byteapps.geoattendence.SharedViewModel


import androidx.lifecycle.ViewModel
import com.byteapps.geoattendence.Utils.AtteData
import com.byteapps.geoattendence.Utils.AttendanceStatusDTO
import com.byteapps.geoattendence.Utils.Date
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SharedAttendanceView : ViewModel(){

    private val _indexed : MutableStateFlow<Int> = MutableStateFlow(0)
    private val indexed : StateFlow<Int> get() = _indexed

    private val _attendanceData : MutableStateFlow<AttendanceStatusDTO?> = MutableStateFlow(attendanceStatusList[0])
    val attendanceData : StateFlow<AttendanceStatusDTO?> get() = _attendanceData

    fun onNextClick(index:Int){

        if (indexed.value != attendanceStatusList.count()-1){
            _indexed.value = indexed.value+index
            _attendanceData.value = attendanceStatusList[indexed.value]
        }

    }

    fun onPreviousClick(index: Int){

        if (indexed.value != 0){
            _indexed.value = indexed.value-index
            _attendanceData.value = attendanceStatusList[indexed.value]
        }

    }


}




val attendanceStatusList = listOf<AttendanceStatusDTO>(

    AttendanceStatusDTO(
        month = "January 2024",
        data = listOf(
            AtteData(
                date = Date(
                    day = "01",
                    week = "Mon"
                ),
                checkIn = "09:00 AM",
                checkOut = "06:22 PM",
                status = "Late In"
            ), AtteData(
                date = Date(
                    day = "02",
                    week = "Tue"
                ),
                checkIn = "09:20 AM",
                checkOut = "06:00 PM",
                status = "Present"
            ), AtteData(
                date = Date(
                    day = "03",
                    week = "Wed"
                ),
                checkIn = "09:05 AM",
                checkOut = "06:07 PM",
                status = "Absent"
            ), AtteData(
                date = Date(
                    day = "04",
                    week = "Thu"
                ),
                checkIn = "09:11 AM",
                checkOut = "06:06 PM",
                status = "Present"
            )
        )
    ),
    AttendanceStatusDTO(
        month = "February 2024",
        data = listOf(
            AtteData(
                date = Date(
                    day = "01",
                    week = "Mon"
                ),
                checkIn = "09:22 AM",
                checkOut = "06:02 PM",
                status = "Present"
            ), AtteData(
                date = Date(
                    day = "02",
                    week = "Mon"
                ),
                checkIn = "09:10 AM",
                checkOut = "06:00 PM",
                status = "Present"
            ), AtteData(
                date = Date(
                    day = "03",
                    week = "Mon"
                ),
                checkIn = "09:09 AM",
                checkOut = "06:07 PM",
                status = "Early Leave"
            ), AtteData(
                date = Date(
                    day = "04",
                    week = "Mon"
                ),
                checkIn = "09:10 AM",
                checkOut = "05:02 PM",
                status = "Present"
            )
        )
    ), AttendanceStatusDTO(
        month = "March 2024",
        data = listOf(
            AtteData(
                date = Date(
                    day = "01",
                    week = "Mon"
                ),
                checkIn = "09:15 AM",
                checkOut = "06:02 PM",
                status = "Late In"
            ), AtteData(
                date = Date(
                    day = "01",
                    week = "Mon"
                ),
                checkIn = "09:15 AM",
                checkOut = "06:02 PM",
                status = "Present"
            ), AtteData(
                date = Date(
                    day = "01",
                    week = "Mon"
                ),
                checkIn = "09:15 AM",
                checkOut = "06:02 PM",
                status = "Present"
            ), AtteData(
                date = Date(
                    day = "01",
                    week = "Mon"
                ),
                checkIn = "09:15 AM",
                checkOut = "06:02 PM",
                status = "Late In"
            )
        )
    ),
    AttendanceStatusDTO(
        month = "April 2024",
        data = listOf(
            AtteData(
                date = Date(
                    day = "01",
                    week = "Mon"
                ),
                checkIn = "09:15 AM",
                checkOut = "06:02 PM",
                status = "Present"
            ), AtteData(
                date = Date(
                    day = "01",
                    week = "Mon"
                ),
                checkIn = "09:15 AM",
                checkOut = "06:02 PM",
                status = "Present"
            ), AtteData(
                date = Date(
                    day = "01",
                    week = "Mon"
                ),
                checkIn = "09:15 AM",
                checkOut = "06:02 PM",
                status = "Absent"
            ), AtteData(
                date = Date(
                    day = "01",
                    week = "Mon"
                ),
                checkIn = "09:15 AM",
                checkOut = "06:02 PM",
                status = "Present"
            )
        )
    ),
    AttendanceStatusDTO(
        month = "May 2024",
        data = listOf(
            AtteData(
                date = Date(
                    day = "01",
                    week = "Mon"
                ),
                checkIn = "09:15 AM",
                checkOut = "06:02 PM",
                status = "Present"
            ), AtteData(
                date = Date(
                    day = "01",
                    week = "Mon"
                ),
                checkIn = "09:15 AM",
                checkOut = "06:02 PM",
                status = "Present"
            ), AtteData(
                date = Date(
                    day = "01",
                    week = "Mon"
                ),
                checkIn = "09:15 AM",
                checkOut = "06:02 PM",
                status = "Late In"
            ), AtteData(
                date = Date(
                    day = "01",
                    week = "Mon"
                ),
                checkIn = "09:15 AM",
                checkOut = "06:02 PM",
                status = "Present"
            )
        )
    )

)