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
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getString
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.pedroapps.recuerdapp.R
import com.pedroapps.recuerdapp.components.MyTimePickerDialog
import com.pedroapps.recuerdapp.data.MemoUI
import com.pedroapps.recuerdapp.utils.formatTime
import com.pedroapps.recuerdapp.utils.formatToStringDate
import com.pedroapps.recuerdapp.utils.getLocalDate
import com.pedroapps.recuerdapp.utils.getLocalTime
import com.pedroapps.recuerdapp.utils.getLocalTimeFromDateTimeMillis
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateMemoScreen(
    currentLanguageCode: String,
    paddingValues: PaddingValues,
    navController: NavHostController,
    saveAndScheduleMemo: (Int?, String, Long) -> Unit,
    memoToUpdate: MemoUI?
) {

    val context = LocalContext.current
    val initialTimeValue: String = getString( context, R.string.selected_time_initial_value )
    val initialDateValue: String = getString( context, R.string.selected_date_initial_value )

//    val initialTimeValue = "No selected time"
//    val initialDateValue = "No date selected"

    val selectedTime: LocalTime?  = memoToUpdate?.millis?.getLocalTimeFromDateTimeMillis()

    val memo = remember {
        mutableStateOf(memoToUpdate?.memo ?: "")
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
        },

        initialSelectedDateMillis = memoToUpdate?.millis
    )

    val timePickerState = rememberTimePickerState(
        is24Hour = false,
        initialHour = selectedTime?.hour ?: 0,
        initialMinute = selectedTime?.minute ?: 0
        )


    val formattedDate = remember {
        derivedStateOf {
            datePickerState.formatToStringDate(currentLanguageCode,
                initialValue = initialDateValue)
        }
    }

    val formattedTime = remember {
        if(timePickerState.hour != 0 && timePickerState.minute != 0) {
            mutableStateOf(timePickerState.formatTime())
        } else {
            mutableStateOf(initialTimeValue)
        }

    }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues = paddingValues)
    ) {
        Text(
            text = stringResource(id = R.string.new_memo_screen_title),
            fontSize = 24.sp,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(start = 12.dp, top = 12.dp)
        )

        OutlinedTextField(
            value = memo.value,
            onValueChange = { memo.value = it },
            label = { Text(text = stringResource(id = R.string.memo_textfield_label)) },
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
            label = { Text(text = stringResource(R.string.when_textfield_label)) },
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


        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp)
        ) {
            Button(

                onClick = {
                    performSaveAndScheduleMemo(
                        memo = memo.value,
                        memoId = memoToUpdate?.id,
                        timeState = timePickerState,
                        dateState = datePickerState,
                        saveAndScheduleMemo = saveAndScheduleMemo,
                    )
                },
                modifier = Modifier
                    .padding(end = 20.dp)
            ) {
                Text(text = stringResource(id = R.string.save_button))
            }

            Button(
                onClick = {
                    navController.popBackStack()
                }) {
                Text(text = stringResource(id = R.string.cancel_button))
            }
        }

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
                            formattedTime = formattedTime,
                            initialValue = initialTimeValue
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
    formattedTime: MutableState<String>,
    initialValue: String
) {
    openTimeDialog.value = false
    formattedTime.value = initialValue
}

@OptIn(ExperimentalMaterial3Api::class)
fun performSaveAndScheduleMemo(
    memo: String,
    memoId: Int? = null,
    timeState: TimePickerState,
    dateState: DatePickerState,
    saveAndScheduleMemo: (Int?, String, Long) -> Unit,
) {

    val localTime = timeState.getLocalTime()
    val localDate = dateState.getLocalDate() ?: return

    val localDateTime = LocalDateTime.of(localDate, localTime)
    val millis = localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    saveAndScheduleMemo(memoId, memo, millis)

}


@Preview(showBackground = true)
@Composable
fun CreateMemoScreenPreview() {
    val currentLanguageCode = "es"
    val paddingValues = PaddingValues()
    val navController = rememberNavController()

    val testMemo = MemoUI.getEmptyMemo()
    testMemo.memo = "Test memo to make sure this works correctly!"
    testMemo.millis = System.currentTimeMillis()

    CreateMemoScreen(
        currentLanguageCode = currentLanguageCode,
        paddingValues = paddingValues,
        navController = navController,
        saveAndScheduleMemo = {_, _, _ -> },
        memoToUpdate = testMemo
    )
}