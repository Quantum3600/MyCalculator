package com.example.mycalculator.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.filledTonalButtonColors
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mycalculator.R
import com.example.mycalculator.data.HistoryItem
import com.example.mycalculator.ui.theme.MyCalculatorTheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HistorySection(
    historyItems: List<HistoryItem>,
    onDelete: (HistoryItem) -> Unit,
    onRestore: (HistoryItem) -> Unit,
    onClearHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    val spaceMonoFamily = FontFamily(
        Font(R.font.space_mono_bold, FontWeight.Bold)
    )
    val collapsedHeight = 265.dp
    val expandedHeightInPixels = LocalWindowInfo.current.containerSize.height
    val expandedHeight = with(LocalDensity.current) { expandedHeightInPixels.toDp() }


    // State for expanded/collapsed and drag offset
    var expanded by remember { mutableStateOf(false) }
    var dragOffset by remember { mutableFloatStateOf(0f) }
    val density = LocalDensity.current
    val minHeightPx = with(density) { collapsedHeight.toPx() }
    val maxHeightPx = with(density) { expandedHeight.toPx() }

    val targetHeight = if (expanded) expandedHeight else collapsedHeight
    val animatedHeight by animateDpAsState(targetValue = targetHeight + with(LocalDensity.current) { dragOffset.toDp() })
    Column(
        modifier = modifier
            .fillMaxWidth()
            .safeContentPadding()
            .height(animatedHeight)
            .padding(bottom = 20.dp)
            .background(
                MaterialTheme.colorScheme.surfaceContainerLowest,
                shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
            )
            .draggable(
                orientation = Orientation.Vertical,
                state = rememberDraggableState { delta ->
                    dragOffset += delta
                },
                onDragStopped = {
                    val newHeight = (if (expanded) maxHeightPx else minHeightPx) + dragOffset
                    expanded = newHeight > (minHeightPx + maxHeightPx) / 2
                    dragOffset = 0f
                }
            ),
        verticalArrangement = Arrangement.Bottom
    ) {
        if (!expanded) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = true)
                    .padding(16.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Pull down to view history",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.displayLarge.copy(fontSize = 38.sp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                )
            }
        } else {
            if (historyItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = true)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "No history saved",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.displayLarge.copy(fontSize = 38.sp),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    )
                }
            } else {
                FilledTonalButton(
                    content = { Text(
                        text = "Clear All",
                        fontFamily = spaceMonoFamily
                    ) },
                    onClick = onClearHistory,
                    enabled = true,
                    shapes = ButtonDefaults.shapes(),
                    colors = filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f),
                        contentColor = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.6f)
                    ),
                    modifier = Modifier
                        .wrapContentSize(Alignment.Center)
                        .align(Alignment.End)
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                )
                LazyColumn(
                    modifier = Modifier.padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(
                        items = historyItems,
                        key = { it.id }
                    ) { item ->
                        HistoryItemRow(
                            item = item,
                            onDelete = onDelete,
                            onRestore = onRestore
                        )
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
            )
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Preview(showBackground = true)
@Composable
fun HistorySectionPreview() {
    val sampleItems = listOf(
        HistoryItem(id = 1, expression = "2+2", result = "4"),
        HistoryItem(id = 2, expression = "3*3", result = "9")
    )
    MyCalculatorTheme(darkTheme = true) {
        HistorySection(
            historyItems = sampleItems,
            onDelete = {},
            onRestore = {},
            onClearHistory = {}
        )
    }
}
