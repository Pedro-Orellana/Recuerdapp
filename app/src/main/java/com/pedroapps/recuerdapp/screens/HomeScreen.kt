package com.pedroapps.recuerdapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    navController: NavHostController
) {


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues = paddingValues)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
            //    .padding(paddingValues = paddingValues)
        ) {
            Text(text = "Main Screen content")
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

            Icon(imageVector = Icons.Filled.Add, contentDescription = "Create new note" )

        }

    }

}