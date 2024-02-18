package com.pedroapps.recuerdapp.screens

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
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset

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
    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {

            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val localDateTime = LocalDateTime.now().with(LocalTime.MIN)
                val startOfDayToday = localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli()
                return utcTimeMillis >= startOfDayToday
            }
        }
    )

    val timePickerState = rememberTimePickerState(is24Hour = false)


    val formattedDate = remember {
        derivedStateOf {
            //TODO("This is hardcoded right now, change to update on language change)
            datePickerState.formatToStringDate("en")
        }
    }

    val formattedTime = remember {
        mutableStateOf("No time selected yet")
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
            label = { Text(text = "At what time?") },
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


    if (openDateDialog.value) {
        DatePickerDialog(
            onDismissRequest = {
                dismissDatePicker(
                    openDateDialog = openDateDialog,
                    dateState = datePickerState
                )
            },
            confirmButton = {
                TextButton(onClick = { openDateDialog.value = false }) {
                    Text(text = "Select")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    dismissDatePicker(
                        openDateDialog = openDateDialog,
                        dateState = datePickerState
                    )
                }) {
                    Text("Cancel")
                }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }


    if (openTimeDialog.value) {
        MyTimePickerDialog(onDismissRequest = { openTimeDialog.value = false }) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp)
            ) {
                TimePicker(state = timePickerState)
                HorizontalDivider()
                Row {
                    TextButton(onClick = {
                        dismissTimePicker(
                            openTimeDialog = openTimeDialog,
                            formattedTime = formattedTime
                        )
                    }) {
                        Text("Dismiss")
                    }

                    TextButton(onClick = {
                        selectTime(
                            openTimeDialog = openTimeDialog,
                            timeState = timePickerState,
                            formattedTime = formattedTime
                        )
                    }) {
                        Text(text = "Accept")
                    }
                }
            }

        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
fun dismissDatePicker(
    openDateDialog: MutableState<Boolean>,
    dateState: DatePickerState,
) {
    openDateDialog.value = false
    dateState.selectedDateMillis = null
}

@OptIn(ExperimentalMaterial3Api::class)
fun selectTime(
    openTimeDialog: MutableState<Boolean>,
    timeState: TimePickerState,
    formattedTime: MutableState<String>
) {
    openTimeDialog.value = false
    formattedTime.value = timeState.formatTime()
}


fun dismissTimePicker(
    openTimeDialog: MutableState<Boolean>,
    formattedTime: MutableState<String>
) {
    openTimeDialog.value = false
    formattedTime.value = "No time selected yet"
}


@Preview(showBackground = true)
@Composable
fun CreateMemoScreenPreview() {
    val paddingValues = PaddingValues()
    CreateMemoScreen(paddingValues = paddingValues)
}