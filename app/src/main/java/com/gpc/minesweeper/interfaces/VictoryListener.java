
package com.gpc.minesweeper.interfaces;

/**
 * Created by pcgu on 16-10-27.
 */

public interface VictoryListener {
    void onOkClick();

    void onCancelClick();

    void onShareClick(boolean newRecord, int sec);
}
