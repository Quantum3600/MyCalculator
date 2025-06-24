package com.example.mycalculator.ui.theme


import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.mycalculator.R

object DisplayLargeVFConfig {
    const val WEIGHT = 950
    const val WIDTH = 75f
    val OPTICAL_SIZE = 144.sp
}

@OptIn(ExperimentalTextApi::class)
val displayLargeFontFamily = FontFamily(
    Font(
        R.font.truculenta_var,
        FontWeight.Bold,
        FontStyle.Normal,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(DisplayLargeVFConfig.WEIGHT),
            FontVariation.width(DisplayLargeVFConfig.WIDTH),
            FontVariation.opticalSizing(DisplayLargeVFConfig.OPTICAL_SIZE),
        )
    )
)