package com.jop.ngaji.presentation.surahFavorite.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jop.ngaji.data.Resource
import com.jop.ngaji.data.repo.SurahRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SurahFavoriteViewModel(private val repository: SurahRepository): ViewModel() {
    fun getAllSurahFavorite() = repository.getAllSurahFavorite()
}