package com.pedroapps.recuerdapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun TestScreen(
    paddingValues: PaddingValues,
    showTestNotification: () -> Unit,
    showNotificationTenSeconds: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues = paddingValues)
    ) {
        Text(
            text = "Test Screen",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        TextButton(onClick = showTestNotification) {
            Text(text = "Receive notification now")
        }
        
        TextButton(onClick = showNotificationTenSeconds) {
            Text(text = "ReceiveNotification in 10 seconds")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TestScreenPreview() {
    val paddingValues = PaddingValues()

    TestScreen(
        paddingValues = paddingValues,
        showTestNotification = {},
        showNotificationTenSeconds = {}
    )
}