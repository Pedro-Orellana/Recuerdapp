package com.pedroapps.recuerdapp.screens

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.pedroapps.recuerdapp.components.MemoCard
import com.pedroapps.recuerdapp.data.MemoUI
import com.pedroapps.recuerdapp.utils.ENGLISH_LANGUAGE_CODE

@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    navController: NavHostController,
    languageCode: String,
    savedMemos: List<MemoUI>,
    getAllSavedMemos: () -> Unit
) {

    LaunchedEffect(key1 = true) {
        //get the latest list of memos from database every time we open this screen
        getAllSavedMemos()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues = paddingValues)
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .scrollable(
                    orientation = Orientation.Vertical,
                    state = rememberScrollState()
                )
        ) {
            Text(
                text = "Home",
                fontSize = 24.sp,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 12.dp, top = 12.dp)
            )

            savedMemos.forEach {
                MemoCard(memo = it, languageCode = languageCode)
            }

            Button(
                onClick = { navController.navigate(Destinations.MemoDetailsScreen) }
            ) {
                Text(text = "Go to details")
            }
        }

        FloatingActionButton(
            onClick = { navController.navigate(Destinations.CreateMemoScreen) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
        ) {

            Icon(imageVector = Icons.Filled.Add, contentDescription = "Create new note")

        }

    }

}


@Preview(showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    val paddingValues = PaddingValues()
    val navController = rememberNavController()


    HomeScreen(
        paddingValues = paddingValues,
        navController = navController,
        languageCode = ENGLISH_LANGUAGE_CODE,
        savedMemos = listOf(MemoUI.getEmptyMemo()),
        getAllSavedMemos = {}
    )
}