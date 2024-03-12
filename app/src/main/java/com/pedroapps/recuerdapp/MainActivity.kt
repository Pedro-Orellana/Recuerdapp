package com.pedroapps.recuerdapp

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pedroapps.recuerdapp.components.BottomNavigationBar
import com.pedroapps.recuerdapp.components.DrawerContent
import com.pedroapps.recuerdapp.components.TopNavigationBar
import com.pedroapps.recuerdapp.data.MemoUI
import com.pedroapps.recuerdapp.data.database.RecuerdappDatabase
import com.pedroapps.recuerdapp.notifications.RecuerdappNotificationReceiver
import com.pedroapps.recuerdapp.screens.CreateMemoScreen
import com.pedroapps.recuerdapp.screens.Destinations
import com.pedroapps.recuerdapp.screens.HomeScreen
import com.pedroapps.recuerdapp.screens.MemoDetailsScreen
import com.pedroapps.recuerdapp.screens.SettingsScreen
import com.pedroapps.recuerdapp.screens.TestScreen
import com.pedroapps.recuerdapp.ui.theme.RecuerdappTheme
import com.pedroapps.recuerdapp.utils.MEMO_ID_EXTRA
import com.pedroapps.recuerdapp.utils.MEMO_STRING_EXTRA
import com.pedroapps.recuerdapp.utils.NOTIFICATION_CHANNEL_ID
import com.pedroapps.recuerdapp.utils.NOTIFICATION_CHANNEL_NAME
import com.pedroapps.recuerdapp.viewmodels.MainViewModel
import kotlinx.coroutines.delay
import kotlin.random.Random

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //create database
        val database = RecuerdappDatabase.getInstance(this)

        //create notification channel
        createRecuerdappNotificationChannel(this)

        val currentLanguage = AppCompatDelegate.getApplicationLocales().toLanguageTags()

        setContent {

            val (showNotificationDialog, setShowNotificationDialog) = remember {
                mutableStateOf(false)
            }


            val notificationPermissionLauncher =
                rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted ->
                    setShowNotificationDialog(!isGranted)
                }


            val factory =
                MainViewModel.Companion.MainViewModelFactory(
                    currentLanguage = currentLanguage,
                    database = database
                )
            val viewModel: MainViewModel = viewModel(factory = factory)

            var appStarting by remember {
                mutableStateOf(true)
            }

            LaunchedEffect(key1 = true) {
                if (viewModel.uiState.value.currentLanguage != currentLanguage) {
                    viewModel.updateCurrentLanguage(currentLanguage)
                }
            }

            LaunchedEffect(key1 = true) {
                delay(1000)
                appStarting = false
            }

            LaunchedEffect(key1 = notificationPermissionLauncher) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
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
                                showNotificationDialog = showNotificationDialog,
                                setShowNotificationDialog = setShowNotificationDialog
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
    showNotificationDialog: Boolean,
    setShowNotificationDialog: (Boolean) -> Unit
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
                        languageCode = appState.value.currentLanguage,
                        savedMemos = appState.value.allMemos,
                        getAllSavedMemos = viewModel::getAllMemos,
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
                        memoToUpdate = appState.value.memoToUpdate,
                        saveAndScheduleMemo = { memo, millis ->

                            createNewMemoAction(
                                memo = memo,
                                millis = millis,
                                context = context,
                                navController = navController,
                                saveNewMemo = viewModel::saveNewMemo,
                                setMemoToUpdate = viewModel::setMemoToUpdate
                            )

                        },

                        scheduleUpdatedMemo = { memoToCancel, newMemoString, newMemoMillis ->


                            scheduleUpdatedMemoAction(
                                memoToCancel = memoToCancel,
                                newMemoString = newMemoString,
                                newMemoMillis = newMemoMillis,
                                context = context,
                                navController = navController,
                                updateMemo = viewModel::updateMemo,
                                setMemoToUpdate = viewModel::setMemoToUpdate
                            )

                        },

                        )
                }

                composable(
                    route = "${Destinations.MemoDetailsScreen}/{memoID}",
                    arguments = listOf(navArgument("memoID") { type = NavType.IntType })
                ) {

                    MemoDetailsScreen(
                        paddingValues = paddingValues,
                        memoUI = appState.value.currentMemo,
                        memoID = it.arguments?.getInt("memoID"),
                        navController = navController,
                        setMemoToUpdate = viewModel::setMemoToUpdate,
                        getMemoByID = viewModel::getMemoByID,
                        deleteMemo = viewModel::deleteMemo
                    )
                }

                //TODO delete this when no longer needed
                composable(route = Destinations.TestScreen) {
                    TestScreen(
                        paddingValues = paddingValues,
                        showTestNotification = {},
                        showNotificationTenSeconds = {
                            showTestNotificationInTenSeconds(context)
                        }
                    )
                }
            }
        }
    }

    if (showNotificationDialog) {
        NotificationPermissionDialog(
            onDismiss = { setShowNotificationDialog(false) }
        )
    }

}


