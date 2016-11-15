
对于很多没有接触过游戏开发的同学来说，开发一款游戏似乎无从下手，总想到可能要学这个框架那个引擎，但是却忽略了其实用Android原生的控件也能开发一款有趣的小游戏，本篇文章就给大家详细介绍如何使用原生控件做出一款Google play上下载量达百万级的游戏——Minesweeper（扫雷）。

### 首先看看我们想要的界面效果

![device-2016-11-15-221523.png](http://upload-images.jianshu.io/upload_images/1761049-ad0ae66b6bd43f0d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/400)

这个界面看起来很复杂，其实并不难，我们可以把它拆分成两个部分，上面就是一个组合布局，下面的方块用一个网格布局GridLayout来实现，游戏的初始界面就实现了。
```
<merge
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <RelativeLayout
        android:layout_width="match_parent"
        android:layout_height="48dp"
        android:background="@drawable/ic_background"
        android:gravity="center_vertical">
        <ImageView
            android:id="@+id/iv_play"
            android:layout_width="@dimen/top_item_width"
            android:layout_height="@dimen/top_item_height"
            android:layout_centerHorizontal="true"/>
        <RelativeLayout
            android:id="@+id/rl_bomb"
            android:layout_width="wrap_content"
            android:layout_height="@dimen/top_time_height"
            android:layout_alignParentLeft="true"
            android:layout_marginLeft="8dp"
            android:background="@android:color/black"
            android:gravity="center_vertical">
            <ImageView
                android:id="@+id/iv_bomb_hundred"
                android:layout_width="@dimen/top_time_width"
                android:layout_height="@dimen/top_time_height"
                android:layout_alignParentLeft="true"/>
            <ImageView
                android:id="@+id/iv_bomb_ten"
                android:layout_width="@dimen/top_time_width"
                android:layout_height="@dimen/top_time_height"
                android:layout_toRightOf="@id/iv_bomb_hundred"/>
            <ImageView
                android:id="@+id/iv_bomb_bit"
                android:layout_width="@dimen/top_time_width"
                android:layout_height="@dimen/top_time_height"
                android:layout_toRightOf="@id/iv_bomb_ten"/>
        </RelativeLayout>
        <ImageView
            android:id="@+id/iv_setting"
            android:layout_width="@dimen/top_item_width"
            android:layout_height="@dimen/top_item_height"
            android:layout_marginLeft="16dp"
            android:layout_toRightOf="@id/rl_bomb"
            android:src="@mipmap/iv_setting"/>
        <RelativeLayout
            android:id="@+id/rl_time"
            android:layout_width="89dp"
            android:layout_height="@dimen/top_time_height"
            android:layout_alignParentRight="true"
            android:layout_marginRight="8dp"
            android:background="@android:color/black"
            android:gravity="center_vertical">
            <ImageView
                android:id="@+id/iv_time_bit"
                android:layout_width="@dimen/top_time_width"
                android:layout_height="@dimen/top_time_height"
                android:layout_alignParentRight="true"/>
            <ImageView
                android:id="@+id/iv_time_ten"
                android:layout_width="@dimen/top_time_width"
                android:layout_height="@dimen/top_time_height"
                android:layout_toLeftOf="@id/iv_time_bit"/>
            <ImageView
                android:id="@+id/iv_time_czz"
                android:layout_width="9dp"
                android:layout_height="@dimen/top_time_height"
                android:layout_toLeftOf="@id/iv_time_ten"
                android:src="@mipmap/iv_czz"/>
            <ImageView
                android:id="@+id/iv_time_hundred"
                android:layout_width="@dimen/top_time_width"
                android:layout_height="@dimen/top_time_height"
                android:layout_toLeftOf="@id/iv_time_czz"/>
            <ImageView
                android:id="@+id/iv_time_kilobit"
                android:layout_width="@dimen/top_time_width"
                android:layout_height="@dimen/top_time_height"
                android:layout_toLeftOf="@id/iv_time_hundred"/>
        </RelativeLayout>
        <ImageView
            android:id="@+id/iv_flag_bomb"
            android:layout_width="@dimen/top_item_width"
            android:layout_height="@dimen/top_item_height"
            android:layout_marginRight="16dp"
            android:layout_toLeftOf="@id/rl_time"
            android:src="@mipmap/iv_switch_flag"/>
    </RelativeLayout>
    <GridLayout
        android:id="@+id/gl_minesweeper"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_marginTop="48dp"/>
</merge>
```

```
//初始化GridLayout
private void initGlView() {
    mGlMinesweeper.setRowCount(mTotalRows);
    mGlMinesweeper.setColumnCount(mTotalColumns);
    for (int i = 0; i < mGlMinesweeper.getRowCount(); i++) {
        for (int j = 0; j < mGlMinesweeper.getColumnCount(); j++) {
            GridLayout.LayoutParams glParams = new GridLayout.LayoutParams(
                    GridLayout.spec(i), GridLayout.spec(j));
            glParams.width = mCellRealWidth;
            glParams.height = mCellRealHeight;
            CellImageView civ = new CellImageView(MainActivity.this, i, j,mCellRealWidth,
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
```

### 然后我们看看还需要什么


![device-2016-11-15-232413.png](http://upload-images.jianshu.io/upload_images/1761049-1d35962551b20612.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/400)

玩过扫雷的都知道，一场游戏有固定的地雷数，方块下地雷周围也会显示附近的地雷数目，一个成熟的地雷游戏还应该是可以根据方块数目来动态生成地雷数目，这又要怎么实现呢？

其实我们可以用多个二维数组来记录这些变量，通过改变数组某个位置的值来响应用户，在让View去根据状态显示不同的图片达到我们想要的效果。

```
private boolean[][] mBombArray;//地雷
private boolean[][] mMarkArray;//标记
private boolean[][] mOpenArray;//打开的位置
private int[][] mBombNumberArray;//地雷周围的数量
private CellImageView[][] mCellArray;初始化GridLayout子项
```

###然后我们需要初始化以上数组
```
//设置地雷和地雷周围的数字
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
```
```
private void increaseBombNumber(int currentRow, int currentColumn) {
    if (currentRow > -1 && currentRow < mTotalRows && currentColumn > -1
            && currentColumn < mTotalColumns) {
        if (mBombNumberArray[currentRow][currentColumn] !=Constant.BOMB_AREA_NUMBER) {
            mBombNumberArray[currentRow][currentColumn]++;
        }
    }
}
```
### 对于空白的地方的打开规则
如果用户点击的地方既没有数字，也没有地雷，那么需要打开它周围的地方，直到边界不再为空白为止。这就是空白部分的打开规则。
```
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
```
```
//打开下一个位置的方块
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
```
### 游戏结束

当用户打开的方块数目加上地雷数等于初始化的GridLaout的子项总数时，代表用户已经赢了游戏。

而当用户点击到地雷，则代表游戏结束，游戏结束需要根据数组的状态把界面重新绘制出来，到这里，游戏的基本功能就大致结束了，至于其它更多的功能，比如快捷操作、用户自定义方块的大小、选择游戏难度等，就不再赘述，感兴趣的，可以去Google play下载Apk体验。


- [Google play下载地址](https://play.google.com/store/apps/details?id=com.gpc.minesweeper)

- [我的博客主页](http://www.jianshu.com/users/590cfc23071d/latest_articles)
