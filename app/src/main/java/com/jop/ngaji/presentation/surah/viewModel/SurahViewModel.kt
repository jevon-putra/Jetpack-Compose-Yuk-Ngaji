package com.jop.ngaji.presentation.surah.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jop.ngaji.data.Resource
import com.jop.ngaji.data.repo.SurahRepository
import com.jop.ngaji.presentation.surah.view.SurahScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SurahViewModel(private val repository: SurahRepository): ViewModel() {
    private val _state = MutableStateFlow(SurahScreenState())
    val state: StateFlow<SurahScreenState> = _state

    init {
        getSurah()
    }

    private fun getSurah() = viewModelScope.launch {
        try {
            repository.allSurah().collect {
                when(it){
                    is Resource.Loading -> {
                        _state.value = SurahScreenState(isLoading = true)
                    }
                    is Resource.Success -> {
                        _state.value = SurahScreenState(data = it.data ?: mutableListOf())
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(errorMessage = "Terjadi Kesalahan")
                    }
                }
            }
        } catch (e: Exception){
            _state.value = _state.value.copy(errorMessage = "Terjadi Kesalahan")
            println("Error: ${e.message}")
        }
    }
}