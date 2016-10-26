
package com.gpc.minesweeper.utils;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import com.gpc.minesweeper.MinesweeperApplication;

/**
 * Created by pcgu on 16-10-26.
 */

public class GameSoundManager {

    private static GameSoundManager sInstance;

    private SoundPool mSoundPool;
    private AudioManager mAudioManager;

    // 允许同时播放的声音数量
    private final int MAX_STREAMS = 3;

    public static GameSoundManager getInstance() {
        if (sInstance == null) {
            synchronized (GameSoundManager.class) {
                if (sInstance == null) {
                    sInstance = new GameSoundManager();
                }
            }
        }
        return sInstance;
    }

    public int getSoundId(int resId) {
        return getSoundPool().load(MinesweeperApplication.getInstance(), resId, 1);
    }

    public void playingSound(int soundID) {
        // 如果是静音模式，直接返回
        if (PreferenceUtils.getMute(MinesweeperApplication.getInstance())) {
            return;
        }
        float volume = getCurrentVolume();
        getSoundPool().play(soundID, volume, volume, 1, 0, 1f);
    }

    private SoundPool getSoundPool() {
        if (mSoundPool != null) {
            return mSoundPool;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            SoundPool.Builder builder = new SoundPool.Builder();
            builder.setMaxStreams(MAX_STREAMS);
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            builder.setAudioAttributes(attributes);
            mSoundPool = builder.build();
        } else {
            mSoundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        }
        return mSoundPool;
    }

    private AudioManager getAudioManager() {
        if (mAudioManager == null) {
            mAudioManager = (AudioManager) MinesweeperApplication.getInstance()
                    .getSystemService(Context.AUDIO_SERVICE);
        }
        return mAudioManager;
    }

    private float getCurrentVolume() {
        float currentVolumeIndex = getAudioManager().getStreamVolume(AudioManager.MODE_RINGTONE);
        float maxVolumeIndex = getAudioManager().getStreamMaxVolume(AudioManager.MODE_RINGTONE);
        return currentVolumeIndex / maxVolumeIndex;
    }

}
