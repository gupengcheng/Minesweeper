
package com.gpc.minesweeper.widget;

import android.content.Context;
import android.widget.ImageView;

import com.gpc.minesweeper.R;
import com.gpc.minesweeper.utils.DrawableManager;

/**
 * Created by pcgu on 16-10-26.
 */

public class CellImageView extends ImageView {

    private int mCellRow;
    private int mCellColumn;

    public CellImageView(Context context) {
        super(context);
    }

    public CellImageView(Context context, int cellRow, int cellColumn, int cellWidth,
            int cellHeight) {
        super(context);
        this.setCellRow(cellRow);
        this.setCellColumn(cellColumn);
        this.setScaleType(ScaleType.FIT_XY);
        this.setImageDrawable(R.mipmap.iv_empty);
        this.setMinimumWidth(cellWidth);
        this.setMaxWidth(cellWidth);
        this.setMinimumHeight(cellHeight);
        this.setMaxHeight(cellHeight);
    }

    public int getCellRow() {
        return mCellRow;
    }

    public void setCellRow(int cellRow) {
        mCellRow = cellRow;
    }

    public int getCellColumn() {
        return mCellColumn;
    }

    public void setCellColumn(int cellColumn) {
        mCellColumn = cellColumn;
    }

    public void setImageDrawable(int resId) {
        this.setImageDrawable(DrawableManager.getInstance().getDrawable(resId));
    }

}
