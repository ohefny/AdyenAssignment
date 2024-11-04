package com.adyen.android.assignment.core.presentation.ui


import android.app.UiModeManager
import android.content.Context
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize

fun Modifier.shimmerEffect(): Modifier = composed {
    val context = LocalContext.current
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val transition = rememberInfiniteTransition(label = "shimmer")
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(2000)
        ), label = "shimmerStart"
    )
    val colors = if (context.isDarkTheme()) {
        listOf(
            Color(0xFFABAAAA),
            Color(0xFF807F7F),
            Color(0xFFABAAAA),
        )
    } else
        listOf(
            Color(0xFFEBEBEB),
            Color(0xFFF7F7F7),
            Color(0xFFEBEBEB),
        )


    background(
        brush = Brush.linearGradient(
            colors = colors,
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    )
        .onGloballyPositioned {
            size = it.size
        }
}


fun Context.isDarkTheme(): Boolean {
    val uiModeManager = this.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
    return when (uiModeManager.nightMode) {
        UiModeManager.MODE_NIGHT_YES ->
            true

        else ->
            false
    }
}