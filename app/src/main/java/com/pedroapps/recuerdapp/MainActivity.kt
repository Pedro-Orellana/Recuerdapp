package com.pedroapps.recuerdapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pedroapps.recuerdapp.components.BottomNavigationBar
import com.pedroapps.recuerdapp.components.DrawerContent
import com.pedroapps.recuerdapp.components.TopNavigationBar
import com.pedroapps.recuerdapp.screens.CreateMemoScreen
import com.pedroapps.recuerdapp.screens.Destinations
import com.pedroapps.recuerdapp.screens.HomeScreen
import com.pedroapps.recuerdapp.screens.MemoDetailsScreen
import com.pedroapps.recuerdapp.screens.SettingsScreen
import com.pedroapps.recuerdapp.screens.TestScreen
import com.pedroapps.recuerdapp.ui.theme.RecuerdappTheme
import com.pedroapps.recuerdapp.viewmodels.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel: MainViewModel = viewModel()

            RecuerdappTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Container(
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun Container(
    viewModel: MainViewModel
) {
    //dependencies
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val navController = rememberNavController()
    val appState = viewModel.uiState.collectAsState()

    ModalNavigationDrawer(
        drawerContent = {

            DrawerContent(
                drawerState = drawerState,
                navController = navController,
                currentDestination = appState.value.currentDestination,
                updateCurrentDestination = viewModel::updateCurrentDestination
                )

        },
        drawerState = drawerState,

        ) {
        Scaffold(
            topBar = {
                TopNavigationBar(
                    navController = navController,
                    drawerState = drawerState
                )
            },
            bottomBar = {
                BottomNavigationBar(
                    navController = navController,
                    currentDestination = appState.value.currentDestination,
                    updateCurrentDestination = viewModel::updateCurrentDestination
                )
            },

            ) { paddingValues ->
            NavHost(navController = navController, startDestination = Destinations.HomeScreen) {
                composable(route = Destinations.HomeScreen) {
                    HomeScreen(
                        paddingValues = paddingValues,
                        navController = navController
                    )
                }

                composable(route = Destinations.SettingsScreen) {
                    SettingsScreen(paddingValues = paddingValues)
                }

                composable(route = Destinations.CreateMemoScreen) {
                    CreateMemoScreen(paddingValues = paddingValues)
                }

                composable(route = Destinations.MemoDetailsScreen) {
                    MemoDetailsScreen(paddingValues = paddingValues)
                }
                
                //TODO delete this when no longer needed
                composable(route = Destinations.TestScreen) {
                    TestScreen(paddingValues = paddingValues)
                }
            }
        }
    }
}
