
package com.gpc.minesweeper.ui;

import com.blankj.utilcode.utils.SizeUtils;
import com.gpc.minesweeper.R;
import com.gpc.minesweeper.utils.Constant;
import com.gpc.minesweeper.utils.PreferenceUtils;
import com.gpc.minesweeper.utils.StatusBarUtils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by pcgu on 16-10-27.
 */

public class CellSizeActivity extends Activity {

    @BindView(R.id.sb_select_size)
    SeekBar mSbSelectCellSize;
    @BindView(R.id.cell_1)
    ImageView mImg1;
    @BindView(R.id.cell_2)
    ImageView mImg2;
    @BindView(R.id.cell_3)
    ImageView mImg3;
    @BindView(R.id.cell_4)
    ImageView mImg4;
    @BindView(R.id.cell_5)
    ImageView mImg5;
    @BindView(R.id.cell_6)
    ImageView mImg6;
    @BindView(R.id.cell_7)
    ImageView mImg7;
    @BindView(R.id.cell_8)
    ImageView mImg8;
    @BindView(R.id.cell_9)
    ImageView mImg9;

    private Context mContext;

    private float mCellWidth;
    private float mMaxCellWidth;
    private float mMinCellWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_cell_size);
        ButterKnife.bind(this);
        mContext = this;
        initViews();
        initClickListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @OnClick(R.id.btn_save)
    void saveClick() {
        PreferenceUtils.setCellSize(mContext, (int) mCellWidth);
        finish();
    }

    private void initViews() {
        boolean isFullScreen = PreferenceUtils.getFullscreenMode(mContext);
        StatusBarUtils.toggleStatusBar(CellSizeActivity.this, !isFullScreen);
        mCellWidth = PreferenceUtils.getCellSize(mContext);
        mMinCellWidth = SizeUtils.dp2px(mContext, Constant.CELL_SIZE_MIN);
        mMaxCellWidth = SizeUtils.dp2px(mContext, Constant.CELL_SIZE_MAX);
        int progress = (int) (((mCellWidth - mMinCellWidth) / (mMaxCellWidth - mMinCellWidth))
                * 100);
        mSbSelectCellSize.setProgress(progress);
        setCellsParams();
    }

    private void initClickListeners() {

        mSbSelectCellSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mCellWidth = (int) ((progress / 100.0f) * (mMaxCellWidth - mMinCellWidth)
                        + mMinCellWidth);
                setCellsParams();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private void setCellsParams() {
        setImageLayoutParams(mImg1);
        setImageLayoutParams(mImg2);
        setImageLayoutParams(mImg3);
        setImageLayoutParams(mImg4);
        setImageLayoutParams(mImg5);
        setImageLayoutParams(mImg6);
        setImageLayoutParams(mImg7);
        setImageLayoutParams(mImg8);
        setImageLayoutParams(mImg9);
    }

    private void setImageLayoutParams(ImageView img) {
        ViewGroup.LayoutParams params = img.getLayoutParams();
        params.width = (int) mCellWidth;
        params.height = (int) mCellWidth;
        img.setLayoutParams(params);
    }

}
