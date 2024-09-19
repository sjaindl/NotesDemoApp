package com.sjaindl.notesdemoapp.ui.common

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp

@Composable
fun LoadingAnimation(
    modifier: Modifier = Modifier,
    animationDelay: Int = 1000
) {

    // circle's scale state
    var circleScale by remember {
        mutableFloatStateOf(0f)
    }

    // animation
    val circleScaleAnimate = animateFloatAsState(
        targetValue = circleScale,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = animationDelay
            )
        ),
        label = "loadingAnimation"
    )

    // This is called when the animation is launched
    LaunchedEffect(Unit) {
        circleScale = 1f
    }

    // animating circle
    Box(
        modifier = modifier
            .size(size = 64.dp)
            .scale(scale = circleScaleAnimate.value)
            .border(
                width = 4.dp,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 1 - circleScaleAnimate.value),
                shape = CircleShape
            )
    )
}
