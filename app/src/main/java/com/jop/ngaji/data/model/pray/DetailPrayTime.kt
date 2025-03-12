package com.jop.ngaji.data.model.pray

import com.jop.ngaji.R

data class DetailPrayTime(
    val prayName: String = "Subuh",
    val selectedIcon: Int = R.drawable.ic_subuh_fill,
    val unselectedIcon: Int = R.drawable.ic_subuh_outline,
    val time: String = "00:00"
)

