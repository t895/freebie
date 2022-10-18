package com.t895.freebie.utils

import android.content.res.Configuration
import android.graphics.Color
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.color.MaterialColors
import com.t895.freebie.R
import kotlin.math.roundToInt

object ThemeHelper {
    const val TAG = "ThemeHelper"

    const val NAV_BAR_ALPHA: Float = 0.9f

    fun setTheme(activity: AppCompatActivity) {
        setStatusBarColor(activity, R.attr.colorSurface)
    }

    private fun setStatusBarColor(activity: AppCompatActivity, @AttrRes colorId: Int) {
        @ColorInt val color: Int = MaterialColors.getColor(activity.window.decorView, colorId)
        activity.window.statusBarColor = color
    }

    fun setNavigationBarColor(activity: AppCompatActivity, @ColorInt color: Int) {
        val gestureType = InsetsHelper.getSystemGestureType(activity.applicationContext)
        val orientation = activity.resources.configuration.orientation
        if ((gestureType == InsetsHelper.THREE_BUTTON_NAVIGATION || gestureType == InsetsHelper.TWO_BUTTON_NAVIGATION) && orientation == Configuration.ORIENTATION_LANDSCAPE) {
            activity.window.navigationBarColor = color
        } else if (gestureType == InsetsHelper.THREE_BUTTON_NAVIGATION || gestureType == InsetsHelper.TWO_BUTTON_NAVIGATION) {
            activity.window.navigationBarColor = getColorWithOpacity(color, NAV_BAR_ALPHA)
        } else {
            activity.window.navigationBarColor = ContextCompat.getColor(
                activity.applicationContext,
                android.R.color.transparent
            )
        }
    }

    private fun getColorWithOpacity(@ColorInt color: Int, alphaFactor: Float): Int {
        return Color.argb(
            (alphaFactor * Color.alpha(color)).roundToInt(), Color.red(color),
            Color.green(color), Color.blue(color)
        )
    }
}