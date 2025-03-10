package com.jop.ngaji.presentation.home.view

sealed interface HomeScreenEvent {
    data class GetPrayTime(val defaultLocation: Boolean): HomeScreenEvent
}