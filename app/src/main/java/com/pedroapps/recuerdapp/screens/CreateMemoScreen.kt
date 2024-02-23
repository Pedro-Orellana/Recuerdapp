package com.pedroapps.recuerdapp.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import com.pedroapps.recuerdapp.R
import com.pedroapps.recuerdapp.components.MyTimePickerDialog
import com.pedroapps.recuerdapp.utils.formatTime
import com.pedroapps.recuerdapp.utils.formatToStringDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateMemoScreen(
    paddingValues: PaddingValues,
    foregroundLocationLauncher: ActivityResultLauncher<Array<String>>
) {
    //TODO(check if the permissions are granted here, if not, launch the permission requester here,
    // when the screen first loads up)

    //TODO(these lines break the preview, when preview is no longer needed we can uncomment them)
    val context = LocalContext.current
//    val initialTimeValue: String = getString( context, R.string.selected_time_initial_value )
//    val initialDateValue: String = getString( context, R.string.selected_date_initial_value )

    val initialTimeValue = "No selected time"
    val initialDateValue = "No date selected"
    val initialPlaceValue = "No place selected"

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
            datePickerState.formatToStringDate("en", initialValue = initialDateValue)
        }
    }

    val formattedTime = remember {
        mutableStateOf(initialTimeValue)
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


        OutlinedTextField(
            value = "No place selected",
            onValueChange = {},
            label = { Text(text = "Where?") },
            enabled = false,
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp, top = 20.dp)
                .clickable {
                    openMap(context, foregroundLocationLauncher)
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


fun openMap(context: Context, permissionLauncher: ActivityResultLauncher<Array<String>>) {
    when {
        (isPermissionGranted(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) && isPermissionGranted(
            context,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )) -> {
            //TODO(both permissions are granted, so open the map here)
            Toast.makeText(context, "Opening map...", Toast.LENGTH_SHORT).show()
        }


        else -> {
            //TODO(permissions not granted, so request the permissions here)
            val permissions = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )

            permissionLauncher.launch(permissions)
        }
    }
}


fun isPermissionGranted(context: Context, permission: String): Boolean {
    return (ContextCompat.checkSelfPermission(
        context,
        permission
    ) == PackageManager.PERMISSION_GRANTED)

}


@Preview(showBackground = true)
@Composable
fun CreateMemoScreenPreview() {
    val paddingValues = PaddingValues()
    val permissionLauncher = object : ActivityResultLauncher<Array<String>>() {
        override fun launch(input: Array<String>?, options: ActivityOptionsCompat?) {
            TODO("Not yet implemented")
        }

        override fun unregister() {
            TODO("Not yet implemented")
        }

        override fun getContract(): ActivityResultContract<Array<String>, *> {
            TODO("Not yet implemented")
        }
    }

    CreateMemoScreen(paddingValues = paddingValues, foregroundLocationLauncher = permissionLauncher)
}