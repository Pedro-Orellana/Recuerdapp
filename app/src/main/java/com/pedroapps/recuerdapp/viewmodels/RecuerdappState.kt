package com.pedroapps.recuerdapp.viewmodels

import com.pedroapps.recuerdapp.data.MemoUI
import com.pedroapps.recuerdapp.screens.Destinations

data class RecuerdappState(
    var currentDestination: String = Destinations.HomeScreen,
    var currentLanguage: String,
    var allMemos: List<MemoUI> = listOf(),
    var currentMemo: MemoUI = MemoUI.getEmptyMemo(),
    var memoToUpdate: MemoUI? = null
)
