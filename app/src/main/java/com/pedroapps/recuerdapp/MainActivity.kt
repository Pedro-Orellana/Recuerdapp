package com.pedroapps.recuerdapp

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pedroapps.recuerdapp.components.BottomNavigationBar
import com.pedroapps.recuerdapp.components.DrawerContent
import com.pedroapps.recuerdapp.components.TopNavigationBar
import com.pedroapps.recuerdapp.data.database.RecuerdappDatabase
import com.pedroapps.recuerdapp.notifications.RecuerdappNotificationReceiver
import com.pedroapps.recuerdapp.screens.CreateMemoScreen
import com.pedroapps.recuerdapp.screens.Destinations
import com.pedroapps.recuerdapp.screens.HomeScreen
import com.pedroapps.recuerdapp.screens.MemoDetailsScreen
import com.pedroapps.recuerdapp.screens.SettingsScreen
import com.pedroapps.recuerdapp.screens.TestScreen
import com.pedroapps.recuerdapp.ui.theme.RecuerdappTheme
import com.pedroapps.recuerdapp.utils.MEMO_PENDING_INTENT_CODE
import com.pedroapps.recuerdapp.utils.MEMO_STRING_EXTRA
import com.pedroapps.recuerdapp.utils.NOTIFICATION_CHANNEL_ID
import com.pedroapps.recuerdapp.utils.NOTIFICATION_CHANNEL_NAME
import com.pedroapps.recuerdapp.viewmodels.MainViewModel
import kotlinx.coroutines.delay

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //create database
        val database = RecuerdappDatabase.getInstance(this)

        //create notification channel
        createRecuerdappNotificationChannel(this)

        //immediately ask for permission to receive notifications
        val notificationPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                //TODO(do something more useful here)
                val message =
                    if (isGranted) "Notifications are allowed" else "Please grant the permission to allow notifications"
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }

        //immediately ask for permission to set alarms
        val alarmSchedulePermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                //TODO(do something more useful here)
                val message =
                    if (isGranted) "Alarm schedule permissions are granted" else "Please allow to schedule alarms"
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            alarmSchedulePermissionLauncher.launch(Manifest.permission.SCHEDULE_EXACT_ALARM)

        val currentLanguage = AppCompatDelegate.getApplicationLocales().toLanguageTags()

        setContent {

            val factory =
                MainViewModel.Companion.MainViewModelFactory(
                    currentLanguage = currentLanguage,
                    database = database
                )
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
                            )
                        }

                    }

                }
            }

        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Container(
    viewModel: MainViewModel,
) {
    //dependencies
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val navController = rememberNavController()
    val appState = viewModel.uiState.collectAsState()

    val context = LocalContext.current

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
                        navController = navController,
                        savedMemos = appState.value.allMemos,
                        getAllSavedMemos = viewModel::getAllMemos
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
                        currentLanguageCode = appState.value.currentLanguage,
                        paddingValues = paddingValues,
                        navController = navController,

                        saveAndScheduleMemo = { memo, millis ->

                            //TODO(save the memo in the database)

//                            viewModel.saveMemo(
//                                memo = memo,
//                                millis = millis
//                            )

                            scheduleMemo(
                                memo = memo,
                                millis = millis,
                                context = context
                            )
                        }
                    )
                }

                composable(route = Destinations.MemoDetailsScreen) {
                    MemoDetailsScreen(paddingValues = paddingValues)
                }

                //TODO delete this when no longer needed
                composable(route = Destinations.TestScreen) {
                    TestScreen(
                        paddingValues = paddingValues,
                        showTestNotification = {
                            showTestNotification(context)
                        },
                        showNotificationTenSeconds = {
                            showTestNotificationInTenSeconds(context)
                        }
                    )
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


fun createRecuerdappNotificationChannel(context: Context) {
    val channel = NotificationChannel(
        NOTIFICATION_CHANNEL_ID,
        NOTIFICATION_CHANNEL_NAME,
        NotificationManager.IMPORTANCE_HIGH
    )
    val manager = context.getSystemService(NotificationManager::class.java)
    manager.createNotificationChannel(channel)
}


fun showTestNotification(context: Context) {

    with(NotificationManagerCompat.from(context)) {
        val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Remember to...")
            .setContentText("Remember to drink 8 glasses of water a day!")

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notify(1, builder.build())
    }
}


fun showTestNotificationInTenSeconds(context: Context) {

    val notificationIntent = Intent(context, RecuerdappNotificationReceiver::class.java)
    notificationIntent.putExtra(MEMO_STRING_EXTRA, "10 seconds have passed since you decided to put this notification!")
    val notificationPendingIntent = PendingIntent.getBroadcast(
        context,
        100,
        notificationIntent,
        PendingIntent.FLAG_IMMUTABLE
    )
    val time = System.currentTimeMillis() + 10_000

    val alarmManager = context.getSystemService(AlarmManager::class.java)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (alarmManager.canScheduleExactAlarms()) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, notificationPendingIntent)
        }
    }

}


fun scheduleMemo(
    memo: String,
    millis: Long,
    context: Context
) {

    val memoIntent = Intent(context, RecuerdappNotificationReceiver::class.java)
    memoIntent.putExtra(MEMO_STRING_EXTRA, memo)

    val pendingMemoIntent = PendingIntent.getBroadcast(
        context,
        MEMO_PENDING_INTENT_CODE,
        memoIntent,
        PendingIntent.FLAG_MUTABLE
    )

    val alarmManager = context.getSystemService(AlarmManager::class.java)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

        if (alarmManager.canScheduleExactAlarms()) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, millis, pendingMemoIntent)
        }

    }


}



