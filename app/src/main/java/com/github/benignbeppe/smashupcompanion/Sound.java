package com.github.benignbeppe.smashupcompanion;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by sebastian on 2015-11-28.
 */
public class Sound {
    public static void play(Context context, int id) {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, id);
        mediaPlayer.setOnCompletionListener(
                new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        mediaPlayer.start();
    }
}
