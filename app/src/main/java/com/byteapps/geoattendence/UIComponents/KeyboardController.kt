package com.byteapps.geoattendence.UIComponents

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalSoftwareKeyboardController

@Composable
fun KeyboardController(isHide : Boolean){
    val keyboardController = LocalSoftwareKeyboardController.current
    if (isHide){
        keyboardController?.hide()
    }
}