@Composable
fun RecuerdappLogo() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(
                id = R.drawable.recuerdapp_logo
            ),
            contentDescription = "Recuerdapp logo"
        )

        Text(
            text = "Recuerdapp",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            textAlign = TextAlign.Center,
        )
    }
}


@Composable
fun NotificationPermissionDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(text = "OK")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(text = "Cancel")
            }
        },
        title = { Text(text = "Notification permission is not granted") },
        text = { Text(text = "To better use this app, please go to the app's settings and grant permission for notifications") }
    )
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


fun showTestNotificationInTenSeconds(context: Context) {

    val notificationIntent = Intent(context, RecuerdappNotificationReceiver::class.java)
    notificationIntent.putExtra(
        MEMO_STRING_EXTRA,
        "10 seconds have passed since you decided to put this notification!"
    )
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
    pendingIntentID: Int,
    memo: String,
    millis: Long,
    context: Context
) {

    val memoIntent = Intent(context, RecuerdappNotificationReceiver::class.java)
    memoIntent.setAction("My intent action")
    memoIntent.addCategory("My intent category")
    memoIntent.setDataAndType(Uri.EMPTY, "NO TYPE")
    memoIntent.putExtra(MEMO_STRING_EXTRA, memo)
    memoIntent.putExtra(MEMO_ID_EXTRA, pendingIntentID)

    println("When creating the memo:")
    println("pendingIntentCode: $pendingIntentID")

    val pendingMemoIntent = PendingIntent.getBroadcast(
        context,
        pendingIntentID,
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


fun scheduleUpdatedMemo(
    memoToCancel: MemoUI,
    newMemoString: String,
    newMemoMillis: Long,
    context: Context
) {

    val alarmManager = context.getSystemService(AlarmManager::class.java)

    val pendingIntentCode = memoToCancel.pendingIntentID

    val intentToCancel = Intent(context, RecuerdappNotificationReceiver::class.java)
    intentToCancel.setAction("My intent action")
    intentToCancel.addCategory("My intent category")
    intentToCancel.setDataAndType(Uri.EMPTY, "NO TYPE")
    intentToCancel.putExtra(MEMO_STRING_EXTRA, memoToCancel.memo)
    intentToCancel.putExtra(MEMO_ID_EXTRA, pendingIntentCode)

    val updatedMemoIntent = Intent(context, RecuerdappNotificationReceiver::class.java)
    updatedMemoIntent.putExtra(MEMO_STRING_EXTRA, newMemoString)
    updatedMemoIntent.putExtra(MEMO_ID_EXTRA, pendingIntentCode)

    val pendingIntentToCancel = PendingIntent.getBroadcast(
        context,
        pendingIntentCode,
        intentToCancel,
        PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_MUTABLE
    )

    alarmManager.cancel(pendingIntentToCancel)

    val pendingMemoIntent = PendingIntent.getBroadcast(
        context,
        pendingIntentCode,
        updatedMemoIntent,
        PendingIntent.FLAG_MUTABLE
    )


    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (alarmManager.canScheduleExactAlarms()) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, newMemoMillis, pendingMemoIntent)
        }

    }

}


private fun createNewMemoAction(
    memo: String,
    millis: Long,
    context: Context,
    navController: NavHostController,
    saveNewMemo: (Int, String, Long) -> Unit,
    setMemoToUpdate: (MemoUI?) -> Unit,
) {

    val pendingIntentID = Random(System.currentTimeMillis()).nextInt()

    saveNewMemo(
        pendingIntentID,
        memo,
        millis
    )

    setMemoToUpdate(null)

    scheduleMemo(
        pendingIntentID = pendingIntentID,
        memo = memo,
        millis = millis,
        context = context
    )

    navController.popBackStack()
}


private fun scheduleUpdatedMemoAction(
    memoToCancel: MemoUI,
    newMemoString: String,
    newMemoMillis: Long,
    context: Context,
    navController: NavHostController,
    updateMemo: (Int, Int, String, Long) -> Unit,
    setMemoToUpdate: (MemoUI?) -> Unit

) {

    updateMemo(
        memoToCancel.id,
        memoToCancel.pendingIntentID,
        newMemoString,
        newMemoMillis
    )

    setMemoToUpdate(null)

    scheduleUpdatedMemo(
        memoToCancel = memoToCancel,
        newMemoString = newMemoString,
        newMemoMillis = newMemoMillis,
        context = context
    )

    navController.popBackStack()

}

