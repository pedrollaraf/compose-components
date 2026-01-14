package com.plfdev.compose_components.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.VicoScrollState

@Suppress("COMPOSE_APPLIER_CALL_MISMATCH")
@Composable
fun ChartScrollbar(
    scrollState: VicoScrollState,
    modifier: Modifier = Modifier,
    trackHeight: Dp = 3.dp,
    thumbHeight: Dp = 3.dp,
    minThumbWidth: Dp = 24.dp,
) {
    // Se maxValue == 0f, não tem scroll (nada pra mostrar)
    val max = scrollState.maxValue
    if (max <= 0f) return

    val progress = (scrollState.value / max).coerceIn(0f, 1f)

    BoxWithConstraints(modifier = modifier) {
        val wPx = constraints.maxWidth.toFloat()
        val density = LocalDensity.current

        val minThumbPx = with(density) { minThumbWidth.toPx() }
        val thumbPx = minThumbPx.coerceAtMost(wPx) // thumb fixo mínimo (simples e bonito)

        val xPx = (wPx - thumbPx) * progress

        // Track
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(trackHeight)
                .clip(RoundedCornerShape(999.dp))
                .background(Color(0x22000000))
        )

        // Thumb
        Box(
            modifier =Modifier
                .offset { IntOffset(xPx.toInt(), 0) }
                .width(with(density) { thumbPx.toDp() })
                .height(thumbHeight)
                .clip(RoundedCornerShape(999.dp))
                .background(Color(0x55000000))
        )
    }
}