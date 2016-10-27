
package com.gpc.minesweeper.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gpc.minesweeper.R;
import com.gpc.minesweeper.interfaces.RateUsListener;
import com.gpc.minesweeper.interfaces.SettingListener;
import com.gpc.minesweeper.interfaces.VictoryListener;

/**
 * Created by pcgu on 16-10-27.
 */

public class DialogManager {

    private static DialogManager sInstance;
    private AlertDialog mVictoryDialog;
    private AlertDialog mRateUsDialog;

    private DialogManager() {

    }

    public static DialogManager getInstance() {
        if (sInstance == null) {
            synchronized (DialogManager.class) {
                sInstance = new DialogManager();
            }
        }
        return sInstance;
    }

    public void showDifficultDialog(final Context context,
            final SettingListener listener) {
        int checkedItem;
        int difficult = PreferenceUtils.getDifficultType(context);
        if (difficult == Constant.DIFFICULT_EASY) {
            checkedItem = 0;
        } else if (difficult == Constant.DIFFICULT_MEDIUM) {
            checkedItem = 1;
        } else {
            checkedItem = 2;
        }
        AlertDialog dialog = new AlertDialog.Builder(context).setTitle(R.string.setting_difficult)
                .setSingleChoiceItems(new String[] {
                        context.getString(R.string.setting_difficult_easy),
                        context.getString(R.string.setting_difficult_medium),
                        context.getString(R.string.setting_difficult_hard)
                }, checkedItem, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            PreferenceUtils.setDifficultType(context,
                                    Constant.DIFFICULT_EASY);
                        } else if (which == 1) {
                            PreferenceUtils.setDifficultType(context,
                                    Constant.DIFFICULT_MEDIUM);
                        } else {
                            PreferenceUtils.setDifficultType(context,
                                    Constant.DIFFICULT_HARD);
                        }
                        dialog.dismiss();
                    }
                }).create();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                listener.onDismissListener();
            }
        });
        dialog.show();
    }

    public void showVictoryDialog(Context context, final VictoryListener listener,
            final int yourTime) {
        if (null != mVictoryDialog && mVictoryDialog.isShowing()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View content = LayoutInflater.from(context).inflate(R.layout.layout_wind,
                null);
        Button btnDone = (Button) content.findViewById(R.id.btn_done);
        Button btnNewGame = (Button) content.findViewById(R.id.btn_new_game);
        Button btnShare = (Button) content.findViewById(R.id.btn_share);
        TextView tvNewRecord = (TextView) content.findViewById(R.id.tv_new_record);
        TextView tvBestTime = (TextView) content.findViewById(R.id.tv_best_time);
        TextView tvYourTime = (TextView) content.findViewById(R.id.tv_your_time);

        final int bestTime = Utils.getBestTime(context);
        if (Utils.isNewRecord(bestTime, yourTime)) {
            tvNewRecord.setVisibility(View.VISIBLE);
        } else {
            tvNewRecord.setVisibility(View.GONE);
        }

        if (bestTime > 0) {
            tvBestTime.setVisibility(View.VISIBLE);
            tvBestTime.setText(
                    context.getString(R.string.win_best_time) + Utils.getTimeBySeconds(bestTime));
        } else {
            tvBestTime.setVisibility(View.GONE);
        }

        final String yourTimeStr = Utils.getTimeBySeconds(yourTime);
        tvYourTime.setText(context.getString(R.string.win_you_time) + yourTimeStr);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVictoryDialog.dismiss();
                listener.onCancelClick();
            }
        });

        btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVictoryDialog.dismiss();
                listener.onOkClick();
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVictoryDialog.dismiss();
                listener.onShareClick(Utils.isNewRecord(bestTime, yourTime), yourTime);
            }
        });
        builder.setCancelable(true);
        builder.setView(content);
        mVictoryDialog = builder.create();
        mVictoryDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mVictoryDialog = null;
            }
        });
        mVictoryDialog.setCanceledOnTouchOutside(false);
        mVictoryDialog.show();
        Utils.saveBestTime(context, yourTime);
    }

    public void showRateUsDialog(Context context, final RateUsListener listener) {
        if (null != mRateUsDialog && mRateUsDialog.isShowing()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View content = LayoutInflater.from(context).inflate(R.layout.view_dialog_rate_us,
                null);
        Button btnRateUs = (Button) content.findViewById(R.id.btn_rate_us);
        Button btnNoThanks = (Button) content.findViewById(R.id.btn_no_thanks);

        btnNoThanks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRateUsDialog.dismiss();
                listener.onCancelClick();
            }
        });

        btnRateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRateUsDialog.dismiss();
                listener.onOkClick();
            }
        });
        builder.setCancelable(true);
        builder.setView(content);
        mRateUsDialog = builder.create();
        mRateUsDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mRateUsDialog = null;
            }
        });
        mRateUsDialog.setCanceledOnTouchOutside(false);
        mRateUsDialog.show();
    }
}
