package edu.whut.lixin.easyMusicPlayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MusicService extends Service {

    private final IBinder binder = new MusicBinder();
    private int[] mp3List = {R.raw.m1, R.raw.m2, R.raw.m3};
    // id of song to show
    private int songId = 0;
    private MediaPlayer mediaPlayer;

    private static final String TAG = "MusicService";

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, mp3List[songId]);
        Log.d(TAG, "onCreate() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    public class MusicBinder extends Binder {

        void play(int num) {
            Log.d(TAG, "play() called with: num = [" + num + "]");
            // song is changed
            if (num != songId) {
                songId = num;
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = MediaPlayer.create(MusicService.this, mp3List[songId]);
            }
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
            }
        }

        void play() {
            play(songId);
        }

        void pause() {
            Log.d(TAG, "pause() called");
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
        }

        void stop() {
            Log.d(TAG, "stop() called");
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.reset();
            }
        }

    }
}
