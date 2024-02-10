package com.pedroapps.recuerdapp.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(RecuerdappState())
    val uiState = _uiState.asStateFlow()


    fun updateCurrentDestination(destination: String) {
        _uiState.update { currentState ->
            currentState.copy(currentDestination = destination)
        }
    }
}