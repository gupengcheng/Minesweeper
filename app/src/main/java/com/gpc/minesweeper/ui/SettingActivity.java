
package com.gpc.minesweeper.ui;

import com.gpc.minesweeper.R;
import com.gpc.minesweeper.interfaces.SettingListener;
import com.gpc.minesweeper.utils.Constant;
import com.gpc.minesweeper.utils.DialogManager;
import com.gpc.minesweeper.utils.PreferenceUtils;
import com.gpc.minesweeper.utils.StatusBarUtils;
import com.gpc.minesweeper.utils.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Created by pcgu on 16-10-27.
 */

public class SettingActivity extends Activity {

    @BindView(R.id.rl_vibration)
    RelativeLayout mRlVibration;
    @BindView(R.id.rl_full_screen)
    RelativeLayout mRlFullscreenMode;
    @BindView(R.id.rl_size)
    RelativeLayout mRlSize;
    @BindView(R.id.rl_difficult)
    RelativeLayout mRlDifficult;
    @BindView(R.id.rl_mute)
    RelativeLayout mRlMute;
    @BindView(R.id.rl_rate_us)
    RelativeLayout mRlRateUs;
    @BindView(R.id.rl_tell_friend)
    RelativeLayout mRlTellFriends;

    @BindView(R.id.tv_vibration_desc)
    TextView mTvVibrationDesc;
    @BindView(R.id.tv_full_screen_desc)
    TextView mTvFullScreenModeDesc;
    @BindView(R.id.tv_difficult_desc)
    TextView mTvDifficultDesc;
    @BindView(R.id.tv_mute_desc)
    TextView mTvMuteDesc;

    @BindView(R.id.cb_vibration)
    CheckBox mCbVibration;
    @BindView(R.id.cb_full_screen)
    CheckBox mCbFullscreenMode;
    @BindView(R.id.cb_mute)
    CheckBox mCbMute;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        mContext = this;
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initViews() {
        boolean isVibration = PreferenceUtils.getVibration(mContext);
        boolean isMute = PreferenceUtils.getMute(mContext);
        boolean isFullScreen = PreferenceUtils.getFullscreenMode(mContext);
        int difficult = PreferenceUtils.getDifficultType(mContext);
        mCbVibration.setChecked(isVibration);
        mCbFullscreenMode.setChecked(isFullScreen);
        mCbMute.setChecked(isMute);
        mTvVibrationDesc.setText(isVibration ? R.string.setting_vibration_enabled
                : R.string.setting_vibration_disabled);
        mTvMuteDesc
                .setText(isMute ? R.string.setting_mute_enabled : R.string.setting_mute_disabled);
        StatusBarUtils.toggleStatusBar(SettingActivity.this, !isFullScreen);
        mTvFullScreenModeDesc.setText(isFullScreen ? R.string.setting_full_screen_enabled
                : R.string.setting_full_screen_disabled);
        setDifficultDesc(difficult);
    }

    @OnClick(R.id.rl_vibration)
    void vibrateClick() {
        mCbVibration.setChecked(!mCbVibration.isChecked());
    }

    @OnClick(R.id.rl_mute)
    void muteClick() {
        mCbMute.setChecked(!mCbMute.isChecked());
    }

    @OnClick(R.id.rl_full_screen)
    void fullScreenClick() {
        mCbFullscreenMode.setChecked(!mCbFullscreenMode.isChecked());
    }

    @OnClick(R.id.rl_size)
    void sizeClick() {
        Intent intent = new Intent();
        intent.setClass(mContext, CellSizeActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.rl_difficult)
    void difficultClick() {
        DialogManager.getInstance().showDifficultDialog(mContext, new SettingListener() {
            @Override
            public void onDismissListener() {
                int difficult = PreferenceUtils.getDifficultType(mContext);
                setDifficultDesc(difficult);
            }
        });
    }

    @OnClick(R.id.rl_rate_us)
    void rateUsClick() {
        Utils.openGooglePlayRating(mContext);
    }

    @OnClick(R.id.rl_tell_friend)
    void tellFriendClick() {
        tellFriends();
    }

    @OnCheckedChanged(R.id.cb_vibration)
    void cbVibrateToggled(boolean isChecked) {
        PreferenceUtils.setVibration(mContext, isChecked);
        mTvVibrationDesc.setText(isChecked ? R.string.setting_vibration_enabled
                : R.string.setting_vibration_disabled);
    }

    @OnCheckedChanged(R.id.cb_mute)
    void cbMuteToggled(boolean isChecked) {
        PreferenceUtils.setMute(mContext, isChecked);
        mTvMuteDesc.setText(isChecked ? R.string.setting_mute_enabled
                : R.string.setting_mute_disabled);
    }

    @OnCheckedChanged(R.id.cb_full_screen)
    void cbFullScreenToggled(boolean isChecked) {
        PreferenceUtils.setFullscreenMode(mContext, isChecked);
        StatusBarUtils.toggleStatusBar(SettingActivity.this, !isChecked);
        mTvFullScreenModeDesc.setText(isChecked ? R.string.setting_full_screen_enabled
                : R.string.setting_full_screen_disabled);
    }

    private void setDifficultDesc(int difficult) {
        if (difficult == Constant.DIFFICULT_EASY) {
            mTvDifficultDesc.setText(R.string.setting_difficult_easy);
        } else if (difficult == Constant.DIFFICULT_MEDIUM) {
            mTvDifficultDesc.setText(R.string.setting_difficult_medium);
        } else {
            mTvDifficultDesc.setText(R.string.setting_difficult_hard);
        }
    }

    private void tellFriends() {
        Utils.shareText(mContext, getResources().getString(R.string.tell_friend_content),
                getString(R.string.app_name));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setResult(Constant.RESULT_SETTING_CODE);
    }
}
