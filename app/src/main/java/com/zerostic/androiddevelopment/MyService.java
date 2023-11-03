package com.zerostic.androiddevelopment;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyService extends Service {
    MediaPlayer mediaPlayer;
    AudioManager audioManager;
    public static boolean isRunning = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isRunning = true;
        mediaPlayer = MediaPlayer.create(this, R.raw.default_tone);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        mediaPlayer.setLooping(true);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 20, 0);
        mediaPlayer.start();
        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
        mediaPlayer.stop();
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_SHORT).show();
    }
}
