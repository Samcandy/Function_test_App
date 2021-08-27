package com.example.functiontest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hideSystemUI();
        setContentView(R.layout.activity_main);
        /*
        editHeight = findViewById(R.id.editHeight);
        editWeight = findViewById(R.id.editWeight);
        button = findViewById(R.id.btnCalculate);

        txtResult = findViewById(R.id.txtResult);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                // TODO something
                double Height = Double.parseDouble(editHeight.getText().toString());
                double Weight = Double.parseDouble(editWeight.getText().toString());

                double BMI = Weight / (Height * Height);

                txtResult.setText(String.valueOf(BMI));
            }
        });
        */
    }


    public void closeApp(View view){
        finish();
        System.exit(0);
    }

    @SuppressLint("SetTextI18n")
    public void startApp(View view){
        setContentView(R.layout.canvas_view);
    }

    public void exitApp(View view){
        setContentView(R.layout.activity_main);

        //Log.d("Sam", );
    }

    public void hideSystemUI(){
        // 設定全螢幕並隱藏 Navigation Bar
        View decorView;
        decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }
/*
    @SuppressLint("SetTextI18n")
    @Override
    public boolean onTouchEvent(MotionEvent event){
        float x = event.getX();
        float y = event.getY();
        int action = event.getAction();
        txtView.setText("X axis: " + String.valueOf(x) + " Y axis: " + String.valueOf(y));

        switch(action) {
            case (MotionEvent.ACTION_DOWN) :
                Log.d(DEBUG_TAG,"Action was DOWN");
                break;
            case (MotionEvent.ACTION_MOVE) :
                Log.d(DEBUG_TAG,"Action was MOVE");
                break;
            case (MotionEvent.ACTION_UP) :
                Log.d(DEBUG_TAG,"Action was UP");
                break;
        }
        return true;
    }

*/
}