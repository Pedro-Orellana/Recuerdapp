package com.pedroapps.recuerdapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pedroapps.recuerdapp.utils.formatToStringDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateMemoScreen(
    paddingValues: PaddingValues
) {

    val memo = remember {
        mutableStateOf("")
    }

    val openDialog = remember {
        mutableStateOf(false)
    }
    val datePickerState = rememberDatePickerState()

    val formattedDate = remember {
        derivedStateOf {
            //TODO("This is hardcoded right now, change to update on language change)
            datePickerState.formatToStringDate("en")
        }
    }

    fun dismissDatePicker() {
        openDialog.value = false
        datePickerState.selectedDateMillis = null
    }


    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues = paddingValues)
    ) {
        Text(
            text = "Create a new memo",
            fontSize = 24.sp,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(start = 12.dp, top = 12.dp)
        )

        OutlinedTextField(
            value = memo.value,
            onValueChange = { memo.value = it },
            label = { Text(text = "What do you want to remember?") },
            minLines = 5,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp, top = 50.dp)
                .clip(shape = RoundedCornerShape(8.dp))
        )

        OutlinedTextField(
           // value = datePickerState.selectedDateMillis.toString(),
            value = formattedDate.value,
            onValueChange = { },
            enabled = false,
            label = { Text(text = "When?") },
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp, top = 28.dp)
                .clickable {
                    openDialog.value = true
                }

        )

        if (openDialog.value) {
            DatePickerDialog(
                onDismissRequest = { dismissDatePicker() },
                confirmButton = {
                    TextButton(onClick = { openDialog.value = false }) {
                        Text(text = "Select")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { dismissDatePicker() }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun CreateMemoScreenPreview() {
    val paddingValues = PaddingValues()
    CreateMemoScreen(paddingValues = paddingValues)
}