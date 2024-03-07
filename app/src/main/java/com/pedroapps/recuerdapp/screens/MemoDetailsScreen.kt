package com.pedroapps.recuerdapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.pedroapps.recuerdapp.data.MemoUI

@Composable
fun MemoDetailsScreen(
    paddingValues: PaddingValues,
    memoID: Int?,
    memoUI: MemoUI,
    navController: NavHostController,
    getMemoByID: (Int) -> Unit,
    setMemoToUpdate: (MemoUI) -> Unit,
    deleteMemo: (MemoUI) -> Unit
) {

    LaunchedEffect(key1 = true) {
        println("memoID is $memoID")
        memoID?.apply(getMemoByID)
    }


    val showDeleteDialog = remember {
        mutableStateOf(false)
    }


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

        Card(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 12.dp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .padding(start = 20.dp, end = 20.dp, top = 50.dp)
        ) {

            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 20.dp)
            )
            {

                Text(
                    text = "Memo ID:",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp
                )
                Text(text = memoUI.id.toString())

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                )


                Text(
                    text = "Memo:",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp

                )
                Text(text = memoUI.memo)

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                )

                Text(
                    text = "remember at:",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp
                )
                Text(text = memoUI.millis.toString())


                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    OutlinedButton(
                        onClick = {
                            setMemoToUpdate(memoUI)
                            navController.navigate(Destinations.CreateMemoScreen)

                        }) {
                        Text(text = "Edit")

                    }

                    TextButton(
                        onClick = { showDeleteDialog.value = true },
                        modifier = Modifier
                            .padding(start = 12.dp)
                    ) {
                        Text(
                            text = "Delete",
                            color = Color.Red
                        )
                    }

                }

            }

        }

    }

    if (showDeleteDialog.value) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog.value = false },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "delete icon",
                    tint = Color.Red
                )
            },

            title = {
                Text(
                    text = "Are you sure you want to delete this memo?",
                    textAlign = TextAlign.Center
                )
            },

            text = {
                Text(
                    text = "If you delete this memo it will not be scheduled anymore, and you won't be able to recover it",
                    textAlign = TextAlign.Center
                )
            },

            confirmButton = {
                TextButton(onClick = {
                    deleteMemo(memoUI)
                    navController.popBackStack()
                    showDeleteDialog.value = false
                }) {
                    Text(
                        text = "Delete",
                        color = Color.Red
                    )
                }
            },


            dismissButton = {
                TextButton(onClick = { showDeleteDialog.value = false }) {
                    Text(text = "Cancel")
                }
            }
        )
    }


}


@Preview(showBackground = true)
@Composable
fun MemoDetailScreenPreview() {

    val paddingValues = PaddingValues()
    val emptyMemo = MemoUI.getEmptyMemo()
    val navController = rememberNavController()

    emptyMemo.memo = "Please remember to have a great day today!"
    emptyMemo.millis = System.currentTimeMillis()

    MemoDetailsScreen(
        paddingValues = paddingValues,
        memoID = 0,
        memoUI = emptyMemo,
        navController = navController,
        getMemoByID = { },
        setMemoToUpdate = { },
        deleteMemo = { }
    )

}