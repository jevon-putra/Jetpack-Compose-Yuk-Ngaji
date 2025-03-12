package com.jop.ngaji.data

import com.jop.ngaji.R
import com.jop.ngaji.ui.route.Route

sealed class BottomNavItem(val route: String, val iconSelect: Int, val iconUnselect: Int, val label: String) {
    object Home : BottomNavItem(Route.HOME, R.drawable.ic_prayer_fill, R.drawable.ic_prayer_outline, "Beranda")
    object Quran : BottomNavItem(Route.QURAN, R.drawable.ic_book_fill, R.drawable.ic_book_outline, "Quran")
}
