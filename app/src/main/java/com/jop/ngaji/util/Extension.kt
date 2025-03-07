package com.jop.ngaji.util

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