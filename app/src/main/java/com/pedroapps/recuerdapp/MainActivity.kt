package com.pedroapps.recuerdapp

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
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
import kotlinx.coroutines.delay

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val currentLanguage = AppCompatDelegate.getApplicationLocales().toLanguageTags()


        val backgroundPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { permissionIsGranted ->
                val message = if (permissionIsGranted) {
                    "All permissions granted!"
                } else "You did not grant all permissions..."

                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }


        val activityLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                    backgroundPermissionLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            }
        }




        setContent {

            val factory =
                MainViewModel.Companion.MainViewModelFactory(currentLanguage = currentLanguage)
            val viewModel: MainViewModel = viewModel(factory = factory)

            var appStarting by remember {
                mutableStateOf(true)
            }

            //TODO(consider creating the state as a variable here, and then just passing it down as a parameter
            // in the Container() composable)
            LaunchedEffect(key1 = true) {
                if (viewModel.uiState.value.currentLanguage != currentLanguage) {
                    viewModel.updateCurrentLanguage(currentLanguage)
                }
            }

            LaunchedEffect(key1 = true) {
                delay(2000)
                appStarting = false
            }

            RecuerdappTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AnimatedContent(
                        targetState = appStarting,
                        label = "main content",

                        ) { isAppStarting ->
                        if (isAppStarting) {
                            RecuerdappLogo()
                        } else {
                            Container(
                                viewModel = viewModel,
                                foregroundLocationLauncher = activityLauncher
                            )
                        }

                    }

                }
            }

        }
    }


}

@Composable
fun Container(
    viewModel: MainViewModel,
    foregroundLocationLauncher: ActivityResultLauncher<Array<String>>
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
                    SettingsScreen(
                        paddingValues = paddingValues,
                        navController = navController,
                        currentLanguage = appState.value.currentLanguage
                    )
                }

                composable(route = Destinations.CreateMemoScreen) {
                    CreateMemoScreen(
                        paddingValues = paddingValues,
                        foregroundLocationLauncher = foregroundLocationLauncher
                    )
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


@Composable
fun RecuerdappLogo() {
    //TODO(finish this logo)
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = "RECUERDAPP LOGO",
            textAlign = TextAlign.Center,
            fontSize = 28.sp
        )
    }
}

