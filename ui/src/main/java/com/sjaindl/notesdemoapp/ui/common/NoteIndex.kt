package com.sjaindl.notesdemoapp.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NoteIndex(
    modifier: Modifier = Modifier,
    number: Int,
    circleSize: Dp = 48.dp,
    borderStroke: BorderStroke = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
    backgroundColor: Color = Color.Transparent,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge
) {
    Surface(
        modifier = modifier.size(circleSize),
        shape = CircleShape,
        color = backgroundColor,
        border = borderStroke,
        contentColor = contentColor
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number.toString(),
                style = textStyle,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NumberInCircularBorderPreview() {
    MaterialTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            NoteIndex(number = 7)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NoteIndexPreview() {
    MaterialTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            NoteIndex(
                number = 99,
                circleSize = 60.dp,
                borderStroke = BorderStroke(4.dp, Color.Red),
                backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                textStyle = TextStyle(fontSize = 24.sp, color = MaterialTheme.colorScheme.onSecondaryContainer)
            )
        }
    }
}
