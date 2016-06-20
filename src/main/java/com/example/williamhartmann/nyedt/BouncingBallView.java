package com.example.williamhartmann.nyedt;

import android.app.usage.UsageEvents;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class BouncingBallView extends View {
    private int xMin = 0;          // This view's bounds
    private int xMax;
    private int yMin = 0;
    private int yMax;
    private float ballRadius = 50; // Ball's radius
    private float ballX = ballRadius + 50;  // Ball's center (x,y)
    private float ballY = 65;
    private float ballSpeedX = 5;  // Ball's speed (x,y)
    private float ballSpeedY = 3;
    private Bitmap cherry_bm;
    private int x_dir = 5;
    private int y_dir = 3;
    private int cherry_x = 100;
    private int cherry_y = 100;
    private int joystick_X = 100;
    private int joystick_Y = 100;
    int oldCherry_x = 0;
    int oldCherry_y  = 0;
    int JoystickX=0;
    int JoystickY=0;

    private RectF ballBounds;      // Needed for Canvas.drawOval
    private Paint paint;           // The paint (e.g. style, color) used for drawing
    private Character seal, polarbear, blue, green, pink;
    private Bitmap bitmap;
    private JoyStick joystickPolarbear;

    public ArrayList<Character> characters;

    // Constructor
    public BouncingBallView(Context context) {
        super(context);
//        blue = new Character(context,R.drawable.squareiconblue,200,200,BouncingBallView.this);
//        blue.setBounds(100,100);
//        blue.setPosition(200,300);
//        blue.setSpeed(5,5);
//blue.setView(this);

//        green = new Character(context,R.drawable.squareicongreen,600,200,joystickPolarbear,BouncingBallView.this);
//        green.setBounds(100,100);
//        green.setPosition(200,200);
//        green.setSpeed(5,5);
//        green.setView(this);
//
//        pink = new Character(context,R.drawable.squareiconpink,500,400,joystickPolarbear, BouncingBallView.this);
//        pink.setBounds(100,100);
//        pink.setPosition(500,400);
//        pink.setSpeed(5,5);
//        pink.setView(this);

//        seal = new Character(context,R.drawable.squareiconbrown,200,200, joystickPolarbear,BouncingBallView.this);
//        seal.setBounds(100,100);
//        seal.setPosition(400,200);
//        seal.setSpeed(5,5);
//        seal.setView(this);

        polarbear = new Character(context,R.drawable.squareiconred,800,1200,joystickPolarbear,BouncingBallView.this);
        polarbear.setBounds(100,100);
        polarbear.setPosition(300,400);
        polarbear.setSpeed(5,5);
        polarbear.setView(this);

        joystickPolarbear = new JoyStick(context);
        joystickPolarbear.setCharacter(polarbear);

        characters = new ArrayList<Character>();
        characters.add(polarbear);
//        characters.add(green);
//        characters.add(blue);
//        characters.add(pink);
//        characters.add(seal);



       // Bitmap decodedImage = BitmapFactory.decodeResource(context.getResources(),R.drawable.polarbearandroid);
        //bitmap = decodedImage.createScaledBitmap(decodedImage,100,100,true);
        //polarbear = new Character(context,R.drawable.polarbearandroid,50,50);
    }

    // Called back to draw the view. Also called by invalidate().
    @Override
    protected void onDraw(Canvas canvas) {

        boolean collidesWithRightWall = getJoystickX() >= canvas.getWidth()-100-100;
        boolean collidesWithLeftWall = getJoystickX() <= 105;
        boolean collidesWithTopWall = getJoystickY() <= 350;
        boolean collidesWithBottomWall = getJoystickY() >= canvas.getHeight()-100-330;
        boolean collidesTopRight = collidesWithRightWall && collidesWithTopWall;
        boolean collidesTopLeft = collidesWithLeftWall && collidesWithTopWall;
        boolean collidesBottomRight = collidesWithBottomWall && collidesWithRightWall;
        boolean collidesBottomLeft = collidesWithBottomWall && collidesWithLeftWall;

        // Draw the ball
            if (collidesWithRightWall) {
//                Log.w("Right","Collides");
               joystick_X = canvas.getWidth()-100-101;
            }else if (collidesWithLeftWall) {
                joystick_X = 106;
            } else if (collidesWithBottomWall) {
                joystick_Y = canvas.getHeight()-430;
            } else if (collidesWithTopWall) {
                joystick_Y = 350;
            }

            if (collidesBottomRight){
                joystick_Y = canvas.getHeight()-430;
                joystick_X = canvas.getWidth()-100-101;
            } else if(collidesBottomLeft){
                joystick_X = 106;
                joystick_Y = canvas.getHeight()-430;
            } else if(collidesTopLeft){
                joystick_Y = 350;
                joystick_X = 106;
            } else if(collidesTopRight){
                joystick_Y = 350;
                joystick_X = canvas.getWidth()-100-101;
            }



    x_dir = joystick_X-oldCherry_x;
    y_dir = joystick_Y-oldCherry_y;




        joystick_X = getJoystickX();
        joystick_Y = getJoystickY();

            polarbear.drawBitmap(canvas,joystick_X,joystick_Y);

//        blue.drawBitmap(canvas,blue.getCherry_x(),blue.getCherry_y());
//        green.drawBitmap(canvas,green.getCherry_x(),green.getCherry_y());
//        pink.drawBitmap(canvas,pink.getCherry_x(),pink.getCherry_y());
//        blue.updatePosition(canvas,characters);
//        green.updatePosition(canvas,characters);
//        pink.updatePosition(canvas,characters);
//        seal.updatePosition(canvas,characters);






        // Delay
        try {
            Thread.sleep(0);
        } catch (InterruptedException e) { }

        invalidate();  // Force a re-draw
    }


    public ArrayList<Character> getCharacters() {return this.characters;}

    public void setCherries(int startCoordinateX, int startCoordinateY) {
        joystick_X=startCoordinateX;
        joystick_Y=startCoordinateY;
    }

    public int getJoystickX(){
        return joystick_X;
    }

    public int getJoystickY(){
        return joystick_Y;
    }

    public void setOldCherry_x(int x){
        this.oldCherry_x=x;
    }

    public void setOldCherry_y(int y){
        this.oldCherry_y=y;
    }

    public void collesionManagement(Character one, Character two) {
//
//        x_dir = getJoystickX();
//        y_dir = getJoystickY();

//        Log.v("speedoneX",""+x_dir);
//        Log.v("speedtwoX",""+two.x_dir);
//
//        double colissionVectorX = x_dir+two.x_dir;
//        double colissionVectorY = y_dir+two.y_dir;
//
//        Log.v("colissionVectorX", colissionVectorX + "");
//        Log.v("colissionVectorY", colissionVectorY + "");
//
//
//
//        one.cherry_x+=colissionVectorX;
//        one.cherry_y+=colissionVectorY;
//
//        if  (colissionVectorX>0) {
//            cherry_x += colissionVectorX / 3;
//            switchFortegnX = false;
//        } else if (colissionVectorY<0) {
//            switchFortegnX = true;
//            cherry_x -= colissionVectorX / 3;
//        }
//
//
//
//        if (colissionVectorY>0) {
//            cherry_y += colissionVectorY / 3;
//            switchFortegnY = false;
//        } else if (colissionVectorY<0) {
//            switchFortegnY = true;
//            cherry_y -= colissionVectorY / 3;
//        }




    }

    public int getX_dir() {
        return x_dir;
    }

    public int getY_dir() {
        return y_dir;
    }

    public void setX_dir(int x){
        x_dir=x;
    }

    public void setY_dir(int y){
        y_dir=y;
    }

    public Character getJoystickPlayer(){
        return polarbear;
    }
}
