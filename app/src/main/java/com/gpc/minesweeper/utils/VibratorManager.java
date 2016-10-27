
package com.gpc.minesweeper.utils;

import android.content.Context;
import android.os.Vibrator;

import com.gpc.minesweeper.MinesweeperApplication;

/**
 * Created by pcgu on 16-10-27.
 */

public class VibratorManager {
    private static VibratorManager sInstance;
    private Vibrator mVibrator;

    private VibratorManager() {

    }

    public static VibratorManager getInstance() {
        if (sInstance == null) {
            synchronized (VibratorManager.class) {
                sInstance = new VibratorManager();
            }
        }
        return sInstance;
    }

    public void vibrator() {
        if (!PreferenceUtils.getVibration(MinesweeperApplication.getInstance())) {
            return;
        }
        if (mVibrator == null) {
            mVibrator = (Vibrator) MinesweeperApplication.getInstance()
                    .getSystemService(Context.VIBRATOR_SERVICE);
        }
        mVibrator.vibrate(100);
    }
}
