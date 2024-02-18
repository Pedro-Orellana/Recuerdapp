package com.pedroapps.recuerdapp.screens

import androidx.appcompat.app.AppCompatDelegate
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
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.LocaleListCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

//TODO(make it so that when selecting a new language, there is an animation on the main screen
// and there's no "hiccups" on the transition)
@Composable
fun SettingsScreen(
    paddingValues: PaddingValues,
    navController: NavHostController,
    currentLanguage: String
) {

    val selectedLanguage = remember {
        mutableStateOf(currentLanguage)
    }

    val showDialog = remember {
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
            text = "Settings Screen",
            fontSize = 24.sp,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(start = 12.dp, top = 12.dp)
        )

        HorizontalDivider()

        Spacer(
            modifier = Modifier
                .padding(20.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .padding(start = 20.dp, end = 20.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxSize()
                    .selectableGroup()
            ) {
                Text(
                    text = "Select your preferred language",
                    modifier = Modifier
                        .padding(start = 12.dp, top = 12.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(start = 12.dp, top = 12.dp)
                        .selectable(
                            selected = selectedLanguage.value == "es",
                            enabled = true,
                            role = Role.RadioButton,
                            onClick = { selectedLanguage.value = "es" }
                        )
                        .fillMaxWidth(0.3f)
                ) {
                    Text(text = "Spanish")
                    RadioButton(
                        selected = selectedLanguage.value == "es",
                        onClick = null
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(start = 12.dp, top = 12.dp, bottom = 12.dp)
                        .selectable(
                            selected = selectedLanguage.value == "en",
                            enabled = true,
                            role = Role.RadioButton,
                            onClick = { selectedLanguage.value = "en" }
                        )
                        .fillMaxWidth(0.3f)
                ) {
                    Text(text = "English")
                    RadioButton(
                        selected = selectedLanguage.value == "en",
                        onClick = null
                    )
                }
            }
        }


        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, end = 20.dp)
        ) {
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .padding(end = 20.dp)
            ) {
                Text(text = "Cancel")
            }

            Button(onClick = { showDialog.value = true }) {
                Text(text = "Save")
            }
        }


    }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text(text = "Are you sure you want to save changes?") },
            text = { Text(text = "Your app might have to restart so be aware of that") },
            confirmButton = {
                TextButton(onClick = {
                    saveSettingsChanges(
                        currentLanguage = currentLanguage,
                        selectedLanguage = selectedLanguage.value,
                        navController = navController
                    )
                }) {
                    Text(text = "Save changes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog.value = false }) {
                    Text(text = "Cancel")
                }
            }
        )
    }


}


fun saveSettingsChanges(
    currentLanguage: String,
    selectedLanguage: String,
    navController: NavHostController
) {
    if (currentLanguage != selectedLanguage) {
        val newLanguage = LocaleListCompat.forLanguageTags(selectedLanguage)
        AppCompatDelegate.setApplicationLocales(newLanguage)
        navController.popBackStack()
    }
}


@Composable
@Preview(showBackground = true)
fun SettingsScreenPreview() {
    val paddingValues = PaddingValues()
    SettingsScreen(
        paddingValues = paddingValues,
        currentLanguage = "es",
        navController = rememberNavController()
    )
}