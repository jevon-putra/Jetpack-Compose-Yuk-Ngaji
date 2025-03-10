package com.jop.ngaji.util

import android.app.Application
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.jop.ngaji.data.model.Surah
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ExoPlayerHelper(private var application: Application) {
    lateinit var exoPlayer: ExoPlayer
    private val _stateCurrentIndex = MutableStateFlow(0)
    lateinit var surah: Surah
    val stateCurrentIndex: StateFlow<Int> = _stateCurrentIndex
    private val _stateIsPlay = MutableStateFlow(false)
    val stateIsPlay: StateFlow<Boolean> = _stateIsPlay

    init {
        prepareExoPlayer()
    }

    private fun prepareExoPlayer() {
        exoPlayer = ExoPlayer.Builder(application).build()
        exoPlayer.playWhenReady = true
        exoPlayer.repeatMode = ExoPlayer.REPEAT_MODE_OFF
        exoPlayer.addListener(object : Player.Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                _stateCurrentIndex.value = exoPlayer.currentMediaItemIndex
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                if(!isPlaying) pause()
            }
        })
    }

    fun play(){
        exoPlayer.prepare()
        exoPlayer.play()
        _stateIsPlay.value = true
    }

    fun pause(){
        exoPlayer.pause()
        _stateIsPlay.value = false
    }

    fun addSurah(surah: Surah) {
        this.surah = surah

        exoPlayer.clearMediaItems()
        val mediaItems = surah.ayat.map { MediaItem.fromUri(it.audio.x04) }
        exoPlayer.setMediaItems(mediaItems)
    }
}