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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pedroapps.recuerdapp.R
import com.pedroapps.recuerdapp.data.MemoUI

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoDetailsScreen(
    paddingValues: PaddingValues,
    memoUI: MemoUI
) {

    val memoString = remember {
        mutableStateOf(memoUI.memo)
    }

    val memoMillis = remember {
        mutableLongStateOf(memoUI.millis)
    }

    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState()

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues = paddingValues)
    ) {
        Text(
            text = "Details",
            fontSize = 24.sp,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(start = 20.dp, top = 12.dp)
        )

        OutlinedTextField(
            value = memoString.value,
            onValueChange = { },
            label = { Text(text = stringResource(id = R.string.memo_textfield_label)) },
            minLines = 5,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp, top = 50.dp)
                .clip(shape = RoundedCornerShape(8.dp))
        )

        OutlinedTextField(
            value = "Something else",
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
                .clickable { }

        )


        OutlinedTextField(
            value = "Third something else",
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
                .clickable {}

        )


        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 12.dp, top = 20.dp)
        ) {
            OutlinedButton(onClick = { /*TODO*/ }) {
                Text(text = "Edit")
            }

            TextButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .padding(start = 12.dp)
            ) {
                Text(text = "Delete")
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun MemoDetailScreenPreview() {

    val paddingValues = PaddingValues()
    val emptyMemo = MemoUI.getEmptyMemo()

    emptyMemo.memo = "Please remember to have a great day today!"

    MemoDetailsScreen(
        paddingValues = paddingValues,
        memoUI = emptyMemo
    )

}