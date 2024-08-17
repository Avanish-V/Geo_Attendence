package com.byteapps.geoattendence.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.byteapps.geoattendence.AttendanceStatus
import com.byteapps.geoattendence.R
import com.byteapps.geoattendence.Status
import com.byteapps.geoattendence.UIComponents.CommonTextField
import com.byteapps.geoattendence.ui.theme.Background
import com.byteapps.geoattendence.ui.theme.Green
import com.byteapps.geoattendence.ui.theme.Light_Black
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    Scaffold(

        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Hi, Vishakha",
                        style = MaterialTheme.typography.displayMedium)
                    },
                navigationIcon = {

                    IconButton(
                        modifier = Modifier.padding(start = 10.dp),
                        onClick = { scope.launch { drawerState.open() } }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ggirl),
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                    }

                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Background

    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            item {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(12.dp)
                        )
                ) {

                    Column(
                        modifier = Modifier.padding(vertical = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(28.dp)
                    ) {

                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(
                                text = "09:10 AM",
                                style = MaterialTheme.typography.displayLarge
                            )

                            Text(
                                text = "15 Aug, 2024-Friday",
                                style = MaterialTheme.typography.displayMedium,
                                color = Light_Black
                            )

                        }

                        Card(
                            modifier = Modifier
                                .size(80.dp)
                                .shadow(
                                    elevation = 30.dp,
                                    ambientColor = Green,
                                    spotColor = Green
                                ),
                            colors = CardDefaults.cardColors(
                                containerColor = Green
                            ),
                            onClick = {

                            },
                            shape = CircleShape

                        ) {

                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {

                                Image(
                                    modifier = Modifier.size(42.dp),
                                    painter = painterResource(id = R.drawable.tap),
                                    contentDescription = null,
                                    colorFilter = ColorFilter.tint(color = Color.White)
                                )

                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {

                            Status(
                                icon = R.drawable.check_in,
                                text = "09:10 AM",
                                statusText = "Check In",
                            )

                            Status(
                                icon = R.drawable.check_out,
                                text = "",
                                statusText = "Check Out"
                            )

                            Status(
                                icon = R.drawable.total_hours,
                                text = "",
                                statusText = "Hours"
                            )

                        }
                    }
                }
            }

            item {
                AttendanceStatus()
            }


        }
    }
}