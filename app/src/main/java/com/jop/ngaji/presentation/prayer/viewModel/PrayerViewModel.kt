package com.jop.ngaji.presentation.prayer.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jop.ngaji.data.Resource
import com.jop.ngaji.data.repo.PrayerRepository
import com.jop.ngaji.presentation.prayer.view.PrayerScreenEvent
import com.jop.ngaji.presentation.prayer.view.PrayerScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PrayerViewModel(private val repository: PrayerRepository): ViewModel() {
    private val _state = MutableStateFlow(PrayerScreenState())
    val state: StateFlow<PrayerScreenState> = _state

    init {
        getAllPrayer()
    }

    private fun getAllPrayer() = viewModelScope.launch {
        try {
            repository.getAllPrayer().collect {
                when(it){
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(isLoading = true)
                    }
                    is Resource.Success -> {
                        _state.value = _state.value.copy(data = it.data ?: mutableListOf(), isLoading = false)
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(errorMessage = "Terjadi Kesalahan", isLoading = false)
                    }
                }
            }
        } catch (e: Exception){
            _state.value = _state.value.copy(errorMessage = "Terjadi Kesalahan", isLoading = false)
        }
    }

    fun onEvent(event: PrayerScreenEvent){
        when(event){
            is PrayerScreenEvent.ShowDetailPrayer -> {
                _state.value = _state.value.copy(selectedPrayer = event.prayer)
            }
        }
    }
}