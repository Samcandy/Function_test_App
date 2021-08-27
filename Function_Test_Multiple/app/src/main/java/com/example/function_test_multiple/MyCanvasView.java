package com.example.function_test_multiple;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;


public class MyCanvasView extends View {
    private static final String DEBUG_TAG = "Sam";
    private Bitmap mBitmap;
    private Canvas  mCanvas;
    private Paint mPaint;
    private Path mPath;
    private Path mPath0;
    private Path mPath1;
    private Path mPath2;
    private Path mPath3;
    private Path mPath4;
    private Path mPath5;
    private Path mPath6;
    private Path mPath7;
    private Path mPath8;
    private Path mPath9;
    private int pointerCount;
    private float[] mX;
    private float[] mY;
    private int[] mId;
    private int[] mColor;

    private void init(){
        mX = new float[10];
        mY = new float[10];
        mColor = new int[10];
        mId = new int[10];
        //初始化路線
        mPath = new Path();
        mPath0 = new Path();
        mPath1 = new Path();
        mPath2 = new Path();
        mPath3 = new Path();
        mPath4 = new Path();
        mPath5 = new Path();
        mPath6 = new Path();
        mPath7 = new Path();
        mPath8 = new Path();
        mPath9 = new Path();
        //繪製線條
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(3);

        mColor[0] = Color.RED;
        mColor[1] = Color.BLUE;
        mColor[2] = Color.GREEN;
        mColor[3] = Color.YELLOW;
        mColor[4] = Color.GRAY;
        mColor[5] = Color.BLACK;
        mColor[6] = Color.MAGENTA;
        mColor[7] = Color.CYAN;
        mColor[8] = Color.LTGRAY;
        mColor[9] = Color.parseColor("#E0F2F4");
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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for(int i=0; i < 10; i++){
            mPaint.setColor(mColor[i]);
            chosePath(i);
            canvas.drawPath(mPath, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        pointerCount = event.getPointerCount();
        int action = event.getAction();

        Log.d(DEBUG_TAG, "Point Count: " + String.valueOf(pointerCount) );
        for (int i = 0; i < pointerCount; i++){
            float x = event.getX(i);
            float y = event.getY(i);
            int id = event.getPointerId(i);

            mX[id] = x;
            mY[id] = y;
            mId[i] = id;
            Log.d(DEBUG_TAG,"id: "+ String.valueOf(id));
        }

        for (int i = 0; i < mX.length; i++) {
            if (mX[i] !=0){
                Log.d(DEBUG_TAG, "X[" + String.valueOf(i) + "]: " + String.valueOf(mX[i]) +
                        " Y[" + String.valueOf(i) + "]: " + String.valueOf(mY[i]));
                chooseAction(action, i);
                mX[i] = 0;
                mY[i] = 0;
            }
        }
        invalidate();
        return true;
    }

    private void chosePath(int index){
        switch(index){
            case 0:
                mPath = mPath0;
                break;
            case 1:
                mPath = mPath1;
                break;
            case 2:
                mPath = mPath2;
                break;
            case 3:
                mPath = mPath3;
                break;
            case 4:
                mPath = mPath4;
                break;
            case 5:
                mPath = mPath5;
                break;
            case 6:
                mPath = mPath6;
                break;
            case 7:
                mPath = mPath7;
                break;
            case 8:
                mPath = mPath8;
                break;
            case 9:
                mPath = mPath9;
                break;
        }
    }

    private void  chooseAction(int action, int index){
        int clearId = 0;
        Log.d(DEBUG_TAG,String.valueOf(action));
        if (action > MotionEvent.ACTION_MOVE){
            clearId = (action-5)/256;
            if ((action - 5)% 256 == 0){
                action = MotionEvent.ACTION_DOWN;
            }else{
                action = MotionEvent.ACTION_UP;
            }
        }
        chosePath(index);
        switch(action) {
            case (MotionEvent.ACTION_DOWN):
                Log.d(DEBUG_TAG, "Action was DOWN");
                mPath.moveTo(mX[index], mY[index]);
                break;
            case (MotionEvent.ACTION_MOVE):
                Log.d(DEBUG_TAG, "Action was MOVE");
                mPath.lineTo(mX[index], mY[index]);
                break;
            case (MotionEvent.ACTION_UP):
                Log.d(DEBUG_TAG, "Clear id: " + String.valueOf(clearId));
               /* if (pointerCount == 1){
                    Log.d(DEBUG_TAG, "Action was UP");
                    for (int i=0; i<10; i++){
                        chosePath(i);
                        mPath.reset();
                    }
                }*/
                if (index - mId[clearId] == 0){
                    Log.d(DEBUG_TAG, "Action was UP");
                    mPath.reset();
                }
                break;
        }
    }
}
