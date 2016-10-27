
package com.gpc.minesweeper.utils;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import com.gpc.minesweeper.MinesweeperApplication;
import com.gpc.minesweeper.R;

import java.util.HashMap;

/**
 * Created by pcgu on 16-10-25.
 */

public class DrawableManager {

    private static DrawableManager sInstance;
    private HashMap<Integer, Drawable> mDrawables;

    private DrawableManager() {

    }

    public static DrawableManager getInstance() {
        if (sInstance == null) {
            synchronized (DrawableManager.class) {
                sInstance = new DrawableManager();
            }
        }
        return sInstance;
    }

    public Drawable getDrawable(int resourceId) {
        if (mDrawables == null || mDrawables.isEmpty()) {
            initDrawableMap();
        }
        return mDrawables.get(resourceId);
    }

    private void initDrawableMap() {
        mDrawables = new HashMap<>();
        for (int i = 0; i < Constant.IV_RESOURCE_ID.length; i++) {
            int id = Constant.IV_RESOURCE_ID[i];
            mDrawables.put(id, ContextCompat.getDrawable(MinesweeperApplication.getInstance(), id));
        }
    }

    public int getTimeNumberResourceId(int number) {
        switch (number) {
            case 0:
                return R.mipmap.iv_c0;
            case 1:
                return R.mipmap.iv_c1;
            case 2:
                return R.mipmap.iv_c2;
            case 3:
                return R.mipmap.iv_c3;
            case 4:
                return R.mipmap.iv_c4;
            case 5:
                return R.mipmap.iv_c5;
            case 6:
                return R.mipmap.iv_c6;
            case 7:
                return R.mipmap.iv_c7;
            case 8:
                return R.mipmap.iv_c8;
            case 9:
                return R.mipmap.iv_c9;
            default:
                return R.mipmap.iv_c0;
        }
    }

    public int getCellNumberResourceId(int number) {
        switch (number) {
            case 0:
                return R.mipmap.iv_empty;
            case 1:
                return R.mipmap.iv_n1;
            case 2:
                return R.mipmap.iv_n2;
            case 3:
                return R.mipmap.iv_n3;
            case 4:
                return R.mipmap.iv_n4;
            case 5:
                return R.mipmap.iv_n5;
            case 6:
                return R.mipmap.iv_n6;
            case 7:
                return R.mipmap.iv_n7;
            case 8:
                return R.mipmap.iv_n8;
            case 9:
                return R.mipmap.iv_n9;
            default:
                return R.mipmap.iv_empty;
        }
    }

}
