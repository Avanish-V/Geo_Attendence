package com.byteapps.geoattendence.UIComponents

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.byteapps.geoattendence.ui.theme.Dark_Black

@Composable
fun CommonTextField(
    modifier: Modifier = Modifier,
    value:String,
    onValueChange:(String)->Unit,
    placeholder:String,
    isError:Boolean,
    prefix:String,
    leadingIcon: @Composable () -> Unit,
    keyboardOptions:KeyboardOptions,
    keyboardActions: KeyboardActions
) {

    TextField(
        modifier = Modifier.fillMaxWidth().border(
            1.dp,
            color = Dark_Black,
            shape = RoundedCornerShape(5.dp)
        ),
        value = value,
        onValueChange = {
            onValueChange.invoke(it)
        },
        placeholder = {
            Text(text = placeholder)
        },
        isError = isError,
        prefix = {

        },
        leadingIcon = {
            leadingIcon()
        },
        singleLine = true,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent


        )

    )



}