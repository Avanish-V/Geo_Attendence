package com.byteapps.Features.MarkAttendance.domain

import android.content.Context
import com.byteapps.geoattendence.Utils.ResultState
import kotlinx.coroutines.flow.Flow

interface MarkAttendanceRepository {

    suspend fun markAttendance(context: Context):Flow<ResultState<Boolean>>

    suspend fun geoAttendance(context: Context):Flow<ResultState<Boolean>>

}