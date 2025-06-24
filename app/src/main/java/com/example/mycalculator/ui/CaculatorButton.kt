package com.example.mycalculator.ui

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mycalculator.R
import com.example.mycalculator.ui.theme.MyCalculatorTheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CalculatorButton(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    val spaceMonoFamily = FontFamily(
        Font(R.font.space_mono_bold, FontWeight.Bold)
    )
    FilledIconButton(
        onClick = onClick,
        modifier = modifier
            .aspectRatio(if (text == "0") 2.1f else 1f)
            .height(80.dp),
        shapes = IconButtonDefaults.shapes(),
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = backgroundColor,
            contentColor = textColor
        )
    ) {
        Text(
            text = text,
            fontFamily = spaceMonoFamily,
            fontSize = 32.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0x000000)
@Composable
fun CalculatorButtonPreview() {
    MyCalculatorTheme(darkTheme = true) {
        CalculatorButton(
            text = "1",
            onClick = {}
        )
    }
}


