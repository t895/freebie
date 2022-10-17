package com.t895.freebie.utils

import android.graphics.Color
import android.os.Build
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.color.MaterialColors
import com.t895.freebie.R
import kotlin.math.roundToInt

object ThemeHelper {
    const val NAV_BAR_ALPHA: Float = 0.9f

    fun setTheme(activity: AppCompatActivity)
    {
        setStatusBarColor(activity, R.attr.colorSurface)
    }

    private fun setStatusBarColor(activity: AppCompatActivity, @AttrRes colorId: Int) {
        @ColorInt val color: Int = MaterialColors.getColor(activity.window.decorView, colorId)
        activity.window.statusBarColor = color
    }

    fun setNavigationBarColor(activity: AppCompatActivity, @ColorInt color: Int) {
        var gestureInset = 0
        if (Build.VERSION.SDK_INT >= 30) {
            gestureInset = activity.window.decorView.rootWindowInsets.getInsets(WindowInsetsCompat.Type.systemGestures()).left
        }

        if (gestureInset == 0) {
            activity.window.navigationBarColor = getColorWithOpacity(color, NAV_BAR_ALPHA)
        } else {
            activity.window.navigationBarColor = ContextCompat.getColor(activity.applicationContext,
                android.R.color.transparent)
        }
    }

    private fun getColorWithOpacity(@ColorInt color: Int, alphaFactor: Float): Int {
        return Color.argb((alphaFactor * Color.alpha(color)).roundToInt(), Color.red(color),
            Color.green(color), Color.blue(color))
    }
}