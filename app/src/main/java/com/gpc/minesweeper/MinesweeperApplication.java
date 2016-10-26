
package com.gpc.minesweeper;

import android.app.Application;

import com.blankj.utilcode.utils.LogUtils;

/**
 * Created by pcgu on 16-10-26.
 */

public class MinesweeperApplication extends Application {

    private static MinesweeperApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        LogUtils.init(mInstance, true, false, 'v', "Gu");
    }

    public static MinesweeperApplication getInstance() {
        return mInstance;
    }
}
