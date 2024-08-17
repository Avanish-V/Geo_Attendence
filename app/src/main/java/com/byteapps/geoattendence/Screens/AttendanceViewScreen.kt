package com.byteapps.geoattendence.Screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.byteapps.geoattendence.SharedViewModel.SharedAttendanceView
import com.byteapps.geoattendence.SharedViewModel.attendanceStatusList
import com.byteapps.geoattendence.SingleDayStatus
import com.byteapps.geoattendence.Utils.Date

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AttendanceScreen(navHostController: NavHostController) {

    val sharedAttendanceView: SharedAttendanceView = viewModel()

    val attendanceData = sharedAttendanceView.attendanceData.collectAsState().value

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = {
            attendanceStatusList.count()
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    Row(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        if (attendanceData != null) {

                            IconButton(onClick = { sharedAttendanceView.onPreviousClick(1) }) {
                                Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "")
                            }

                            Text(
                                text = attendanceStatusList[pagerState.currentPage].month,
                                style = MaterialTheme.typography.displayMedium
                            )

                            IconButton(onClick = { sharedAttendanceView.onNextClick(1) }) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowRight,
                                    contentDescription = ""
                                )
                            }
                        }
                    }

                }
            )
        }
    ) { paddingValues ->

        if (attendanceData != null) {

            HorizontalPager(modifier = Modifier.padding(paddingValues).fillMaxSize(), contentPadding = PaddingValues(top = 20.dp), verticalAlignment = Alignment.Top, state = pagerState) {

                LazyColumn(
                    contentPadding = PaddingValues(2.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    items(attendanceStatusList[pagerState.currentPage].data) {

                        SingleDayStatus(
                            date = Date(
                                day = it.date.day,
                                week = it.date.week
                            ),
                            checkIn = it.checkIn,
                            checkOut = it.checkOut,
                            status = it.status
                        )
                    }
                }
            }
        }
    }
}