package com.example.mycalculator.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    expression: String,
    modifier: Modifier
) {
    val scrollState = rememberScrollState()
    var showExpression by remember { mutableStateOf(false) }
    val extraHeight by animateFloatAsState(
        targetValue = if (expression.isNotEmpty()) 1f else 0f,
        animationSpec = spring(
            dampingRatio = 0.6f,
            stiffness = Spring.StiffnessMedium
        ),
        finishedListener = { value ->
            showExpression = value == 1f && expression.isNotEmpty()
        },
        label = "height"
    )
    LaunchedEffect(expression) {
        if (expression.isEmpty()) showExpression = false
    }


    // Auto-scroll to the end when expression updates
    LaunchedEffect(currentInput) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }
    val spaceMonoFamily = FontFamily(
        Font(R.font.space_mono_bolditalic, FontWeight.Bold)
    )
    Box(
        modifier = modifier
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
            .height(64.dp + (28.dp * extraHeight))
        ,
        contentAlignment = Alignment.BottomEnd,
    ) {
        if (showExpression) {
            Row(
                modifier = Modifier
                    .horizontalScroll(scrollState)
                    .align(Alignment.TopEnd)
            ) {
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
        }
            Row(
                modifier = Modifier
                    .horizontalScroll(scrollState)
                    .height(64.dp)
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

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Preview(showBackground = true, backgroundColor = 0x0000)
@Composable
fun CalculatorDisplayPreview() {
    MyCalculatorTheme(darkTheme = true) {
        CalculatorDisplay(
            currentInput = "00",
            expression = "0 + 6",
            modifier = Modifier,
        )
    }
}
@Preview(showBackground = true, backgroundColor = 0x0000)
@Composable
fun CalculatorDisplay2Preview() {
    MyCalculatorTheme(darkTheme = true) {
        CalculatorDisplay(
            currentInput = "00",
            expression = "",
            modifier = Modifier,
        )
    }
}