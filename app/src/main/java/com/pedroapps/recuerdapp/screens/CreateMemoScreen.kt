package com.pedroapps.recuerdapp.screens

import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
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
import com.pedroapps.recuerdapp.components.MyTimePickerDialog
import com.pedroapps.recuerdapp.utils.formatTime
import com.pedroapps.recuerdapp.utils.formatToStringDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateMemoScreen(
    paddingValues: PaddingValues
) {

    val memo = remember {
        mutableStateOf("")
    }

    val openDateDialog = remember {
        mutableStateOf(false)
    }
    
    val openTimeDialog = remember {
        mutableStateOf(false)
    }
    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState(is24Hour = false)

    val formattedDate = remember {
        derivedStateOf {
            //TODO("This is hardcoded right now, change to update on language change)
            datePickerState.formatToStringDate("en")
        }
    }

    val formattedTime = remember {
        derivedStateOf {
            timePickerState.formatTime()
        }
    }

    fun dismissDatePicker() {
        openDateDialog.value = false
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
                    openDateDialog.value = true
                }

        )


        OutlinedTextField(
            value = formattedTime.value,
            onValueChange = {},
            enabled = false,
            label = { Text(text = "At what time?")},
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
                    openTimeDialog.value = true
                }

        )


    }

    //TODO(make this dialog capable of selecting dates only from today's date forward)
    if (openDateDialog.value) {
        DatePickerDialog(
            onDismissRequest = { dismissDatePicker() },
            confirmButton = {
                TextButton(onClick = { openDateDialog.value = false }) {
                    Text(text = "Select")
                }
            },
            dismissButton = {
                TextButton(onClick = { dismissDatePicker() }) {
                    Text("Cancel")
                }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }


    if(openTimeDialog.value) {
        MyTimePickerDialog(onDismissRequest = { openTimeDialog.value = false }) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
                    .padding(top = 16.dp)
            ) {
                TimePicker(state = timePickerState)
                HorizontalDivider()
                Row {
                   TextButton(onClick = { openTimeDialog.value = false }) {
                       Text("Dismiss")
                   }

                    TextButton(onClick = { openTimeDialog.value = false }) {
                        Text(text = "Accept")
                    }
                }
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