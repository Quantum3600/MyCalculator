package com.example.mycalculator.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mycalculator.R
import com.example.mycalculator.data.HistoryItem
import com.example.mycalculator.ui.theme.MyCalculatorTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryItemRow(
    item: HistoryItem,
    onDelete: (HistoryItem) -> Unit,
    onRestore: (HistoryItem) -> Unit
) {
    val spaceMonoFamily = FontFamily(
        Font(R.font.space_mono_bold, FontWeight.Bold)
    )
    val swipeToDismissBoxState = rememberSwipeToDismissBoxState()
    if (swipeToDismissBoxState.currentValue == SwipeToDismissBoxValue.EndToStart) {
        LaunchedEffect(swipeToDismissBoxState.currentValue) {
            onDelete(item)
            swipeToDismissBoxState.reset()
        }
    }
    SwipeToDismissBox(
        state = swipeToDismissBoxState,
        backgroundContent = {
            val color = MaterialTheme.colorScheme.errorContainer
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape = RoundedCornerShape(18.dp))
                    .background(color),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(end = 24.dp)
                )
            }
        },
        enableDismissFromStartToEnd = false
    ) {
        ListItem(
            supportingContent = {
                Text(
                    text = item.result,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontFamily = spaceMonoFamily
                )
            },
            headlineContent = {
                Text(
                    text = item.expression + " =",
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    fontFamily = spaceMonoFamily,
                )
            },
            modifier = Modifier
                .clickable { onRestore(item) }
                .clip(shape = RoundedCornerShape(16.dp))
            ,
            colors = ListItemDefaults.colors()
        )
    }
}
@Preview(showBackground = true, backgroundColor = 0x000000)
@Composable
fun HistoryItemRowPreview() {
    MyCalculatorTheme(darkTheme = true) {
        HistoryItemRow(
            item = HistoryItem(id = 1, expression = "5+7", result = "12"),
            onDelete = {},
            onRestore = {}
        )
    }
}