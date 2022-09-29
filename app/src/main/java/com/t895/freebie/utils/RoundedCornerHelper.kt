package com.t895.freebie.utils

import android.content.Context
import android.util.DisplayMetrics
import kotlin.math.roundToInt

object RoundedCornerHelper
{
    const val EIGHT_DP = 8

    fun dpToPx(context: Context, dp: Int): Int
    {
        val displayMetrics = context.resources.displayMetrics
        return (dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
    }
}
