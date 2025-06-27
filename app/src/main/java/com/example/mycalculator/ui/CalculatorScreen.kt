package com.example.mycalculator.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mycalculator.CalculatorOperation
import com.example.mycalculator.CalculatorViewModel
import com.example.mycalculator.data.HistoryDao
import com.example.mycalculator.data.HistoryItem
import com.example.mycalculator.ui.theme.MyCalculatorTheme
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel = viewModel() // Inject ViewModel
) {

    val infiniteTransition = rememberInfiniteTransition(label = "rotation")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotateAngle"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            // Use system-level padding for edge-to-edge, not manual padding here
            .background(MaterialTheme.colorScheme.surface),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.TopCenter

        ) {
            Text(
                text = "Hello!",
                fontSize = 168.sp,
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Black,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Clip,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f) // Example: blue color, change as needed
            )

        }
        Spacer(modifier = Modifier.height(16.dp))
        // Display Area
        CalculatorDisplay(
            currentInput = viewModel.currentInput,
            expression = viewModel.expression
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Calculator Buttons
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp), // Add horizontal padding here
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Row 1: AC, +/-, %, /
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalculatorButton(
                    text = "AC",
                    modifier = Modifier.weight(1f),
                    backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                    textColor = MaterialTheme.colorScheme.onSecondaryContainer
                ) { viewModel.onClearClick() }
                CalculatorButton(
                    text = "<-",
                    modifier = Modifier.weight(1f),
                    backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                    textColor = MaterialTheme.colorScheme.onSecondaryContainer
                ) { viewModel.onBackspaceClick() }
                CalculatorButton(
                    text = "%",
                    modifier = Modifier.weight(1f),
                    backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                    textColor = MaterialTheme.colorScheme.onSecondaryContainer
                ) { viewModel.onPercentageClick() }
                CalculatorButton(
                    text = "÷",
                    modifier = Modifier.weight(1f),
                    backgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
                    textColor = MaterialTheme.colorScheme.onTertiaryContainer
                ) { viewModel.onOperatorClick(CalculatorOperation.DIVIDE) }
            }

            // Row 2: 7, 8, 9, x
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalculatorButton(text = "7", modifier = Modifier.weight(1f)) { viewModel.onNumberClick("7") }
                CalculatorButton(text = "8", modifier = Modifier.weight(1f)) { viewModel.onNumberClick("8") }
                CalculatorButton(text = "9", modifier = Modifier.weight(1f)) { viewModel.onNumberClick("9") }
                CalculatorButton(
                    text = "x",
                    modifier = Modifier.weight(1f),
                    backgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
                    textColor = MaterialTheme.colorScheme.onTertiaryContainer
                ) { viewModel.onOperatorClick(CalculatorOperation.MULTIPLY) }
            }

            // Row 3: 4, 5, 6, -
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalculatorButton(text = "4", modifier = Modifier.weight(1f)) { viewModel.onNumberClick("4") }
                CalculatorButton(text = "5", modifier = Modifier.weight(1f)) { viewModel.onNumberClick("5") }
                CalculatorButton(text = "6", modifier = Modifier.weight(1f)) { viewModel.onNumberClick("6") }
                CalculatorButton(
                    text = "-",
                    modifier = Modifier.weight(1f),
                    backgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
                    textColor = MaterialTheme.colorScheme.onTertiaryContainer
                ) { viewModel.onOperatorClick(CalculatorOperation.SUBTRACT) }
            }

            // Row 4: 1, 2, 3, +
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalculatorButton(text = "1", modifier = Modifier.weight(1f)) { viewModel.onNumberClick("1") }
                CalculatorButton(text = "2", modifier = Modifier.weight(1f)) { viewModel.onNumberClick("2") }
                CalculatorButton(text = "3", modifier = Modifier.weight(1f)) { viewModel.onNumberClick("3") }
                CalculatorButton(
                    text = "+",
                    modifier = Modifier.weight(1f),
                    backgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
                    textColor = MaterialTheme.colorScheme.onTertiaryContainer
                ) { viewModel.onOperatorClick(CalculatorOperation.ADD) }
            }

            // Row 5: 0, ., =
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalculatorButton(text = "0", modifier = Modifier.weight(2.1f)) { viewModel.onNumberClick("0") }
                CalculatorButton(text = ".", modifier = Modifier.weight(1f)) { viewModel.onDecimalClick() }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .width(85.dp)
                        .scale(1.2f)
                        .absoluteOffset(x = 4.dp, y = 4.dp)
                        .aspectRatio(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .graphicsLayer(
                                rotationZ = angle
                            )
                            .clip(MaterialShapes.Cookie6Sided.toShape())
                            .background(MaterialTheme.colorScheme.primary)
                    )
                    CalculatorButton(
                        text = "=",
                        modifier = Modifier,
                        backgroundColor = Color.Transparent,
                        textColor = MaterialTheme.colorScheme.onPrimary
                    ) {
                        viewModel.onEqualsClick()
                    }
                }
            }
            // Add a spacer at the bottom to account for navigation bar if using edge-to-edge
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_7_PRO, apiLevel = 35)
@Composable
fun CalculatorScreenPreview() {
    MyCalculatorTheme(darkTheme = true) {
        CalculatorScreen(
            viewModel = FakeCalculatorViewModel()
        )
    }
}

// Create a fake ViewModel just for preview
private class FakeCalculatorViewModel : CalculatorViewModel(FakeHistoryDao()) {
    // Override if needed
}

private class FakeHistoryDao : HistoryDao {
    // Implement required methods with preview data
    override suspend fun insert(historyItem: HistoryItem) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteById(id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun clearAll() {
        TODO("Not yet implemented")
    }

    override fun getAll(): Flow<List<HistoryItem>> {
        TODO("Not yet implemented")
    }
}
