package com.example.functiontest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class MyCanvasView extends View {
    private static final String DEBUG_TAG = "Sam";
    private Bitmap mBitmap;
    private Canvas  mCanvas;
    private Path mPath;
    private Paint mPaint;
    private Paint mPaintSlash;
    private Paint mPaintText;
    private Paint mPaintRec;
    private Paint mPaintSeq;
    private Paint.FontMetrics fontMetrics;
    private RectF seqF;
    private RectF rectF;
    private float distance, baseline;
    private float mX, mY;
    public int testMode;
    private int failStatus;
    private int vWidth, vHeight;
    final private int iconSize = 60;
    private boolean drawStatus;
    private boolean condition;

    private void init(){
        mPath = new Path();
        testMode = 0;
        drawStatus = false;
        //繪製線條
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(10);
        //繪製斜線
        mPaintSlash = new Paint();
        mPaintSlash.setAntiAlias(true);
        mPaintSlash.setDither(true);
        mPaintSlash.setColor(Color.parseColor("#E0F2F4"));
        mPaintSlash.setStyle(Paint.Style.STROKE);
        mPaintSlash.setStrokeJoin(Paint.Join.MITER);
        mPaintSlash.setStrokeCap(Paint.Cap.SQUARE);
        mPaintSlash.setStrokeWidth((float) (iconSize * Math.sqrt(2)));
        //繪製正方形
        mPaintSeq = new Paint();
        mPaintSeq.setColor(Color.YELLOW);
        mPaintSeq.setStyle(Paint.Style.FILL);
        seqF = new RectF(0, 0, iconSize, iconSize);
        //繪製長方形
        mPaintRec = new Paint();
        mPaintRec.setColor(Color.parseColor("#E0F2F4"));
        mPaintRec.setStyle(Paint.Style.FILL);
        rectF = new RectF(iconSize, 0, vWidth - iconSize, iconSize);
        //繪製字型
        mPaintText = new Paint();
        mPaintText.setAntiAlias(true);
        mPaintText.setColor(Color.GRAY);
        mPaintText.setStrokeWidth(1);
        mPaintText.setTextSize(25);
        mPaintText.setTextAlign(Paint.Align.CENTER);
        fontMetrics = mPaintText.getFontMetrics();
        distance = (fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.bottom;
    }

    public MyCanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //初始化(空畫布)
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        vWidth = w;
        vHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 畫圖示
        iconDraw(canvas);


        // 畫線
        canvas.drawPath(mPath, mPaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        float x = event.getX();
        float y = event.getY();
        int action = event.getAction();
        Log.d(DEBUG_TAG, "X: " + String.valueOf(x) + " Y: " + String.valueOf(y));

        switch(action) {
            case (MotionEvent.ACTION_DOWN) :
                Log.d(DEBUG_TAG,"Action was DOWN");
                touch_start(x, y);
                break;
            case (MotionEvent.ACTION_MOVE) :
                Log.d(DEBUG_TAG,"Action was MOVE");
                if(drawStatus){
                    touch_move(x, y);
                }
                break;
            case (MotionEvent.ACTION_UP) :
                Log.d(DEBUG_TAG,"Action was UP");
                if(drawStatus){
                    touch_up(x, y);
                }
                break;
        }
        invalidate();
        return true;
    }
    // 觸碰到螢幕時，取得手指的X,Y座標；並順便設定為線的起點
    private void touch_start(float x, float y) {
        judgeDraw(x, y, "down");
        Log.d(DEBUG_TAG, String.valueOf(condition));
        if (condition){
            drawStatus = true;
            mPath.reset();
            mPath.moveTo(x, y);
            mX = x;
            mY = y;
        } else{
            failStatus = 1; // start point fail
        }
    }
    // 在螢幕上滑動時，不斷刷新移動路徑
    private void touch_move(float x, float y) {
        judgeDraw(x, y, "move");
        if (condition){
            // 畫貝爾茲曲線
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            // 更新上一點X座標
            mX = x;
            // 更新上一點Y座標
            mY = y;
        } else{
            //setFail();
            drawStatus = false;
            failStatus = 2; // move point fail
            mPath.reset();
        }
    }
    // 當使用者放開時，把位置設為終點
    private void touch_up(float x, float y) {
        drawStatus = false;
        mPath.lineTo(mX, mY);
        mCanvas.drawPath(mPath,  mPaint);
        mPath.reset();

        // 切換測試模式
        if ( (testMode == 0 || testMode == 5) &&
                (x >= (vWidth - iconSize) && y <= iconSize)){
            if (testMode == 0){
                testMode = 1;
            } else {
                testMode = 6;
            }
        } else if ( (testMode == 1 || testMode == 4) &&
                (x >= (vWidth - iconSize) && y >= (vHeight - iconSize))){
            if (testMode == 1){
                testMode = 2;
            } else {
                testMode = 5;
            }
        } else if ( (testMode == 2 || testMode == 6) &&
                (x <= iconSize && y >= (vHeight - iconSize))){
            if (testMode == 2){
                testMode = 3;
            } else {
                testMode = 255;
            }
        } else if ( testMode == 3 &&
                (x <= iconSize && y <= iconSize)){
            testMode = 4;
        } else {
            condition = false;
            failStatus = 3; // up point fail
        }
    }

    private void judgeDraw(float x, float y, String mode) {
        switch (mode) {
            case "down":
                if (((testMode == 0 || testMode == 4) && (x <= iconSize && y <= iconSize)) ||
                    ((testMode == 1 || testMode == 6) && (x >= (vWidth - iconSize) && y <= iconSize)) ||
                    ((testMode == 2 || testMode == 5) && (x >= (vWidth - iconSize) && y >= (vHeight - iconSize))) ||
                    ((testMode == 3 && (x <= iconSize && y >= (vHeight - iconSize))))) {
                    condition = true;
                } else {
                    condition = false;
                }
            case "move":
                if (!drawStatus){
                    break;
                }
                if ((testMode == 0 && y <= iconSize) ||
                    ((testMode == 1 || testMode == 5) && x >= (vWidth - iconSize)) ||
                    (testMode == 2 && y >= (vHeight - iconSize)) ||
                    (testMode == 3 && x <= iconSize) ||
                    (testMode == 4 && (IsPointInMatrix(x, y) ||
                            (x < iconSize && y < iconSize) ||
                            (x > vWidth-iconSize && y > vHeight-iconSize))) ||
                    (testMode == 6 && IsPointInMatrix(x, y) ||
                            (x > vWidth-iconSize && y < iconSize) ||
                            (x < iconSize && y > vHeight-iconSize))) {
                    condition = true;
                } else {
                    condition = false;
                }
        }
    }

    private void iconDraw(Canvas canvas){
        if (!condition && failStatus != 0){
            mPaintText.setTextSize(100);
            mPaintText.setColor(Color.RED);
            canvas.drawText("Fail!!", 4 * iconSize, vHeight >> 1, mPaintText);
        }

        mPaintText.setColor(Color.GRAY);
        mPaintText.setTextSize(35);
        // 顯示座標
        canvas.drawText("X： " + String.valueOf(mX) + "    Y： " + String.valueOf(mY),
                vWidth >> 1, 15 * iconSize, mPaintText);

        // 重設方塊數字的字體大小
        mPaintText.setTextSize(25);

        if (testMode == 0){
            // 方塊 0 , 中間長方形, 方塊 1
            iconDrawRect(canvas);
            iconDrawSeq0(canvas);
            iconDrawSeq1(canvas);
        } else if (testMode == 1 || testMode == 5){
            // 方塊 1 , 中間長方形, 方塊 2
            iconDrawRect(canvas);
            iconDrawSeq1(canvas);
            iconDrawSeq2(canvas);
        } else if (testMode == 2){
            // 方塊 2 , 中間長方形, 方塊 3
            iconDrawRect(canvas);
            iconDrawSeq2(canvas);
            iconDrawSeq3(canvas);
        } else if (testMode == 3){
            // 方塊 3 , 中間長方形, 方塊 0
            iconDrawRect(canvas);
            iconDrawSeq3(canvas);
            iconDrawSeq0(canvas);
        } else if (testMode == 4){
            // 方塊 0, 斜長方形, 方塊 2
            canvas.drawLine(iconSize>>1, iconSize>>1,
                    vWidth-(iconSize>>1), vHeight-(iconSize>>1), mPaintSlash);
            iconDrawSeq0(canvas);
            iconDrawSeq2(canvas);
        } else if (testMode == 6){
            // 方塊 1, 斜長方形, 方塊 3
            canvas.drawLine(vWidth-(iconSize>>1), iconSize>>1,
                    iconSize>>1, vHeight-(iconSize>>1), mPaintSlash);
            iconDrawSeq1(canvas);
            iconDrawSeq3(canvas);
        }
        // 提示文字
        iconDrawHint(canvas);
    }

    private void iconDrawSeq0(Canvas canvas){
        // 方塊 0
        seqF.left = 0;
        seqF.right = iconSize;
        seqF.top = 0;
        seqF.bottom = iconSize;
        baseline = seqF.centerY() + distance;
        canvas.drawRect(seqF, mPaintSeq);
        canvas.drawText("0", seqF.centerX(), baseline, mPaintText);
    }

    private void iconDrawSeq1(Canvas canvas){
        // 方塊 1
        seqF.left = (vWidth - iconSize);
        seqF.right = vWidth;
        seqF.top = 0;
        seqF.bottom = iconSize;
        baseline = seqF.centerY() + distance;
        canvas.drawRect(seqF, mPaintSeq);
        canvas.drawText("1", seqF.centerX(), baseline, mPaintText);
    }

    private void iconDrawSeq2(Canvas canvas){
        // 方塊 2
        seqF.left = vWidth - iconSize;
        seqF.right = vWidth;
        seqF.top = vHeight - iconSize;
        seqF.bottom = vHeight;
        baseline = seqF.centerY() + distance;
        canvas.drawRect(seqF, mPaintSeq);
        canvas.drawText("2", seqF.centerX(), baseline, mPaintText);
    }

    private void iconDrawSeq3(Canvas canvas){
        // 方塊 3
        seqF.left = 0;
        seqF.right = iconSize;
        seqF.top = vHeight - iconSize;
        seqF.bottom = vHeight;
        baseline = seqF.centerY() + distance;
        canvas.drawRect(seqF, mPaintSeq);
        canvas.drawText("3", seqF.centerX(), baseline, mPaintText);
    }

    private void iconDrawRect(Canvas canvas){
        // 中間長方形
        if (testMode == 0){
            rectF.left = iconSize;
            rectF.right = vWidth - iconSize;
            rectF.top = 0;
            rectF.bottom = iconSize;
        } else if (testMode == 1 || testMode == 5){
            rectF.left = vWidth - iconSize;
            rectF.right = vWidth;
            rectF.top = iconSize;
            rectF.bottom = vHeight - iconSize;
        } else if (testMode == 2){
            rectF.left = iconSize;
            rectF.right = vWidth - iconSize;
            rectF.top = vHeight - iconSize;
            rectF.bottom = vHeight;
        } else if (testMode == 3){
            rectF.left = 0;
            rectF.right = iconSize;
            rectF.top = iconSize;
            rectF.bottom = vHeight - iconSize;
        } else if (testMode == 4){
            rectF.left = 0;
            rectF.right = iconSize;
            rectF.top = iconSize;
            rectF.bottom = vHeight - iconSize;
        }
        canvas.drawRect(rectF, mPaintRec);
    }

    private void iconDrawHint(Canvas canvas){
        // 提示文字
        mPaintText.setTextSize(35);
        int startNum = 0;
        int endNum = 0;
        if (testMode == 0){
            startNum = 0;
            endNum = 1;
        } else if (testMode == 1){
            startNum = 1;
            endNum = 2;
        } else if (testMode == 2){
            startNum = 2;
            endNum = 3;
        } else if (testMode == 3){
            startNum = 3;
            endNum = 0;
        } else if (testMode == 4){
            startNum = 0;
            endNum = 2;
        } else if (testMode == 5){
            startNum = 2;
            endNum = 1;
        } else if (testMode == 6){
            startNum = 1;
            endNum = 3;
        }
        if (testMode == 255){
            mPaintText.setTextSize(120);
            mPaintText.setColor(Color.GREEN);
            canvas.drawText("Pass!!", 4 * iconSize, vHeight >> 1, mPaintText);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 1s = 1000ms
                    System.exit(0);
                }
            }, 1000);
        }else {
            canvas.drawText("Move your finger from Rect " + String.valueOf(startNum) +
                    " to "+  String.valueOf(endNum), vWidth >> 1, 3 * iconSize, mPaintText);
        }
    }

    static final class Point {
        private float x;    // x-coordinate
        private float y;    // y-coordinate

        // point initialized from parameters
        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }

        // accessor methods
        public float x() { return x; }
        public float y() { return y; }
    }

    private float GetCross(Point p1,  Point p2, Point p)
    {
        return (p2.x() - p1.x()) * (p.y() - p1.y()) -
                (p.x() - p1.x()) * (p2.y() - p1.y());
    }

    // 預設初始座標為 testMode = 4 的四點座標
    private boolean IsPointInMatrix(float x, float y)
    {
        Point p = new Point(x, y);
        Point p1 = new Point(iconSize, 0);
        Point p2 = new Point(0, iconSize);
        Point p3 = new Point(vWidth - iconSize, vHeight);
        Point p4 = new Point(vWidth, vHeight - iconSize);

        if (testMode == 6){
            // 第一點座標
            p1.x = vWidth - iconSize;
            p1.y = 0;
            // 第二點座標
            p2.x = vWidth;
            p2.y = iconSize;
            // 第三點座標
            p3.x = iconSize;
            p3.y = vHeight;
            // 第四點座標
            p4.x = 0;
            p4.y = vHeight - iconSize;
        }

        return GetCross(p1,p2,p) * GetCross(p3,p4,p) >= 0 &&
                GetCross(p2,p3,p) * GetCross(p4,p1,p) >= 0;
    }
}
