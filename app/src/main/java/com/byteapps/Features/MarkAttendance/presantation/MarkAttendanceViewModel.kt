package com.byteapps.Features.MarkAttendance.presantation

import android.content.Context
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byteapps.Features.MarkAttendance.domain.MarkAttendanceRepository
import com.byteapps.geoattendence.Utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarkAttendanceViewModel @Inject constructor(private val markAttendanceRepository: MarkAttendanceRepository) : ViewModel() {

    private val _result : MutableStateFlow<MarkAttendanceResultState> = MutableStateFlow(MarkAttendanceResultState())
    val result :StateFlow<MarkAttendanceResultState>get() = _result

    fun markAttendance(context: Context){
        viewModelScope.launch {
            markAttendanceRepository.markAttendance(context)
                .collectLatest{
                    when(it){
                        is ResultState.Loading->{
                            _result.value = MarkAttendanceResultState(isLoading = true)
                        }
                        is ResultState.Error->{
                            _result.value = MarkAttendanceResultState(error = it.message)
                        }
                        is ResultState.Success->{
                            _result.value = MarkAttendanceResultState(isSuccess = it.data)
                        }
                    }
                }
        }
    }


}
data class MarkAttendanceResultState(
    val isLoading:Boolean = false,
    val isSuccess:Boolean? = null,
    val error:String = ""
)