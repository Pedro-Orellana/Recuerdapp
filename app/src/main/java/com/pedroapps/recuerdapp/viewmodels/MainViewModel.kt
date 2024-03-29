package com.pedroapps.recuerdapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pedroapps.recuerdapp.data.MemoDataRepository
import com.pedroapps.recuerdapp.data.MemoUI
import com.pedroapps.recuerdapp.data.database.MemoRoomEntity
import com.pedroapps.recuerdapp.data.database.RecuerdappDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    currentLanguage: String,
    database: RecuerdappDatabase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecuerdappState(currentLanguage = currentLanguage))
    val uiState = _uiState.asStateFlow()

    private val dataRepository = MemoDataRepository(database = database)


    //STATE FUNCTIONS

    fun updateCurrentDestination(destination: String) {
        _uiState.update { currentState ->
            currentState.copy(currentDestination = destination)
        }
    }


    fun updateCurrentLanguage(newLanguage: String) {
        if (uiState.value.currentLanguage != newLanguage) {
            _uiState.update { currentState ->
                currentState.copy(currentLanguage = newLanguage)
            }
        }
    }


    fun setMemoToUpdate(memo: MemoUI?) {
        _uiState.update { currentState ->
            currentState.copy(memoToUpdate = memo)
        }
    }



    //These functions only to be used from the database functions down below. That's why they are private
    private fun updateAllMemos(allMemos: List<MemoUI>) {
        _uiState.update { currentState ->
            currentState.copy(allMemos = allMemos)
        }
    }

    private fun updateCurrentMemo(memo: MemoUI) {
        _uiState.update { currentState ->
            currentState.copy(currentMemo = memo)
        }
    }



    //DATABASE FUNCTIONS

    fun saveNewMemo(pendingIntentID: Int, memo: String, millis: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            dataRepository.saveNewMemo(pendingIntentID, memo, millis)
        }
    }


    fun getAllMemos()  {
        viewModelScope.launch(Dispatchers.IO) {
            val allMemos = dataRepository.getAllMemos()
            updateAllMemos(allMemos = allMemos)
        }
    }


    fun getMemoByID(memoID: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val memo = dataRepository.getMemoById(memoID)
            updateCurrentMemo(memo)
        }
    }


//    fun updateMemo(memo: MemoUI) {
//        viewModelScope.launch(Dispatchers.IO) {
//            dataRepository.updateMemo(memo)
//        }
//    }

    fun updateMemo(memoID: Int, pendingIntentID: Int, memoString: String, memoMillis: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            dataRepository.updateMemo(memoID, pendingIntentID = pendingIntentID, memoString, memoMillis)
        }
    }


    fun deleteMemo(memo: MemoUI) {
        viewModelScope.launch(Dispatchers.IO) {
            dataRepository.deleteMemo(memo)
        }
    }




    companion object {

        class MainViewModelFactory(
            private val currentLanguage: String,
            private val database: RecuerdappDatabase
        ) : ViewModelProvider.NewInstanceFactory() {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MainViewModel(
                    currentLanguage = currentLanguage,
                    database = database
                ) as T
            }
        }
    }
}