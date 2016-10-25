
package com.gpc.minesweeper.utils;

import android.graphics.drawable.Drawable;

/**
 * Created by pcgu on 16-10-25.
 */

public class DrawableManager {

    private static DrawableManager mInstance;
    private Drawable[] mDrawables;

    private DrawableManager() {

    }

    public static DrawableManager getInstance() {
        if (mInstance == null) {
            synchronized (DrawableManager.class) {
                mInstance = new DrawableManager();
            }
        }
        return mInstance;
    }
}
