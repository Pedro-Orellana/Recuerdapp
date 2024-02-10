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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavigationBar(
    navController: NavHostController,
    drawerState: DrawerState
) {

    //TODO(change colors of top bar)

    val coroutineScope = rememberCoroutineScope()

    CenterAlignedTopAppBar(
        title = { Text(text = "Recuerdapp") },
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
                Icon(imageVector = Icons.Filled.Menu, contentDescription = "menu button")
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Cyan
        )
    )
}


@Composable
fun BottomNavigationBar(
    navController: NavHostController
) {
    println(navController.currentDestination?.route)

    val currentSelected = remember {
        mutableStateOf(navController.currentDestination?.route ?: Destinations.HomeScreen)
    }

    //TODO make it so that the current destination of the navcontroller
    //is saved in the state, and that is used to determine if navigation items are selected

    BottomAppBar(
        tonalElevation = 12.dp,
        //TODO( change the color of the bottom bar)
        containerColor = Color.Green
    ) {

        NavigationBarItem(
            selected = (currentSelected.value == Destinations.HomeScreen),
            onClick = {
                navController.navigate(Destinations.HomeScreen)
                currentSelected.value = Destinations.HomeScreen
            },
            icon = { Icon(imageVector = Icons.Filled.Home, contentDescription = "Home screen") },
            label = { Text(text = "Home") }
        )


        NavigationBarItem(
            selected = (currentSelected.value == Destinations.CreateMemoScreen),
            onClick = {
                navController.navigate(Destinations.CreateMemoScreen)
                currentSelected.value = Destinations.CreateMemoScreen
            },
            icon = {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = "Create memo"
                )
            },
            label = { Text(text = "Create") }
        )

        NavigationBarItem(
            selected = (currentSelected.value == Destinations.SettingsScreen),
            onClick = {
                navController.navigate(Destinations.SettingsScreen)
                currentSelected.value = Destinations.SettingsScreen
            },
            icon = { Icon(imageVector = Icons.Filled.Settings, contentDescription = "settings") },
            label = { Text(text = "Settings") }
        )

    }

}

@Composable
fun DrawerContent(
    drawerState: DrawerState,
    navController: NavHostController
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
            selected = false,
            onClick = {
                coroutineScope.launch {
                    navController.navigate(Destinations.TestScreen)
                    drawerState.apply {
                        if(isOpen) close() else open()
                    }
                }
            })
    }
}

