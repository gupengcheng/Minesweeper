
package com.gpc.minesweeper.utils;

import android.app.Activity;
import android.view.WindowManager;

/**
 * Created by pcgu on 16-10-26.
 */

public class StatusBarUtils {

    public static void toggleStatusBar(Activity activity, boolean showStatusBar) {
        /*
         * int flag = 0; 这种方法只能隐藏部分状态栏的icon flag = View.STATUS_BAR_HIDDEN;
         * this.getWindow().getDecorView().setSystemUiVisibility(flag);
         * this.getWindow().getDecorView().requestLayout();
         */
        if (showStatusBar) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            // 如果使用下面这个选项则无法再切会全屏模式
            /*
             * getWindow().setFlags(WindowManager.LayoutParams.
             * FLAG_FORCE_NOT_FULLSCREEN,
             * WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
             */
        } else {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

    }

}
