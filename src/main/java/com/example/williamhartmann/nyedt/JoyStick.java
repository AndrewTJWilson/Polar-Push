package com.example.williamhartmann.nyedt;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by williamhartmann on 16/06/16.
 */
public class JoyStick extends View {
    Paint elPainto;
    int centerX = 0;
    int centerY = 0;
    int x = 5;
    int y = 4;
    int joystickRadius = 75;
    int buttonRadius = 30;
    int redEdgesX = 0;
    int redEdgesY = 0;
    int startCoordinateX = 500;
    int startCoordinateY = 500;
    int oldAngle;
    int width = 100;
    int height = 100;
    int angle;
    int xRate;
    int yRate;
    int angleDiff;
    int skalarProduct;
    int vectorLength;
    int lengthProduct;
    int x_dir = 0;
    int y_dir=0;
    int placementX = 0;
    int placementY = 0;
    public Paint mainCircle;
    public Paint secondaryCircle;
    public Paint button;
    public Paint horizontalLine;
    public Paint verticalLine;
    Bitmap polarbear;
    boolean fingerUp = true;
    boolean touchEvent;
    boolean handlingFriction;
    boolean init = true;
    int joystick_X = 100;
    int joystick_Y = 100;
    int oldStartCoordinateX = 0;
    int oldStartCoordinateY = 0;
    Thread thread;
    Handler handler;
    BouncingBallView bouncingBallView;
    Character character;
    ArrayList<Character> characters;

    public JoyStick(Context context) {
        super(context);

        initJoystickView();

        elPainto = new Paint();
        elPainto.setColor(Color.RED);

    }

    protected void initJoystickView() {


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);


