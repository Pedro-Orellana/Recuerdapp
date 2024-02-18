package com.pedroapps.recuerdapp.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.pedroapps.recuerdapp.screens.Destinations
import com.pedroapps.recuerdapp.ui.theme.myAccentColor
import com.pedroapps.recuerdapp.ui.theme.mySecondaryColor
import com.pedroapps.recuerdapp.ui.theme.secondSecondary
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavigationBar(
    navController: NavHostController,
    drawerState: DrawerState
) {

    val coroutineScope = rememberCoroutineScope()

    CenterAlignedTopAppBar(
        title = { Text(
            text = "Recuerdapp",
            color = myAccentColor
        ) },
        navigationIcon = {

            IconButton(
                onClick = {
                    coroutineScope.launch {
                        drawerState.apply {
                            if (isOpen) close() else open()
                        }
                    }

                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "menu button",
                    tint = myAccentColor
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = mySecondaryColor
        )
    )
}


@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    currentDestination: String,
    updateCurrentDestination: (String) -> Unit
) {

    BottomAppBar(
        tonalElevation = 12.dp,
        containerColor = mySecondaryColor
    ) {

        NavigationBarItem(
            selected = (currentDestination == Destinations.HomeScreen),
            onClick = {
                navController.navigate(Destinations.HomeScreen)
                updateCurrentDestination(Destinations.HomeScreen)
            },
            icon = { Icon(imageVector = Icons.Filled.Home, contentDescription = "Home screen") },
            label = { Text(text = "Home") }
        )

        NavigationBarItem(
            selected = (currentDestination == Destinations.SettingsScreen),
            onClick = {
                navController.navigate(Destinations.SettingsScreen)
                updateCurrentDestination(Destinations.SettingsScreen)
            },
            icon = { Icon(imageVector = Icons.Filled.Settings, contentDescription = "settings") },
            label = { Text(text = "Settings") }
        )

    }

}

@Composable
fun DrawerContent(
    drawerState: DrawerState,
    navController: NavHostController,
    currentDestination: String,
    updateCurrentDestination: (String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    ModalDrawerSheet {

        NavigationDrawerItem(
            label = { Text(text = "Log out") },
            selected = false,
            onClick = {
                println("Attempted to log out!")
                coroutineScope.launch {
                    drawerState.close()
                }
            })

        NavigationDrawerItem(
            label = { Text(text = "Test Screen") },
            selected = currentDestination == Destinations.TestScreen,
            onClick = {
                coroutineScope.launch {
                    navController.navigate(Destinations.TestScreen)
                    updateCurrentDestination(Destinations.TestScreen)
                    drawerState.apply {
                        if(isOpen) close() else open()
                    }
                }
            })
    }
}

