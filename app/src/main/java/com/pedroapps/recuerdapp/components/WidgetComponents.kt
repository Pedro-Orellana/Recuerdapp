package com.pedroapps.recuerdapp.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.pedroapps.recuerdapp.data.MemoUI
import com.pedroapps.recuerdapp.utils.ENGLISH_LANGUAGE_CODE


@Composable
fun MyTimePickerDialog(
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.LightGray
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                content()
            }

        }

    }
}


@Composable
fun MemoCard(
    memo: MemoUI,
    languageCode: String,
    showDetails: () -> Unit) {
    //TODO call function here to get scheduled time and date
    val scheduledDate = MemoUI.getFormattedDateFromMillis(memo.millis, languageCode )
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 12.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .padding(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 12.dp)
            .clickable { showDetails() }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 12.dp, bottom = 12.dp)
        ) {
            Text(
                text = memo.memo,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                fontStyle = FontStyle.Italic,
                fontFamily = FontFamily.Monospace,
                fontSize = 18.sp,
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                )

            Spacer(modifier = Modifier.padding(12.dp))

            Text(
                text = "scheduled at:",
                fontWeight = FontWeight.Light
                )
            Text(text = scheduledDate)
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun MemoCardPreview() {

    val memo = MemoUI(
        id = 1,
        memo = "Remember to do your best at work today!",
        millis = System.currentTimeMillis()
    )

    MemoCard(memo = memo, ENGLISH_LANGUAGE_CODE){}
}