                postInvalidate();
            }
        };

        redEdgesX = centerX;
        redEdgesY = centerY;
        //Setting up all the painting
        polarbear = BitmapFactory.decodeResource(getResources(), R.drawable.polarbearandroid);


        mainCircle = new Paint();
        mainCircle.setColor(Color.BLACK);
        mainCircle.setStyle(Paint.Style.FILL_AND_STROKE);

        secondaryCircle = new Paint();
        secondaryCircle.setColor(Color.MAGENTA);
        secondaryCircle.setStyle(Paint.Style.STROKE);

        verticalLine = new Paint();
        verticalLine.setStrokeWidth(5);
        verticalLine.setColor(Color.MAGENTA);

        horizontalLine = new Paint();
        horizontalLine.setStrokeWidth(2);
        horizontalLine.setColor(Color.MAGENTA);

        button = new Paint(Paint.ANTI_ALIAS_FLAG);
        button.setColor(Color.MAGENTA);
        button.setStyle(Paint.Style.FILL);

    }


    @Override
    protected void onDraw(Canvas canvas) {


        boolean collidesWithRightWall = startCoordinateX >= canvas.getWidth()-width-100;
        boolean collidesWithLeftWall = startCoordinateX <= 105;
        boolean collidesWithTopWall = startCoordinateY <= 350;
        boolean collidesWithBottomWall = startCoordinateY >= canvas.getHeight()-height-330;

//        calcAngleForMovement();

        super.onDraw(canvas);
        centerX = getWidth() - placementX;
        centerY = getHeight() - placementY;
        if (init) {
            startCoordinateX = getWidth() / 2;
            startCoordinateY = getHeight() / 2;

            //setTheDamnCherriesCH();
            //characters.get(0).setSpeed(startCoordinateX,startCoordinateY);

            redEdgesX = centerX;
            redEdgesY = centerY;
            init = false;
        }




        // painting the main circle
        canvas.drawCircle((int) centerX, (int) centerY, joystickRadius,
                mainCircle);
        // painting the secondary circle
        canvas.drawCircle((int) centerX, (int) centerY, joystickRadius / 2,
                secondaryCircle);
        // paint lines
        canvas.drawLine((float) centerX, (float) centerY, (float) centerX,
                (float) (centerY - joystickRadius), verticalLine);
        canvas.drawLine((float) (centerX - joystickRadius), (float) centerY,
                (float) (centerX + joystickRadius), (float) centerY,
                horizontalLine);
        canvas.drawLine((float) centerX, (float) (centerY + joystickRadius),
                (float) centerX, (float) centerY, horizontalLine);


        // painting the move button

        if (fingerUp) {
            canvas.drawCircle(centerX, centerY, buttonRadius, button);


        } else {
            movementOfJoystick();
            canvas.drawCircle(redEdgesX, redEdgesY, buttonRadius, button);

        }


//        if (!handlingFriction) {
//            handlingFriction = true;
//            xRate = startCoordinateX + (x - centerX) / 10;
//            yRate = startCoordinateY + (y - centerY) / 10;
//            angleDiff = oldAngle - angle;
//            handlingFriction();
//        }
        if (collidesWithRightWall) {
//            Log.w("Joystick","Collides");
            startCoordinateX = canvas.getWidth()-100-101;
        }else
        if (collidesWithLeftWall) {
            startCoordinateX = 106;
        } else
        if (collidesWithBottomWall) {
            startCoordinateY = canvas.getHeight()-430;
        } else if (collidesWithTopWall) {
            startCoordinateY = 350;
        }



        bouncingBallView.setOldCherry_x(startCoordinateX);
        bouncingBallView.setOldCherry_y(startCoordinateY);



        this.startCoordinateX +=  (redEdgesX - centerX) / 5;
        this.startCoordinateY +=  (redEdgesY - centerY) / 5;


        bouncingBallView.setCherries(startCoordinateX,startCoordinateY);



        oldAngle = angle;


        touchEvent = false;
        if (!handlingFriction) {
            if (!touchEvent) {
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                }
                invalidate();

            }
        }
    }

              public boolean onTouch(View v, MotionEvent event) {
                touchEvent = true;

                x = (int) event.getX();
                y = (int) event.getY();


//Transfer click to other classes
                setXY(x, y);


                setAngle(angle);


                if (event.getAction() == MotionEvent.ACTION_UP) {
                    fingerUp = true;
                } else {
                    fingerUp = false;
                }

                invalidate();


                return true;
            }

    // hello, this is a commit test!
    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setOnTouch(boolean touchEvent) {
        this.touchEvent = touchEvent;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }


    public void handlingFriction() {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                if (oldAngle == angle) {

                    handlingFriction = false;
                    return;
                }
                //Vi tager differencen mellem old og new angle til at være tiden det skal tage at rette kursen op.
                while (startCoordinateX != startCoordinateX + (x - centerX) / 5) {
                    startCoordinateX += (xRate / angleDiff) * 10;
                    handler.sendEmptyMessage(1);
                }

                while (startCoordinateY != startCoordinateY + (y - centerY) / 5) {
                    startCoordinateY += (yRate / angleDiff) * 10;
                    handler.sendEmptyMessage(0);
                }
            }
        };
        thread = new Thread(runnable);
        runnable.run();

    }



    public void movementOfJoystick() {

        //De "20" ekstra til sidst i udregningerne er for at den røde button ikke bliver for yderlig!!
        while (x > redEdgesX && redEdgesX < (centerX + joystickRadius - 20)) {
            redEdgesX++;
        }
        while (y > redEdgesY && redEdgesY < (centerY + joystickRadius - 20)) {
            redEdgesY++;

        }

        while (x < redEdgesX && redEdgesX > (centerX - joystickRadius + 20)) {


            redEdgesX--;
        }

        while (y < redEdgesY && redEdgesY > (centerY - joystickRadius + 20)) {
            redEdgesY--;
//
        }


    }

    public void setBouncingBallView(BouncingBallView bouncingBallView) {
        this.bouncingBallView = bouncingBallView;
    }

    public void setCharacter (Character character){
        this.character = character;
    }


    /*public void setTheDamnCherriesCH(){
        for (Character character : bouncingBallView.characters){
            character.setCherries(startCoordinateX,startCoordinateY);
        }
    }
*/
    public void setCharacters(ArrayList<Character> characters) {
        this.characters = characters;
    }

    public void setStartCoordinateX(int startCoordinateX) {
        this.startCoordinateX = startCoordinateX;
    }

    public void setStartCoordinateY(int startCoordinateY) {
        this.startCoordinateY = startCoordinateY;
    }

    public void setCherries(int startCoordinateX,int startCoordinateY){
        this.joystick_X=startCoordinateX;
        this.joystick_Y=startCoordinateY;
    }

    public int getStartCoordinateX() {
        return joystick_X;
    }

    public int getStartCoordinateY() {
        return joystick_Y;



    }

    public void setPlacementX(int placementX) {
        this.placementX = placementX;
    }

    public void setPlacementY(int placementY) {
        this.placementY = placementY;
    }
}
