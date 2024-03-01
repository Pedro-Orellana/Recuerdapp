package com.pedroapps.recuerdapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@Composable
fun MemoDetailsScreen(
    paddingValues: PaddingValues,
    memoUI: MemoUI
) {
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
            value = memoUI.memo,
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

    }
}


@Preview(showBackground = true)
@Composable
fun MemoDetailScreenPreview() {

    val paddingValues = PaddingValues()
    val emptyMemo = MemoUI.getEmptyMemo()

    emptyMemo.memo = "Something new"

    MemoDetailsScreen(
        paddingValues = paddingValues,
        memoUI = emptyMemo
    )

}