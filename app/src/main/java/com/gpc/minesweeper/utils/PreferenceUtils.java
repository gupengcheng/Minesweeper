
package com.gpc.minesweeper.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.blankj.utilcode.utils.ConvertUtils;

import org.json.JSONArray;

/**
 * Created by pcgu on 16-10-26.
 */

public class PreferenceUtils {

    private PreferenceUtils() {

    }

    public static boolean getVibration(Context context) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getBoolean("vibration", true);
    }

    public static void setVibration(Context context, boolean vibration) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings.edit().putBoolean("vibration", vibration).commit();
    }

    public static boolean getMute(Context context) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getBoolean("mute", false);
    }

    public static void setMute(Context context, boolean mute) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings.edit().putBoolean("mute", mute).commit();
    }

    public static boolean getFullscreenMode(Context context) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getBoolean("setting_fullscreen_mode", true);
    }

    public static void setFullscreenMode(Context context, boolean isFullscreenMode) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings.edit().putBoolean("setting_fullscreen_mode", isFullscreenMode).commit();
    }

    public static int getCellSize(Context context) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        return settings.getInt("setting_cell_size",
                ConvertUtils.dp2px(context, Constant.CELL_SIZE_DEFAULT));
    }

    public static void setCellSize(Context context, int value) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings.edit().putInt("setting_cell_size", value).commit();
    }

    public static int getDifficultType(Context context) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        return settings.getInt("setting_difficult", Constant.DIFFICULT_EASY);
    }

    public static void setDifficultType(Context context, int value) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings.edit().putInt("setting_difficult", value).commit();
    }

    public static int getEasyBestTime(Context context) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getInt("difficult_easy_best", 0);
    }

    public static void setEasyBestTime(Context context, int value) {
        int bestTime = getEasyBestTime(context);
        if (value < bestTime || bestTime == 0) {
            final SharedPreferences settings = PreferenceManager
                    .getDefaultSharedPreferences(context);
            settings.edit().putInt("difficult_easy_best", value).commit();
        }
    }

    public static int getMediumBestTime(Context context) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getInt("difficult_medium_best", 0);
    }

    public static void setMediumBestTime(Context context, int value) {
        int bestTime = getMediumBestTime(context);
        if (value < bestTime || bestTime == 0) {
            final SharedPreferences settings = PreferenceManager
                    .getDefaultSharedPreferences(context);
            settings.edit().putInt("difficult_medium_best", value).commit();
        }
    }

    public static int getHardBestTime(Context context) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getInt("difficult_hard_best", 0);
    }

    public static void setHardBestTime(Context context, int value) {
        int bestTime = getHardBestTime(context);
        if (value < bestTime || bestTime == 0) {
            final SharedPreferences settings = PreferenceManager
                    .getDefaultSharedPreferences(context);
            settings.edit().putInt("difficult_hard_best", value).commit();
        }
    }

    public static int getPlayedTime(Context context) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getInt("save_time", 0);
    }

    public static void setPlayedTime(Context context, int value) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings.edit().putInt("save_time", value).commit();
    }

    public static int getExistBombNumber(Context context) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getInt("exist_bomb_number", 0);
    }

    public static void setExistBombNumber(Context context, int value) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings.edit().putInt("exist_bomb_number", value).commit();
    }

    public static int getRealBombNumber(Context context) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getInt("real_bomb_number", 0);
    }

    public static void setRealBombNumber(Context context, int value) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings.edit().putInt("real_bomb_number", value).commit();
    }

    public static int getTotalRows(Context context) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getInt("save_total_row", 0);
    }

    public static void setTotalRows(Context context, int value) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings.edit().putInt("save_total_row", value).commit();
    }

    public static int getTotalColumns(Context context) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getInt("save_total_column", 0);
    }

    public static void setTotalColumns(Context context, int value) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings.edit().putInt("save_total_column", value).commit();
    }

    public static void setOpenNumbers(Context context, int value) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings.edit().putInt("save_open_number", value).commit();
    }

    public static int getOpenNumbers(Context context) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getInt("save_open_number", 0);
    }

    public static boolean[][] getBombsArray(Context context) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        String bombsArrayStr = settings.getString("save_bombs_array", "");
        if (TextUtils.isEmpty(bombsArrayStr)) {
            return null;
        }
        int totalRows = getTotalRows(context);
        if (totalRows == 0) {
            return null;
        }
        int totalColumns = getTotalColumns(context);
        if (totalColumns == 0) {
            return null;
        }
        boolean[][] bombs = new boolean[totalRows][totalColumns];
        try {
            JSONArray jsonArray = new JSONArray(bombsArrayStr);
            for (int i = 0; i < bombs.length; i++) {
                for (int j = 0; j < bombs[i].length; j++) {
                    bombs[i][j] = jsonArray.getBoolean(i * bombs[i].length + j);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return bombs;
    }

    public static void setBombsArray(Context context, boolean[][] bombs) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        if (bombs == null) {
            settings.edit().putString("save_bombs_array", "").commit();
            return;
        }
        try {
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < bombs.length; i++) {
                for (int j = 0; j < bombs[i].length; j++) {
                    jsonArray.put(bombs[i][j]);
                }
            }
            settings.edit().putString("save_bombs_array", jsonArray.toString()).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean[][] getMarksArray(Context context) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        String marksArrayStr = settings.getString("save_marks_array", "");
        if (TextUtils.isEmpty(marksArrayStr)) {
            return null;
        }
        int totalRows = getTotalRows(context);
        if (totalRows == 0) {
            return null;
        }
        int totalColumns = getTotalColumns(context);
        if (totalColumns == 0) {
            return null;
        }
        boolean[][] marks = new boolean[totalRows][totalColumns];
        try {
            JSONArray jsonArray = new JSONArray(marksArrayStr);
            for (int i = 0; i < marks.length; i++) {
                for (int j = 0; j < marks[i].length; j++) {
                    marks[i][j] = jsonArray.getBoolean(i * marks[i].length + j);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return marks;
    }

    public static void setMarksArray(Context context, boolean[][] marks) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        if (marks == null) {
            settings.edit().putString("save_marks_array", "").commit();
            return;
        }
        try {
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < marks.length; i++) {
                for (int j = 0; j < marks[i].length; j++) {
                    jsonArray.put(marks[i][j]);
                }
            }
            settings.edit().putString("save_marks_array", jsonArray.toString()).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean[][] getOpensArray(Context context) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        String opensArrayStr = settings.getString("save_opens_array", "");
        if (TextUtils.isEmpty(opensArrayStr)) {
            return null;
        }
        int totalRows = getTotalRows(context);
        if (totalRows == 0) {
            return null;
        }
        int totalColumns = getTotalColumns(context);
        if (totalColumns == 0) {
            return null;
        }
        boolean[][] opens = new boolean[totalRows][totalColumns];
        try {
            JSONArray jsonArray = new JSONArray(opensArrayStr);
            for (int i = 0; i < opens.length; i++) {
                for (int j = 0; j < opens[i].length; j++) {
                    opens[i][j] = jsonArray.getBoolean(i * opens[i].length + j);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return opens;
    }

    public static void setOpensArray(Context context, boolean[][] opens) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        if (opens == null) {
            settings.edit().putString("save_opens_array", "").commit();
            return;
        }
        try {
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < opens.length; i++) {
                for (int j = 0; j < opens[i].length; j++) {
                    jsonArray.put(opens[i][j]);
                }
            }
            settings.edit().putString("save_opens_array", jsonArray.toString()).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int[][] getBombNumberArray(Context context) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        String bombNumberStr = settings.getString("save_bomb_number_array", "");
        if (TextUtils.isEmpty(bombNumberStr)) {
            return null;
        }
        int totalRows = getTotalRows(context);
        if (totalRows == 0) {
            return null;
        }
        int totalColumns = getTotalColumns(context);
        if (totalColumns == 0) {
            return null;
        }
        int[][] bombNumberArray = new int[totalRows][totalColumns];
        try {
            JSONArray jsonArray = new JSONArray(bombNumberStr);
            for (int i = 0; i < bombNumberArray.length; i++) {
                for (int j = 0; j < bombNumberArray[i].length; j++) {
                    bombNumberArray[i][j] = jsonArray.getInt(i * bombNumberArray[i].length + j);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return bombNumberArray;
    }

    public static void setBombNumberArray(Context context, int[][] bombNumberArray) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        if (bombNumberArray == null) {
            settings.edit().putString("save_bomb_number_array", "").commit();
            return;
        }
        try {
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < bombNumberArray.length; i++) {
                for (int j = 0; j < bombNumberArray[i].length; j++) {
                    jsonArray.put(bombNumberArray[i][j]);
                }
            }
            settings.edit().putString("save_bomb_number_array", jsonArray.toString()).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取用户关闭游戏时屏幕的横竖,默认为竖屏
     *
     * @author pcgu
     * @time 16-10-20 下午3:18
     */
    public static int getScreenOrientation(Context context) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getInt("save_screen_orientation", 1);
    }

    /**
     * 存储用户关闭游戏时屏幕的横竖
     *
     * @author pcgu
     * @time 16-10-20 下午3:17
     */
    public static void setScreenOrientation(Context context, int screenOrientation) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings.edit().putInt("save_screen_orientation", screenOrientation).commit();
    }

    public static boolean getMarkedState(Context context) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getBoolean("save_marked_state", false);
    }

    public static void setMarkedState(Context context, boolean markedState) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings.edit().putBoolean("save_marked_state", markedState).commit();
    }

    /**
     * 是否要恢复之前的游戏状态
     * 
     * @param context
     * @return true 要恢复 false不恢复
     */
    public static boolean isRestoreGame(Context context) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getBoolean("restore_game", false);
    }

    public static void setRestoreGame(Context context, boolean restoreGame) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings.edit().putBoolean("restore_game", restoreGame).commit();
    }

    public static boolean getShowRateUs(Context context) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getBoolean("rate_us", true);
    }

    public static void setShowRateUs(Context context, boolean rateUs) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings.edit().putBoolean("rate_us", rateUs).commit();
    }

}
