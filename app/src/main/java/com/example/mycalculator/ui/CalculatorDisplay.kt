package com.example.mycalculator.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mycalculator.R
import com.example.mycalculator.ui.theme.MyCalculatorTheme


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CalculatorDisplay(
    currentInput: String,
    expression: String = ""
) {
    val scrollState = rememberScrollState()

    // Auto-scroll to the end when expression updates
    LaunchedEffect(currentInput) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }
    val spaceMonoFamily = FontFamily(
        Font(R.font.space_mono_bolditalic, FontWeight.Bold)
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(
                MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(
                    topStart = 48.dp,
                    topEnd = 16.dp,
                    bottomEnd = 48.dp,
                    bottomStart = 16.dp
                )
            )
            .padding(16.dp)
            .heightIn(min = 64.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Column(horizontalAlignment = Alignment.End) {
            if (expression.isNotEmpty()) {
                Text(
                    text = expression,
                    fontSize = 20.sp,
                    fontFamily = spaceMonoFamily,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    maxLines = 1,
                    softWrap = false
                )
            }
            Row(
                modifier = Modifier
                    .horizontalScroll(scrollState)
            ) {
                Text(
                    text = currentInput,
                    fontSize = 48.sp,
                    fontFamily = spaceMonoFamily,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    softWrap = false,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Preview(showBackground = true, backgroundColor = 0x0000)
@Composable
fun CalculatorDisplayPreview() {
    MyCalculatorTheme(darkTheme = true) {
        CalculatorDisplay(
            currentInput = "00"
        )
    }
}