package com.example.celebrityquiz;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.example.celebrityquiz.R;

import androidx.annotation.Nullable;

public class MusicService extends Service {
    MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
    @Override
    public void onCreate(){
        super.onCreate();

        mediaPlayer = MediaPlayer.create(this, R.raw.love);
        mediaPlayer.setLooping(true);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        mediaPlayer.start();

        return super.onStartCommand(intent,flags,startId);

    }
    @Override
    public void onDestroy(){
        super.onDestroy();

        mediaPlayer.stop();
    }
}
