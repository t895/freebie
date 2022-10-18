package com.t895.freebie.utils

import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.math.roundToInt

object InsetsHelper {
    const val EIGHT_DP = 8
    const val NAV_BAR_DP = 80
    const val EXTRA_LIST_SPACING_DP = 36

    fun dpToPx(context: Context, dp: Int): Int {
        val displayMetrics = context.resources.displayMetrics
        return (dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
    }

    fun setListInsets(list: View, context: Context) {
        ViewCompat.setOnApplyWindowInsetsListener(list) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            val extraPadding: Int = dpToPx(context, NAV_BAR_DP + EXTRA_LIST_SPACING_DP)
            view.setPadding(
                0,
                0,
                0,
                insets.bottom + dpToPx(context, NAV_BAR_DP + EXTRA_LIST_SPACING_DP)
            )
            windowInsets
        }
    }
}