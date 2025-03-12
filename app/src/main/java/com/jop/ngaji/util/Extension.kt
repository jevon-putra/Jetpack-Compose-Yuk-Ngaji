package com.jop.ngaji.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import java.util.Date
import java.util.Locale

fun Modifier.shimmerBackground(isLoading: Boolean = true): Modifier = composed {
    var shimmer: Brush? = null
    val shimmerColor = MaterialTheme.colorScheme.outlineVariant
    val backgroundColor = MaterialTheme.colorScheme.onPrimary

    if(isLoading){
        val transition = rememberInfiniteTransition(label = "shimmer")

        val translateAnimation by transition.animateFloat(
            initialValue = 0f,
            targetValue = 400f,
            animationSpec = infiniteRepeatable(
                tween(durationMillis = 1500, easing = LinearOutSlowInEasing),
                RepeatMode.Restart
            ),
            label = "shimmer",
        )

        val shimmerColors = listOf(
            Color.Transparent,
            shimmerColor.copy(alpha = 0.5f),
        )

        shimmer = Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(translateAnimation, translateAnimation),
            end = Offset(translateAnimation + 85f, translateAnimation + 85f),
            tileMode = TileMode.Mirror,
        )
    }

    Modifier.drawWithContent {
        drawContent()
        if(shimmer != null){
            drawRect(color = backgroundColor)
            drawRect(brush = shimmer)
        }
    }
}

fun Activity.goToAppSetting() {
    val i = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    )
    startActivity(i)
}

fun String.getCountryCodeFromCountryName(): String {
    val locales = Locale.getAvailableLocales()
    for (locale in locales) {
        if (this.equals(locale.displayCountry, ignoreCase = true)) {
            return locale.country
        }
    }
    return "ID"
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settingPrefs")

fun Long.differenceResult(): Pair<Long, Long> {
    val differentTime = this - Date().time
    val value: Long = differentTime / 1000

    val hours: Long = value % (24 * 3600) / 3600
    val minute: Long = value % 3600 / 60

    return Pair(hours, minute)
}