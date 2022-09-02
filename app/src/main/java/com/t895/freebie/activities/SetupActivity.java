package com.t895.freebie.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.t895.freebie.R;

public class SetupActivity extends AppCompatActivity
{
  public static final String TAG = "SetupActivity";

  private Button btnPermission;
  private boolean startButtonClicked = false;

  private ActivityResultLauncher<String> requestPermissionLauncher;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted ->
            {
              if (granted)
              {
                btnPermission.setText(R.string.start_app);
              }
              else
              {
                Toast.makeText(getApplicationContext(), R.string.permission_denied_toast,
                        Toast.LENGTH_LONG).show();
              }
            });

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_setup);

    if (ContextCompat.checkSelfPermission(getApplicationContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED)
    {
      goToMainActivity();
    }

    btnPermission = findViewById(R.id.btnPermission);
    btnPermission.setOnClickListener(view ->
    {
      if (ContextCompat.checkSelfPermission(getApplicationContext(),
              Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED)
      {
        // Request permission
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        {
          requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_AUDIO);
        }
        else
        {
          requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
      }
      else
      {
        // Button enters main activity when permissions are granted
        if (startButtonClicked) // Avoid running multiple times
        {
          return;
        }
        goToMainActivity();
        startButtonClicked = true;
      }
    });
  }

  private void goToMainActivity()
  {
    Intent i = new Intent(this, MainActivity.class);
    startActivity(i);
    finish();
  }
}