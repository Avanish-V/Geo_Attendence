package com.byteapps.geoattendence.UIComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


@Composable
fun StatusScreenWithButton(text:String,buttonText:String,onClick:()->Unit) {

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White), contentAlignment = Alignment.Center){

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {

           Text(text = text, textAlign = TextAlign.Center)
           OutlinedButton(onClick = {onClick() }) {
               Text(text = buttonText)
           }
        }

    }

}

@Composable
fun LoadingScreen() {

    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.White), contentAlignment = Alignment.Center){



        CircularProgressIndicator(
            strokeCap = StrokeCap.Round
        )

    }

}

@Composable
fun StatusScreen(text:String) {

    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.White), contentAlignment = Alignment.Center){

            Text(text = text)

        }

    }

