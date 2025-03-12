package com.jop.ngaji.presentation.detailSurah.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jop.ngaji.data.Resource
import com.jop.ngaji.data.local.store.DataStoreSetting
import com.jop.ngaji.data.model.LastReadSurah
import com.jop.ngaji.data.repo.SurahRepository
import com.jop.ngaji.presentation.detailSurah.view.DetailSurahScreenEvent
import com.jop.ngaji.presentation.detailSurah.view.DetailSurahScreenState
import com.jop.ngaji.util.ExoPlayerHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailSurahViewModel(private val repository: SurahRepository, private val exoPlayerHelper: ExoPlayerHelper, private val dataStore: DataStoreSetting): ViewModel() {
    private val _state = MutableStateFlow(DetailSurahScreenState())
    val state: StateFlow<DetailSurahScreenState> = _state

    init {
        viewModelScope.launch {
            exoPlayerHelper.stateCurrentIndex.collect{
                if(_state.value.audioIndex != it) _state.value = _state.value.copy(audioIndex = it)
            }
        }

        viewModelScope.launch {
            exoPlayerHelper.stateIsPlay.collect{
                _state.value = _state.value.copy(isAudioPlaying = it)
            }
        }

        viewModelScope.launch {
            dataStore.getLastReadSurah().collect {
                _state.value = _state.value.copy(lastReadSurah = it ?: LastReadSurah())
            }
        }

        exoPlayerHelper.pause()
        getSurah()
    }

    fun onEvent(event: DetailSurahScreenEvent){
        when(event){
            is DetailSurahScreenEvent.GetDetailSurah -> { getDetailSurah(event.surahNumber) }
            is DetailSurahScreenEvent.SetFavoriteSurah -> {
                viewModelScope.launch {
                    repository.updateFavoriteSurah(event.surahNumber, event.isFavorite)

                    var selectedSurah = _state.value.allSurah.find { it.id == event.surahNumber }
                    selectedSurah = selectedSurah!!.copy(favorite = event.isFavorite)

                    _state.value = _state.value.copy(
                        selectedSurah = selectedSurah
                    )
                }
            }
            is DetailSurahScreenEvent.ShowDetailAyahBottomSheet -> {
                _state.value = _state.value.copy(selectedAyah = event.selectedAyah)
            }
            is DetailSurahScreenEvent.SetLastReadSurah -> {
                viewModelScope.launch {
                    dataStore.setLastReadSurah(event.selectedAyah)
                }
            }
            is DetailSurahScreenEvent.OnPlayAudioStartFromAyah -> { exoPlayerHelper.playStartFrom(event.ayahIndex) }
            is DetailSurahScreenEvent.OnPlayAudio -> { exoPlayerHelper.play() }
            is DetailSurahScreenEvent.OnPauseAudio -> { exoPlayerHelper.pause() }
        }
    }

    private fun getSurah() = viewModelScope.launch {
        repository.allSurah().collect {
            when(it){
                is Resource.Loading -> {
                    _state.value = _state.value.copy(isAllSurahLoading = true)
                }
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        allSurah = it.data ?: mutableListOf(),
                        isAllSurahLoading = false
                    )
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(isAllSurahLoading = false)
                }
            }
        }
    }

    private fun getDetailSurah(surahNumber: Int) = viewModelScope.launch {
        try {
            repository.detailSurah(surahNumber).collect {
                when(it){
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(isDetailSurahLoading = true)
                    }
                    is Resource.Success -> {
                        var updateSurah = _state.value.allSurah.find { it.id == surahNumber }
                        updateSurah = updateSurah!!.copy(ayat = it.data!!.ayat)

                        val listSurah = _state.value.allSurah.toMutableList()
                        listSurah[surahNumber - 1] = updateSurah

                        _state.value = _state.value.copy(
                            allSurah = listSurah,
                            selectedSurah = it.data,
                            isDetailSurahLoading = false
                        )

                        exoPlayerHelper.addSurah(it.data)
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(errorMessage = "Terjadi Kesalahan", isDetailSurahLoading = false)
                    }
                }
            }
        } catch (e: Exception){
            println(e.message.toString())
            _state.value = _state.value.copy(errorMessage = "Terjadi Kesalahan", isDetailSurahLoading = false)
        }
    }
}