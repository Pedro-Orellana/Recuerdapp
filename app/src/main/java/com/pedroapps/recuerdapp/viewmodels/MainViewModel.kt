package com.pedroapps.recuerdapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel(
    currentLanguage: String
): ViewModel() {

    private val _uiState = MutableStateFlow(RecuerdappState(currentLanguage = currentLanguage))
    val uiState = _uiState.asStateFlow()


    fun updateCurrentDestination(destination: String) {
        _uiState.update { currentState ->
            currentState.copy(currentDestination = destination)
        }
    }


    fun updateCurrentLanguage(newLanguage: String) {
        if(uiState.value.currentLanguage != newLanguage) {
            _uiState.update { currentState ->
                currentState.copy(currentLanguage = newLanguage)
            }
        }
    }


    companion object {

        class MainViewModelFactory(
            private val currentLanguage: String
        ): ViewModelProvider.NewInstanceFactory() {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MainViewModel(
                    currentLanguage = currentLanguage
                ) as T
            }
        }
    }
}