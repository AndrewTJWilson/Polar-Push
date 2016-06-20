package com.example.williamhartmann.nyedt;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import java.lang.*;
import java.util.ArrayList;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */

    int x,y;
    int angle = 20;
    JoyStick joyStick1;
    JoyStick joyStick2;
    boolean touchEvent;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RelativeLayout rl = new RelativeLayout(this);
        BouncingBallView bouncingBallView = new BouncingBallView(this);
        rl.addView(bouncingBallView);
        joyStick1 = new JoyStick(this);
        joyStick2 = new JoyStick(this);

        joyStick1.setBouncingBallView(bouncingBallView);
        joyStick2.setBouncingBallView(bouncingBallView);
       joyStick1.setPlacementX(100);
        joyStick1.setPlacementY(100);
        joyStick2.setPlacementX(625);
        joyStick2.setPlacementY(1050);

        rl.addView(joyStick1);
        rl.addView(joyStick2);
        setContentView(rl);
        bouncingBallView.setBackgroundResource(R.drawable.spilleplade);
        //joyStick.setBouncingBallView(bouncingBallView);

        joyStick1.setOnTouch(touchEvent);



        rl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touchEvent = true;

                x = (int) event.getX();
                y = (int) event.getY();


//Transfer click to other classes
                joyStick1.setXY(x, y);


                joyStick1.setAngle(angle);


                if (event.getAction() == MotionEvent.ACTION_UP) {
                    joyStick1.fingerUp = true;
                } else {
                    joyStick1.fingerUp = false;
                }

                joyStick1.invalidate();


                return true;
            }
        });
    }
}
