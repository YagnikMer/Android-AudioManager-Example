package com.example.yagnik.audiomanagerdemo;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    AudioManager audioManager;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    public void init() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        textView = (TextView) findViewById(R.id.tvstatus);
        updateRingerStatus();
    }

    public void setRingMode(View view) {
        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        updateRingerStatus();

    }

    public void setSlientMode(View view) {
        try {
            if (Build.VERSION.SDK_INT < 23) {
                audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                updateRingerStatus();
            } else if (Build.VERSION.SDK_INT >= 23) {
                this.requestForDoNotDisturbPermissionOrSetDoNotDisturbForApi23AndUp();
            }
        } catch (SecurityException e) {

        }
    }

    public void setVibrateMode(View view) {
        audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        updateRingerStatus();
    }

    public void updateRingerStatus() {
        int status = audioManager.getRingerMode();
        if (status == AudioManager.RINGER_MODE_NORMAL)
            textView.setText("Normal");
        if (status == AudioManager.RINGER_MODE_SILENT)
            textView.setText("Slient");
        if (status == AudioManager.RINGER_MODE_VIBRATE)
            textView.setText("Vibrate");
    }

    private void requestForDoNotDisturbPermissionOrSetDoNotDisturbForApi23AndUp() {

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        // if user granted access else ask for permission
        if (notificationManager.isNotificationPolicyAccessGranted()) {
            AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        } else {
            // Open Setting screen to ask for permisssion
            Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivityForResult(intent, 100);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            this.requestForDoNotDisturbPermissionOrSetDoNotDisturbForApi23AndUp();
        }
    }
}
