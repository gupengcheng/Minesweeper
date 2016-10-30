
package com.gpc.minesweeper.ui;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.blankj.utilcode.utils.BarUtils;
import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ScreenUtils;
import com.blankj.utilcode.utils.SizeUtils;
import com.gpc.minesweeper.R;
import com.gpc.minesweeper.interfaces.RateUsListener;
import com.gpc.minesweeper.interfaces.VictoryListener;
import com.gpc.minesweeper.utils.Constant;
import com.gpc.minesweeper.utils.DialogManager;
import com.gpc.minesweeper.utils.DrawableManager;
import com.gpc.minesweeper.utils.GameSoundManager;
import com.gpc.minesweeper.utils.PreferenceUtils;
import com.gpc.minesweeper.utils.StatusBarUtils;
import com.gpc.minesweeper.utils.Utils;
import com.gpc.minesweeper.utils.VibratorManager;
import com.gpc.minesweeper.widget.CellImageView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    @BindView(R.id.iv_flag_bomb)
    ImageView mIvFlagBomb;

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

    private Context mContext;

    private Handler mPlayedTimeHandler;
    private int mPlayedTime = 0;

    private int mSoundIdClick;
    private int mSoundIdMark;
    private int mSoundIdVictory;
    private int mSoundIdFailed;

    private boolean mMarked = false;
    private boolean mStarted = false;
    private boolean mFullScreen;
    private int mGameState;
    private int mCellRealWidth;
    private int mCellRealHeight;

    // 打开的地雷方块总数
    private int mOpenCellNumbers = 0;
    // 参与计算的地雷总数
    private int mExistBombNumber;
    // 实际不变的地雷总数
    private int mRealBombNumber;
    private int mTotalRows;
    private int mTotalColumns;
    private int mTotalCells;
    private int mScreenOrientation;
    private int mCellSize;
    private int mDifficulty;

    private boolean[][] mBombArray;
    private boolean[][] mMarkArray;
    private boolean[][] mOpenArray;
    private int[][] mBombNumberArray;
    private CellImageView[][] mCellArray;

    private Timer mTimer;
    private TimerTask mTimerTask;

    private static final int TOP_VIEW_HEIGHT = 48;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;
        initPlayedTimeHandler();
        initGame();
    }

    @Override
    public void onClick(View v) {
        if (mGameState != Constant.STATE_PLAYING) {
            return;
        }

        CellImageView cellImageView = (CellImageView) v;

        startPlayedTimer();

        if (mMarked) {
            markStateClick(cellImageView);
        } else {
            normalStateClick(cellImageView);
        }
    }

    @Override
    public boolean onLongClick(View v) {

        if (mGameState != Constant.STATE_PLAYING) {
            return false;
        }
        startPlayedTimer();
        CellImageView cellImageView = (CellImageView) v;
        int currentRow = cellImageView.getCellRow();
        int currentColumn = cellImageView.getCellColumn();
        if (mOpenArray[currentRow][currentColumn]) {
            return true;
        }
        if (mMarkArray[currentRow][currentColumn]) {
            mExistBombNumber++;
        } else {
            // 如果已经没有可以标记的炸弹数量，则不能再标记
            if (mExistBombNumber <= 0) {
                return true;
            }
            mExistBombNumber--;
        }
        showBombNumberView();
        VibratorManager.getInstance().vibrator();
        GameSoundManager.getInstance().playingSound(mSoundIdMark);
        mMarkArray[currentRow][currentColumn] = !mMarkArray[currentRow][currentColumn];
        cellImageView.setImageDrawable(
                mMarkArray[currentRow][currentColumn] ? R.mipmap.iv_flag : R.mipmap.iv_button);
        return true;

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mGameState != Constant.STATE_PLAYING) {
            return false;
        }

        int action = event.getAction();
        CellImageView cellImageView = (CellImageView) v;
        int currentRow = cellImageView.getCellRow();
        int currentColumn = cellImageView.getCellColumn();

        if (!mOpenArray[currentRow][currentColumn]) {
            return false;
        }

        if (mBombNumberArray[currentRow][currentColumn] == 0) {
            return false;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                performTouchAround(currentRow, currentColumn);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                resetTouchAround(currentRow, currentColumn);
                break;
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mGameState != Constant.STATE_PLAYING) {
                finish();
                return true;
            }

            if (!userPlayed()) {
                finish();
                return true;
            }

            saveRestoreGameData();
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int currentOrientation = getResources().getConfiguration().orientation;

        if (mScreenOrientation == currentOrientation) {
            return;
        }

        mScreenOrientation = currentOrientation;

        // 如果没有打开过，直接走重新Play的逻辑
        if (!userPlayed()) {
            newGame();
            return;
        }

        reverseRowColumns();
        reverseArrays();
        restoreGlMinesweeper();
        restoreView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_SETTING_CODE) {

            setFullScreenMode();

            int difficulty = PreferenceUtils.getDifficultType(mContext);
            if (mDifficulty != difficulty) {
                newGame();
            }

            int cellSize = PreferenceUtils.getCellSize(mContext);
            if (mCellSize != cellSize) {
                newGame();
            }
        }
    }

    @OnClick(R.id.iv_play)
    void playGame() {
        if (mStarted) {
            newGame();
        }
    }

    @OnClick(R.id.iv_setting)
    void setting() {
        Intent intent = new Intent();
        intent.setClass(mContext, SettingActivity.class);
        startActivityForResult(intent, Constant.REQUEST_SETTING_CODE);
    }

    @OnClick(R.id.iv_flag_bomb)
    void flagBomb() {
        mMarked = !mMarked;
        mIvFlagBomb.setImageResource(
                mMarked ? R.mipmap.iv_switch_flag : R.mipmap.iv_switch_bomb);
    }

    private void setFullScreenMode() {
        boolean fullScreen = PreferenceUtils.getFullscreenMode(mContext);
        LogUtils.e("fullScreen = " + fullScreen + " mFullScreen = " + mFullScreen);
        if (fullScreen == mFullScreen) {
            return;
        }
        mFullScreen = fullScreen;
        StatusBarUtils.toggleStatusBar(this, !mFullScreen);
        mCellRealHeight = getGlHeight() / mTotalRows;
        mCellRealWidth = getGlWidth() / mTotalColumns;
        validateGlView();
    }

    private void validateGlView() {
        for (int i = 0; i < mGlMinesweeper.getRowCount(); i++) {
            for (int j = 0; j < mGlMinesweeper.getColumnCount(); j++) {
                View view = mGlMinesweeper.getChildAt(i * mTotalColumns + j);
                if (view == null) {
                    continue;
                }
                GridLayout.LayoutParams gllp = new GridLayout.LayoutParams(
                        GridLayout.spec(i), GridLayout.spec(j));
                gllp.width = mCellRealWidth;
                gllp.height = mCellRealHeight;
                CellImageView civ = (CellImageView) view;
                civ.setLayoutParams(gllp);
            }
        }
    }

    private void performTouchAround(int currentRow, int currentColumn) {
        int bombSize = mBombNumberArray[currentRow][currentColumn];
        if (bombSize == Constant.BOMB_AREA_NUMBER) {
            return;
        }
        int markedSize = getAroundMarkedSize(currentRow, currentColumn);
        if (markedSize >= bombSize) {
            openBombAroundCell(currentRow, currentColumn);
        } else {
            performPressed(currentRow, currentColumn);
        }
    }

    private void resetTouchAround(int currentRow, int currentColumn) {
        int bombSize = mBombNumberArray[currentRow][currentColumn];
        if (bombSize == Constant.BOMB_AREA_NUMBER) {
            return;
        }
        int markedSize = getAroundMarkedSize(currentRow, currentColumn);
        if (markedSize < bombSize) {
            performUnPressed(currentRow, currentColumn);
        }
    }

    private void performPressed(int currentRow, int currentColumn) {
        performCurrentPressed(currentRow, currentColumn - 1);
        performCurrentPressed(currentRow, currentColumn + 1);
        performCurrentPressed(currentRow - 1, currentColumn - 1);
        performCurrentPressed(currentRow - 1, currentColumn);
        performCurrentPressed(currentRow - 1, currentColumn + 1);
        performCurrentPressed(currentRow + 1, currentColumn - 1);
        performCurrentPressed(currentRow + 1, currentColumn);
        performCurrentPressed(currentRow + 1, currentColumn + 1);
    }

    private void performCurrentPressed(int currentRow, int currentColumn) {
        if (currentRow > -1 && currentRow < mTotalRows && currentColumn > -1
                && currentColumn < mTotalColumns) {
            if (mMarkArray[currentRow][currentColumn] || mOpenArray[currentRow][currentColumn]) {
                return;
            }
            mCellArray[currentRow][currentColumn].setImageDrawable(R.mipmap.iv_empty);
        }
    }

    private void performUnPressed(int currentRow, int currentColumn) {
        performCurrentUnPressed(currentRow, currentColumn - 1);
        performCurrentUnPressed(currentRow, currentColumn + 1);
        performCurrentUnPressed(currentRow - 1, currentColumn - 1);
        performCurrentUnPressed(currentRow - 1, currentColumn);
        performCurrentUnPressed(currentRow - 1, currentColumn + 1);
        performCurrentUnPressed(currentRow + 1, currentColumn - 1);
        performCurrentUnPressed(currentRow + 1, currentColumn);
        performCurrentUnPressed(currentRow + 1, currentColumn + 1);
    }

    private void performCurrentUnPressed(int currentRow, int currentColumn) {
        if (currentRow > -1 && currentRow < mTotalRows && currentColumn > -1
                && currentColumn < mTotalColumns) {
            if (mMarkArray[currentRow][currentColumn] || mOpenArray[currentRow][currentColumn]) {
                return;
            }
            mCellArray[currentRow][currentColumn].setImageDrawable(R.mipmap.iv_button);
        }
    }

    private int getAroundMarkedSize(int currentRow, int currentColumn) {

        int markedCell = 0;
        markedCell = markedCell + (imageCellMarked(currentRow, currentColumn - 1) ? 1 : 0);
        markedCell = markedCell + (imageCellMarked(currentRow, currentColumn + 1) ? 1 : 0);
        markedCell = markedCell + (imageCellMarked(currentRow - 1, currentColumn - 1) ? 1 : 0);
        markedCell = markedCell + (imageCellMarked(currentRow - 1, currentColumn) ? 1 : 0);
        markedCell = markedCell + (imageCellMarked(currentRow - 1, currentColumn + 1) ? 1 : 0);
        markedCell = markedCell + (imageCellMarked(currentRow + 1, currentColumn - 1) ? 1 : 0);
        markedCell = markedCell + (imageCellMarked(currentRow + 1, currentColumn) ? 1 : 0);
        markedCell = markedCell + (imageCellMarked(currentRow + 1, currentColumn + 1) ? 1 : 0);

        return markedCell;
    }

    private boolean imageCellMarked(int currentRow, int currentColumn) {
        if (currentRow > -1 && currentRow < mTotalRows && currentColumn > -1
                && currentColumn < mTotalColumns) {
            if (mMarkArray[currentRow][currentColumn]) {
                return true;
            }
        }
        return false;
    }

    private void openBombAroundCell(int currentRow, int currentColumn) {
        openCurrentCell(currentRow, currentColumn - 1);
        openCurrentCell(currentRow, currentColumn + 1);
        openCurrentCell(currentRow - 1, currentColumn - 1);
        openCurrentCell(currentRow - 1, currentColumn);
        openCurrentCell(currentRow - 1, currentColumn + 1);
        openCurrentCell(currentRow + 1, currentColumn - 1);
        openCurrentCell(currentRow + 1, currentColumn);
        openCurrentCell(currentRow + 1, currentColumn + 1);
    }

    private void openCurrentCell(int currentRow, int currentColumn) {
        if (mGameState != Constant.STATE_PLAYING) {
            return;
        }
        if (currentRow > -1 && currentRow < mTotalRows && currentColumn > -1
                && currentColumn < mTotalColumns) {
            if (mOpenArray[currentRow][currentColumn]) {
                return;
            }
            if (mMarkArray[currentRow][currentColumn]) {
                return;
            }
            if (!mMarkArray[currentRow][currentColumn]) {
                if (!mBombArray[currentRow][currentColumn]) {
                    GameSoundManager.getInstance().playingSound(mSoundIdClick);
                    openNext(currentRow, currentColumn);
                } else {
                    mOpenArray[currentRow][currentColumn] = true;
                    GameSoundManager.getInstance().playingSound(mSoundIdFailed);
                    gameOver(currentRow, currentColumn);
                    clearRestoreGameData();
                }
            }
        }
    }

    private void markStateClick(CellImageView cellImageView) {
        int cellRow = cellImageView.getCellRow();
        int cellColumn = cellImageView.getCellColumn();
        if (mOpenArray[cellRow][cellColumn]) {
            return;
        }
        if (mMarkArray[cellRow][cellColumn]) {
            mMarkArray[cellRow][cellColumn] = false;
            mExistBombNumber++;
        } else {
            //// 如果已经没有可以标记的炸弹数量，则不能再标记
            if (mExistBombNumber <= 0) {
                return;
            }
            mMarkArray[cellRow][cellColumn] = true;
            mExistBombNumber--;
        }
        GameSoundManager.getInstance().playingSound(mSoundIdMark);
        showBombNumberView();
        cellImageView.setImageDrawable(
                mMarkArray[cellRow][cellColumn] ? R.mipmap.iv_flag : R.mipmap.iv_button);
    }

    private void normalStateClick(CellImageView cellImageView) {
        int cellRow = cellImageView.getCellRow();
        int cellColumn = cellImageView.getCellColumn();
        if (mOpenArray[cellRow][cellColumn] || mMarkArray[cellRow][cellColumn]) {
            return;
        }
        if (!mBombArray[cellRow][cellColumn]) {
            GameSoundManager.getInstance().playingSound(mSoundIdClick);
            openNext(cellRow, cellColumn);
        } else {
            mOpenArray[cellRow][cellColumn] = true;
            GameSoundManager.getInstance().playingSound(mSoundIdFailed);
            gameOver(cellRow, cellColumn);
            clearRestoreGameData();
        }
    }

    private void openNext(int cellRow, int cellColumn) {
        mOpenArray[cellRow][cellColumn] = true;
        mCellArray[cellRow][cellColumn].setImageDrawable(DrawableManager.getInstance()
                .getCellNumberResourceId(mBombNumberArray[cellRow][cellColumn]));
        mOpenCellNumbers++;
        // 如果炸弹数量为0，则可以打开周围的位置，以作为边界结束
        if (mBombNumberArray[cellRow][cellColumn] == 0) {
            openAround(cellRow, cellColumn);
        }
        if (mOpenCellNumbers + mRealBombNumber == mTotalCells) {
            gameVictory(cellRow, cellColumn);
            clearRestoreGameData();
        }
    }

    // 打开周围的位置，以边界作为结束
    private void openAround(int row, int column) {
        if (column - 1 >= 0)
            if (!mBombArray[row][column - 1] && !mOpenArray[row][column - 1]
                    && !mMarkArray[row][column - 1])
                openNext(row, column - 1);
        if (column + 1 < mOpenArray[row].length)
            if (!mBombArray[row][column + 1] && !mOpenArray[row][column + 1]
                    && !mMarkArray[row][column + 1])
                openNext(row, column + 1);
        if (row - 1 >= 0) {
            if (!mBombArray[row - 1][column] && !mOpenArray[row - 1][column]
                    && !mMarkArray[row - 1][column])
                openNext(row - 1, column);
            if (column - 1 >= 0)
                if (!mBombArray[row - 1][column - 1] && !mOpenArray[row - 1][column - 1]
                        && !mMarkArray[row - 1][column - 1])
                    openNext(row - 1, column - 1);
            if (column + 1 < mOpenArray[row].length)
                if (!mBombArray[row - 1][column + 1] && !mOpenArray[row - 1][column + 1]
                        && !mMarkArray[row - 1][column + 1])
                    openNext(row - 1, column + 1);
        }
        if (row + 1 < mOpenArray.length) {
            if (!mBombArray[row + 1][column] && !mOpenArray[row + 1][column]
                    && !mMarkArray[row + 1][column])
                openNext(row + 1, column);
            if (column - 1 >= 0)
                if (!mBombArray[row + 1][column - 1] && !mOpenArray[row + 1][column - 1]
                        && !mMarkArray[row + 1][column - 1])
                    openNext(row + 1, column - 1);
            if (column + 1 < mOpenArray[row].length)
                if (!mBombArray[row + 1][column + 1] && !mOpenArray[row + 1][column + 1]
                        && !mMarkArray[row + 1][column + 1])
                    openNext(row + 1, column + 1);
        }
    }

    private void gameVictory(int cellRow, int cellColumn) {
        if (mGameState == Constant.STATE_WIN) {
            return;
        }
        mGameState = Constant.STATE_WIN;
        GameSoundManager.getInstance().playingSound(mSoundIdVictory);
        showVictoryDialog();
        closePlayedTimer();
    }

    private void gameOver(int cellRow, int cellColumn) {
        for (int i = 0; i < mCellArray.length; i++) {
            for (int j = 0; j < mCellArray[i].length; j++) {
                // 设置当前用户点击的炸弹
                if (i == cellRow && j == cellColumn) {
                    mCellArray[i][j].setImageDrawable(R.mipmap.iv_rmine);
                    continue;
                }
                if (mBombArray[i][j] && !mMarkArray[i][j]) {
                    mCellArray[i][j].setImageDrawable(R.mipmap.iv_gmine);
                } else if (mMarkArray[i][j]) {
                    if (mBombArray[i][j]) {
                        mCellArray[i][j].setImageDrawable(R.mipmap.iv_flag);
                    } else {
                        mCellArray[i][j].setImageDrawable(R.mipmap.iv_wrflag);
                    }
                } else {
                    mCellArray[i][j].setImageDrawable(DrawableManager.getInstance()
                            .getCellNumberResourceId(mBombNumberArray[i][j]));
                }
            }
        }
        setGameOverState();
    }

    private void setGameOverState() {
        mGameState = Constant.STATE_LOST;
        mIvPlay.setImageResource(R.mipmap.iv_sorrow);
        closePlayedTimer();
    }

    private void initPlayedTimeHandler() {
        mPlayedTimeHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                setPlayedTime();
            }
        };
    }

    private void setPlayedTime() {
        int minute = mPlayedTime / 60;
        int second = mPlayedTime % 60;

        int tenMinute = minute / 10;
        int bitMinute = minute % 10;

        int tenSecond = second / 10;
        int bitSecond = second % 10;

        mIvTimeKilo.setImageDrawable(DrawableManager.getInstance()
                .getDrawable(DrawableManager.getInstance().getTimeNumberResourceId(tenMinute)));
        mIvTimeHundred.setImageDrawable(DrawableManager.getInstance()
                .getDrawable(DrawableManager.getInstance().getTimeNumberResourceId(bitMinute)));
        mIvTimeTen.setImageDrawable(DrawableManager.getInstance()
                .getDrawable(DrawableManager.getInstance().getTimeNumberResourceId(tenSecond)));
        mIvTimeBit.setImageDrawable(DrawableManager.getInstance()
                .getDrawable(DrawableManager.getInstance().getTimeNumberResourceId(bitSecond)));
    }

    private void initGame() {
        initGameSound();
        initGameState();
    }

    private void initGameSound() {
        mSoundIdClick = GameSoundManager.getInstance().getSoundId(R.raw.click);
        mSoundIdMark = GameSoundManager.getInstance().getSoundId(R.raw.mark);
        mSoundIdFailed = GameSoundManager.getInstance().getSoundId(R.raw.failed);
        mSoundIdVictory = GameSoundManager.getInstance().getSoundId(R.raw.vctory);
    }

    private void initOrientation() {
        mScreenOrientation = getCurrentOrientation();
    }

    private int getCurrentOrientation() {
        return getResources().getConfiguration().orientation;
    }

    private void initGameState() {
        if (PreferenceUtils.isRestoreGame(mContext)) {
            restoreGame();
        } else {
            newGame();
        }
    }

    private void restoreGame() {
        getRestoreGameData();
        // 如果数据不合法，重新新建游戏
        if (!restoreDataLegal()) {
            newGame();
            return;
        }
        mStarted = true;
        initCommonGameState();
        setStatusBar();
        initMarkedImageView();
        if (mScreenOrientation != getCurrentOrientation()) {
            reverseArrays();
            reverseRowColumns();
        }
        restoreGlMinesweeper();
        restoreView();
    }

    private void newGame() {
        resetNewGameState();
        initOrientation();
        setStatusBar();
        initMarkedImageView();
        initGridMinesweeper();
        initBombNumber();
        closePlayedTimer();
        resetPlayedTime();
    }

    private void saveRestoreGameData() {
        PreferenceUtils.setPlayedTime(mContext, mPlayedTime);
        PreferenceUtils.setExistBombNumber(mContext, mExistBombNumber);
        PreferenceUtils.setRealBombNumber(mContext, mRealBombNumber);
        PreferenceUtils.setTotalRows(mContext, mTotalRows);
        PreferenceUtils.setTotalColumns(mContext, mTotalColumns);
        PreferenceUtils.setOpenNumbers(mContext, mOpenCellNumbers);
        PreferenceUtils.setBombsArray(mContext, mBombArray);
        PreferenceUtils.setMarksArray(mContext, mMarkArray);
        PreferenceUtils.setOpensArray(mContext, mOpenArray);
        PreferenceUtils.setBombNumberArray(mContext, mBombNumberArray);
        PreferenceUtils.setScreenOrientation(mContext, mScreenOrientation);
        PreferenceUtils.setMarkedState(mContext, mMarked);
        PreferenceUtils.setRestoreGame(mContext, true);
    }

    private void getRestoreGameData() {
        mPlayedTime = PreferenceUtils.getPlayedTime(mContext);
        mExistBombNumber = PreferenceUtils.getExistBombNumber(mContext);
        mRealBombNumber = PreferenceUtils.getRealBombNumber(mContext);
        mTotalRows = PreferenceUtils.getTotalRows(mContext);
        mTotalColumns = PreferenceUtils.getTotalColumns(mContext);
        mTotalCells = mTotalRows * mTotalColumns;
        mOpenCellNumbers = PreferenceUtils.getOpenNumbers(mContext);
        mBombArray = PreferenceUtils.getBombsArray(mContext);
        mMarkArray = PreferenceUtils.getMarksArray(mContext);
        mOpenArray = PreferenceUtils.getOpensArray(mContext);
        mBombNumberArray = PreferenceUtils.getBombNumberArray(mContext);
        mScreenOrientation = PreferenceUtils.getScreenOrientation(mContext);
        mMarked = PreferenceUtils.getMarkedState(mContext);
    }

    private void clearRestoreGameData() {
        if (!PreferenceUtils.isRestoreGame(this)) {
            return;
        }
        PreferenceUtils.setPlayedTime(this, 0);
        PreferenceUtils.setExistBombNumber(this, 0);
        PreferenceUtils.setRealBombNumber(this, 0);
        PreferenceUtils.setTotalRows(this, 0);
        PreferenceUtils.setTotalColumns(this, 0);
        PreferenceUtils.setOpenNumbers(this, 0);
        PreferenceUtils.setBombsArray(this, null);
        PreferenceUtils.setMarksArray(this, null);
        PreferenceUtils.setOpensArray(this, null);
        PreferenceUtils.setBombNumberArray(this, null);
        PreferenceUtils.setScreenOrientation(this, 1);
        PreferenceUtils.setMarkedState(this, mMarked);
        PreferenceUtils.setRestoreGame(this, false);
    }

    private void initCommonGameState() {
        mGameState = Constant.STATE_PLAYING;
        mIvPlay.setImageResource(R.mipmap.iv_smile);

        mFullScreen = PreferenceUtils.getFullscreenMode(mContext);
        mCellSize = PreferenceUtils.getCellSize(mContext);
        mDifficulty = PreferenceUtils.getDifficultType(mContext);

        mCellRealWidth = mCellSize;
        mCellRealHeight = mCellSize;
    }

    private void resetNewGameState() {
        mOpenCellNumbers = 0;
        mStarted = false;
        initCommonGameState();
    }

    private boolean restoreDataLegal() {
        if (mBombArray == null || mMarkArray == null || mOpenArray == null
                || mBombNumberArray == null) {
            return false;
        }
        return true;
    }

    private void restoreGlMinesweeper() {
        mGlMinesweeper.removeAllViews();

        int glWidth = getGlWidth();
        int glHeight = getGlHeight();

        mCellRealWidth = glWidth / mTotalColumns;
        mCellRealHeight = glHeight / mTotalRows;
        mCellArray = new CellImageView[mTotalRows][mTotalColumns];

        initGlView();
    }

    private void initGridMinesweeper() {
        mGlMinesweeper.removeAllViews();

        int glWidth = getGlWidth();
        int glHeight = getGlHeight();
        int glRows = glHeight / mCellRealHeight;
        int glColumns = glWidth / mCellRealWidth;

        LogUtils.e("store mCellRealWidth = " + mCellRealWidth + " mCellRealHeight = "
                + mCellRealHeight);
        // 防止不能整除的情况下，出现多余的空间没有分配，尽量减少这种情况发生
        mCellRealWidth = glWidth / glColumns;
        mCellRealHeight = glHeight / glRows;
        LogUtils.e("final mCellRealWidth = " + mCellRealWidth + " mCellRealHeight = "
                + mCellRealHeight);

        initArrays(glRows, glColumns);
        initGlView();
    }

    private void initMarkedImageView() {
        mIvFlagBomb.setImageResource(
                mMarked ? R.mipmap.iv_switch_flag : R.mipmap.iv_switch_bomb);
    }

    private int getGlWidth() {
        int screenWidth = ScreenUtils.getScreenWidth(mContext);
        return screenWidth;
    }

    private int getGlHeight() {
        int topViewHeight = SizeUtils.dp2px(mContext, TOP_VIEW_HEIGHT);
        int screenHeight = ScreenUtils.getScreenHeight(mContext);
        if (mFullScreen) {
            return screenHeight - topViewHeight;
        }
        int statusBarHeight = BarUtils.getStatusBarHeight(mContext);
        return screenHeight - statusBarHeight - topViewHeight;
    }

    private void initArrays(int totalRows, int totalColumns) {
        mTotalRows = totalRows;
        mTotalColumns = totalColumns;
        mTotalCells = totalRows * totalColumns;
        mBombArray = new boolean[totalRows][totalColumns];
        mMarkArray = new boolean[totalRows][totalColumns];
        mOpenArray = new boolean[totalRows][totalColumns];
        mBombNumberArray = new int[totalRows][totalColumns];
        mCellArray = new CellImageView[totalRows][totalColumns];
    }

    private void initGlView() {
        mGlMinesweeper.setRowCount(mTotalRows);
        mGlMinesweeper.setColumnCount(mTotalColumns);
        for (int i = 0; i < mGlMinesweeper.getRowCount(); i++) {
            for (int j = 0; j < mGlMinesweeper.getColumnCount(); j++) {
                GridLayout.LayoutParams glParams = new GridLayout.LayoutParams(
                        GridLayout.spec(i), GridLayout.spec(j));
                glParams.width = mCellRealWidth;
                glParams.height = mCellRealHeight;
                CellImageView civ = new CellImageView(MainActivity.this, i, j, mCellRealWidth,
                        mCellRealHeight);
                civ.setLayoutParams(glParams);
                civ.setOnClickListener(this);
                civ.setOnLongClickListener(this);
                civ.setOnTouchListener(this);
                // 关闭系统按键发出的声音
                civ.setHapticFeedbackEnabled(false);
                mCellArray[i][j] = civ;
                mGlMinesweeper.addView(civ);
            }
        }
    }

    private void setStatusBar() {
        StatusBarUtils.toggleStatusBar(MainActivity.this, !mFullScreen);
    }

    private void initBombNumber() {
        resetBombNumber();
        showBombNumberView();
        resetBombArray();
    }

    private void resetBombNumber() {
        if (mDifficulty == Constant.DIFFICULT_EASY) {
            mExistBombNumber = (int) Math.floor(mTotalCells * Constant.DIFFICULT_LEVEL[0]);
        } else if (mDifficulty == Constant.DIFFICULT_MEDIUM) {
            mExistBombNumber = (int) Math.floor(mTotalCells * Constant.DIFFICULT_LEVEL[1]);
        } else {
            mExistBombNumber = (int) Math.floor(mTotalCells * Constant.DIFFICULT_LEVEL[2]);
        }
        mRealBombNumber = mExistBombNumber;
    }

    private void showBombNumberView() {
        int hundredBomb = mExistBombNumber / 100;
        int tenBomb = (mExistBombNumber - hundredBomb * 100) / 10;
        int bitBomb = mExistBombNumber % 10;

        mIvBombHundred.setImageDrawable(DrawableManager.getInstance()
                .getDrawable(DrawableManager.getInstance().getTimeNumberResourceId(hundredBomb)));
        mIvBombTen.setImageDrawable(DrawableManager.getInstance()
                .getDrawable(DrawableManager.getInstance().getTimeNumberResourceId(tenBomb)));
        mIvBombBit.setImageDrawable(DrawableManager.getInstance()
                .getDrawable(DrawableManager.getInstance().getTimeNumberResourceId(bitBomb)));
    }

    private void resetBombArray() {
        Random random = new Random();
        int i = 0;
        while (i < mRealBombNumber) {
            // 随机生成安放炸弹的坐标位置
            int row = random.nextInt(mTotalRows);
            int column = random.nextInt(mTotalColumns);
            // 如果还没有安放炸弹，此处可以安放
            if (!mBombArray[row][column]) {
                i++;
                mBombArray[row][column] = true;
                // 炸弹的位置设为 BOMB_AREA_NUMBER，标识此处为炸弹，不需要计数
                mBombNumberArray[row][column] = Constant.BOMB_AREA_NUMBER;
                // 周围8个相邻地方格子
                increaseBombNumber(row - 1, column - 1);
                increaseBombNumber(row - 1, column);
                increaseBombNumber(row - 1, column + 1);
                increaseBombNumber(row, column + 1);
                increaseBombNumber(row + 1, column + 1);
                increaseBombNumber(row + 1, column);
                increaseBombNumber(row + 1, column - 1);
                increaseBombNumber(row, column - 1);
            }
        }
    }

    private void increaseBombNumber(int currentRow, int currentColumn) {
        if (currentRow > -1 && currentRow < mTotalRows && currentColumn > -1
                && currentColumn < mTotalColumns) {
            if (mBombNumberArray[currentRow][currentColumn] != Constant.BOMB_AREA_NUMBER) {
                mBombNumberArray[currentRow][currentColumn]++;
            }
        }
    }

    private void startPlayedTimer() {
        if (!mStarted) {
            mStarted = true;
            openPlayedTimer();
        }
    }

    private void openPlayedTimer() {
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                mPlayedTime++;
                mPlayedTimeHandler.sendEmptyMessage(1);
            }
        };
        mTimer.schedule(mTimerTask, 0, 1000);
    }

    private void closePlayedTimer() {
        if (mTimer != null || mTimerTask != null) {
            mTimer.cancel();
            mTimerTask.cancel();
        }
    }

    private void resetPlayedTime() {
        mPlayedTime = 0;
        setPlayedTime();
    }

    private void showVictoryDialog() {
        DialogManager.getInstance().showVictoryDialog(this, new VictoryListener() {
            @Override
            public void onOkClick() {
                newGame();
                rateUs();
            }

            @Override
            public void onCancelClick() {
                rateUs();
            }

            @Override
            public void onShareClick(boolean newRecord, int sec) {
                shareRecord(newRecord, sec);
            }
        }, mPlayedTime);
    }

    private void rateUs() {
        if (PreferenceUtils.getShowRateUs(mContext)) {
            showRateUsDialog();
            PreferenceUtils.setShowRateUs(mContext, false);
        }
    }

    private void showRateUsDialog() {
        DialogManager.getInstance().showRateUsDialog(mContext, new RateUsListener() {
            @Override
            public void onOkClick() {
                Utils.openGooglePlayRating(mContext);
            }

            @Override
            public void onCancelClick() {
            }
        });
    }

    private void shareRecord(boolean newRecord, int sec) {
        if (newRecord) {
            Utils.shareText(mContext, String.format(
                    getResources().getString(R.string.share_new_record),
                    String.valueOf(sec)), getString(R.string.app_name));
        } else {
            Utils.shareText(MainActivity.this, String.format(
                    getResources().getString(R.string.share_normal), String.valueOf(sec)),
                    getString(R.string.app_name));
        }
    }

    private boolean userPlayed() {
        boolean hasPlayed = false;

        for (int i = 0; i < mOpenArray.length; i++) {
            for (int j = 0; j < mOpenArray[i].length; j++) {
                if (mOpenArray[i][j]) {
                    hasPlayed = true;
                    i = mOpenArray.length - 1;
                    j = mOpenArray[i].length - 1;
                }
            }
        }

        for (int i = 0; i < mMarkArray.length; i++) {
            for (int j = 0; j < mMarkArray[i].length; j++) {
                if (mMarkArray[i][j]) {
                    hasPlayed = true;
                    i = mMarkArray.length - 1;
                    j = mMarkArray[i].length - 1;
                }
            }
        }

        return hasPlayed;
    }

    private void reverseRowColumns() {
        int row = mTotalRows;
        int columns = mTotalColumns;
        mTotalRows = columns;
        mTotalColumns = row;
    }

    private void reverseArrays() {
        mBombArray = Utils.reverseArray(mBombArray);
        mMarkArray = Utils.reverseArray(mMarkArray);
        mOpenArray = Utils.reverseArray(mOpenArray);
        mBombNumberArray = Utils.reverseArray(mBombNumberArray);
    }

    private void restoreView() {
        for (int i = 0; i < mOpenArray.length; i++) {
            for (int j = 0; j < mOpenArray[i].length; j++) {
                if (!mOpenArray[i][j] && !mMarkArray[i][j]) {
                    continue;
                }
                if (mMarkArray[i][j]) {
                    mCellArray[i][j].setImageDrawable(R.mipmap.iv_flag);
                } else {
                    mCellArray[i][j].setImageDrawable(DrawableManager.getInstance()
                            .getCellNumberResourceId(mBombNumberArray[i][j]));
                }
            }
        }
    }

}
