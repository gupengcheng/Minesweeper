
package com.gpc.minesweeper.utils;

import com.gpc.minesweeper.R;

/**
 * Created by pcgu on 16-10-26.
 */

public class Constant {

    public static final int[] IV_RESOURCE_ID = {
            R.mipmap.iv_empty, R.mipmap.iv_n1, R.mipmap.iv_n2, R.mipmap.iv_n3, R.mipmap.iv_n4,
            R.mipmap.iv_n5, R.mipmap.iv_n6, R.mipmap.iv_n7, R.mipmap.iv_n8, R.mipmap.iv_n9,
            R.mipmap.iv_flag, R.mipmap.iv_button, R.mipmap.iv_wrflag, R.mipmap.iv_gmine,
            R.mipmap.iv_rmine, R.mipmap.iv_c0, R.mipmap.iv_c1, R.mipmap.iv_c2, R.mipmap.iv_c3,
            R.mipmap.iv_c4, R.mipmap.iv_c5, R.mipmap.iv_c6, R.mipmap.iv_c7, R.mipmap.iv_c8,
            R.mipmap.iv_c9
    };

    public static final double[] DIFFICULT_LEVEL = new double[] {
            0.15d, 0.2d, 0.375d
    };

    public static final int CELL_SIZE_DEFAULT = 30;
    public static final int CELL_SIZE_MAX = 70;
    public static final int CELL_SIZE_MIN = 25;

    // 游戏难度
    public static final int DIFFICULT_EASY = 1;
    public static final int DIFFICULT_MEDIUM = 2;
    public static final int DIFFICULT_HARD = 3;

    // 游戏状态 开始 胜利 失败
    public static final int STATE_PLAYING = 1;
    public static final int STATE_WIN = 2;
    public static final int STATE_LOST = 3;

    public static final int REQUEST_SETTING_CODE = 111;
    public static final int RESULT_SETTING_CODE = 112;

    public static final int BOMB_AREA_NUMBER = 12;

}
