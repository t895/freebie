package com.t895.freebie.activities

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.result.ActivityResultLauncher
import android.os.Bundle
import com.t895.freebie.R
import android.widget.Toast
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import android.os.Build
import android.content.Intent
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ColorInt
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.color.MaterialColors
import com.t895.freebie.databinding.ActivitySetupBinding
import com.t895.freebie.utils.ThemeHelper

class SetupActivity : AppCompatActivity() {
    private val TAG = "SetupActivity"

    private lateinit var mBinding: ActivitySetupBinding

    private lateinit var btnPermission: Button
    private var startButtonClicked = false
    private var requestPermissionLauncher: ActivityResultLauncher<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeHelper.setTheme(this)

        super.onCreate(savedInstanceState)
        mBinding = ActivitySetupBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setInsets()

        btnPermission = mBinding.btnPermission

        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted: Boolean ->
                if (granted) {
                    btnPermission.setText(R.string.start_app)
                } else {
                    Toast.makeText(
                        applicationContext, R.string.permission_denied_toast,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.READ_MEDIA_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            goToMainActivity()
        }

        btnPermission.setOnClickListener {
            if (ContextCompat
                    .checkSelfPermission(applicationContext, Manifest.permission.READ_MEDIA_AUDIO)
                != PackageManager.PERMISSION_GRANTED
            ) {
                // Request permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestPermissionLauncher!!.launch(Manifest.permission.READ_MEDIA_AUDIO)
                } else {
                    requestPermissionLauncher!!.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            } else {
                // Button enters main activity when permissions are granted
                if (startButtonClicked) {
                    return@setOnClickListener
                }
                goToMainActivity()
                startButtonClicked = true
            }
        }
    }

    private fun goToMainActivity() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish()
    }

    private fun setInsets() {
        // Don't fit system windows
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Insets for app bar
        ViewCompat.setOnApplyWindowInsetsListener(mBinding.root) { _, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            val mlp = mBinding.root.layoutParams as ViewGroup.MarginLayoutParams
            mlp.topMargin = insets.top
            mlp.bottomMargin = insets.bottom
            mBinding.root.layoutParams = mlp

            @ColorInt val color: Int =
                MaterialColors.getColor(window.decorView, R.attr.colorSurface)
            ThemeHelper.setNavigationBarColor(this, color)

            windowInsets
        }
    }
}
