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

    const val THREE_BUTTON_NAVIGATION = 0
    const val TWO_BUTTON_NAVIGATION = 1
    const val GESTURE_NAVIGATION = 2

    fun dpToPx(context: Context, dp: Int): Int {
        val displayMetrics = context.resources.displayMetrics
        return (dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
    }

    fun setListInsets(list: View, context: Context) {
        ViewCompat.setOnApplyWindowInsetsListener(list) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            val extraPadding: Int = dpToPx(context, NAV_BAR_DP + EXTRA_LIST_SPACING_DP)
            view.setPadding(
                insets.left,
                0,
                insets.right,
                insets.bottom + dpToPx(context, NAV_BAR_DP + EXTRA_LIST_SPACING_DP)
            )
            windowInsets
        }
    }

    fun getSystemGestureType(context: Context): Int {
        val resources = context.resources
        val resourceId =
            resources.getIdentifier("config_navBarInteractionMode", "integer", "android")
        if (resourceId != 0) {
            return resources.getInteger(resourceId)
        }
        return 0
    }
}