
package com.gpc.minesweeper.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.gpc.minesweeper.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by pcgu on 16-10-25.
 */

public class MainActivity extends Activity implements View.OnClickListener,
        View.OnLongClickListener, View.OnTouchListener {

    @BindView(R.id.gl_minesweeper)
    GridLayout mGlMinesweeper;

    @BindView(R.id.iv_play)
    ImageView mIvPlay;
    @BindView(R.id.iv_setting)
    ImageView mIvSetting;
    @BindView(R.id.iv_flag_mine)
    ImageView mIvFlagMine;

    @BindView(R.id.iv_bomb_hundred)
    ImageView mIvBombHundred;
    @BindView(R.id.iv_bomb_ten)
    ImageView mIvBombTen;
    @BindView(R.id.iv_bomb_bit)
    ImageView mIvBombBit;

    @BindView(R.id.iv_time_kilobit)
    ImageView mIvTimeKilo;
    @BindView(R.id.iv_time_hundred)
    ImageView mIvTimeHundred;
    @BindView(R.id.iv_time_ten)
    ImageView mIvTimeTen;
    @BindView(R.id.iv_time_bit)
    ImageView mIvTimeBit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @OnClick(R.id.iv_play)
    void playGame() {

    }

    private void initDrawables() {

    }
}
