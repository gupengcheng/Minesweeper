
package com.gpc.minesweeper.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import java.util.List;

/**
 * Created by pcgu on 16-10-27.
 */

public class Utils {

    /**
     * 对数组进行转置
     *
     * @param data
     */
    public static boolean[][] reverseArray(boolean[][] data) {
        int row = data.length;
        int column = data[0].length;
        boolean[][] result = new boolean[column][row];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                result[j][i] = data[i][j];
            }
        }
        return result;
    }

    public static int[][] reverseArray(int[][] data) {
        int row = data.length;
        int column = data[0].length;
        int[][] result = new int[column][row];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                result[j][i] = data[i][j];
            }
        }
        return result;
    }

    public static String getTimeBySeconds(int seconds) {
        int minute = seconds / 60;
        int second = seconds % 60;

        int tenMinute = minute / 10;
        int bitMinute = minute % 10;

        int tenSecond = second / 10;
        int bitSecond = second % 10;

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(tenMinute).append(bitMinute).append(":").append(tenSecond)
                .append(bitSecond);
        return stringBuilder.toString();
    }

    public static boolean isNewRecord(int bestTime, int yourTime) {
        boolean newRecord;
        if (yourTime < bestTime) {
            newRecord = true;
        } else {
            newRecord = false;
        }
        return newRecord;
    }

    public static int getBestTime(Context context) {
        int difficult = PreferenceUtils.getDifficultType(context);
        if (difficult == Constant.DIFFICULT_EASY) {
            return PreferenceUtils.getEasyBestTime(context);
        } else if (difficult == Constant.DIFFICULT_MEDIUM) {
            return PreferenceUtils.getMediumBestTime(context);
        } else {
            return PreferenceUtils.getHardBestTime(context);
        }
    }

    public static void saveBestTime(Context context, int time) {
        int difficult = PreferenceUtils.getDifficultType(context);
        if (difficult == Constant.DIFFICULT_EASY) {
            PreferenceUtils.setEasyBestTime(context, time);
        } else if (difficult == Constant.DIFFICULT_MEDIUM) {
            PreferenceUtils.setMediumBestTime(context, time);
        } else {
            PreferenceUtils.setHardBestTime(context, time);
        }
    }

    public static void openMarketRating(Context context) {
        String packageName = context.getPackageName();
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + packageName)));
        } catch (android.content.ActivityNotFoundException e) {
            e.printStackTrace();
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
        }
    }

    public static void openGooglePlayRating(Context context) {
        Intent rateIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("market://details?id=" + context.getPackageName()));
        boolean marketFound = false;

        // find all applications able to handle our rateIntent
        final List<ResolveInfo> otherApps = context.getPackageManager()
                .queryIntentActivities(rateIntent, 0);
        for (ResolveInfo otherApp : otherApps) {
            // look for Google Play application
            if (otherApp.activityInfo.applicationInfo.packageName.equals("com.android.vending")) {

                ActivityInfo otherAppActivity = otherApp.activityInfo;
                ComponentName componentName = new ComponentName(
                        otherAppActivity.applicationInfo.packageName,
                        otherAppActivity.name);
                rateIntent.setFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                rateIntent.setComponent(componentName);
                context.startActivity(rateIntent);
                marketFound = true;
                break;

            }
        }

        // if GP not present on device, open web browser
        if (!marketFound) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                    "https://play.google.com/store/apps/details?id=" + context.getPackageName()));
            context.startActivity(webIntent);
        }
    }

    public static void shareText(Context context, String shareContent, String shareTitle) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareContent);
        shareIntent.setType("text/plain");

        context.startActivity(Intent.createChooser(shareIntent, shareTitle));
    }

}
