package com.example.williamhartmann.nyedt;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;


import java.util.ArrayList;

/**
 * Created by thoma on 15-06-2016.
 */

public class Character {

    private int rightX, leftX, topY, downY;
    private Bitmap bitmap, decodedImage;
    private int icon, xPos, yPos;
    private Context context;
    private float xPosition, yPosition;
    private boolean setJoystick = false, intersects = false;
    private boolean thisBeneathOther = false, thisAboveOther = false, thisRightToOther = false, thisLeftToOther = false;
    private Point p1;
    private int width = 100;
    private int height = 65;
    private int x_dir = 10;
    private int y_dir = 10;
    private int cherry_x = 100;
    private int cherry_y = 100;
    private int oldCherry_x = 0;
    private int oldCherry_y = 0;
    int oldRightX = 0;
    int oldLeftX = 0;
    int oldDownY = 0;
    int oldTopY = 0;
    int fakeCherry_x = 0;
    int fakeCherry_y = 0;
    long start = 0;
    int cherryCounter = 0;
    private int speedX = 1;
    private int speedY = 1;
    private int joystick_X = 0;
    private int joystick_Y = 0;
    boolean justCollided = false;
    private BouncingBallView theView;
    private JoyStick joystick;
    int i = 0;
    boolean coliding;
    boolean dekremeringX;
    boolean dekremeringY;
    boolean backwards;
    ArrayList<Integer> oldCharriesX = new ArrayList<Integer>();
    ArrayList<Integer> oldCharriesY = new ArrayList<Integer>();
    BouncingBallView bouncingBallView;
    Stopwatch stopwatch = new Stopwatch();
    Character intesector;


    public Character(Context context, int icon, int xPos, int yPos, JoyStick joystick, BouncingBallView theView) {
        this.context = context;
        this.joystick = joystick;
        this.icon = icon;
        this.xPos = xPos;
        this.yPos = yPos;
        theView = theView;
        decodePicture();

    }

    public Character(Context context, int icon, int xPos, int yPos, BouncingBallView theView) {
        this.context = context;
        this.joystick = joystick;
        this.icon = icon;
        this.xPos = xPos;
        this.yPos = yPos;
        theView = theView;
        decodePicture();


    }

    private void decodePicture() {
        decodedImage = BitmapFactory.decodeResource(context.getResources(), icon);
        bitmap = decodedImage.createScaledBitmap(decodedImage, 100, 65, true);
    }


    public void setCherryCoordinatesFromObject(int cherry_x, int cherry_y) {
        leftX = cherry_x;
        topY = cherry_y;
        rightX = cherry_x + bitmap.getWidth();
        downY = cherry_y + bitmap.getHeight();


    }

    public void drawBitmap(Canvas canvas, float left, float top) {
        canvas.drawBitmap(bitmap, left, top, null);
        setCherryCoordinatesFromObject((int) left, (int) top);
    }

    public void setBounds(int width, int height) {
        bitmap = decodedImage.createScaledBitmap(decodedImage, width, height, true);
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean intersects(Character other) {

        boolean intersects;

        assert (leftX < rightX);
        assert (topY < downY);
        assert (other.leftX < other.rightX);
        assert (other.topY < other.downY);

        if ((rightX <= other.leftX) || // A is to the left of B
                (other.rightX <= leftX) || // B is to the left of A
                (downY <= other.topY) || // A is above B
                (other.downY <= topY))   // B is above A
        {
            intersects = false; // A and B don't intersect

        } else {
            intersects = true; // A and B intersect

        }

        return intersects;
    }

    public void updatePosition(Canvas canvas, ArrayList<Character> characters) {

        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
        }

        boolean collidesWithRightWall = cherry_x >= canvas.getWidth() - width - 100;
        boolean collidesWithLeftWall = cherry_x <= 105;
        boolean collidesWithTopWall = cherry_y <= 350;
        boolean collidesWithBottomWall = cherry_y >= canvas.getHeight() - height - 330;
        boolean collidesWithWall = collidesWithBottomWall || collidesWithLeftWall || collidesWithRightWall || collidesWithTopWall;

        double canvasWidth = 8.2;
        double canvasHeight = 13;
        double widthToFirstObjectLeftWall = 1.7;
        double widthToFirstObjetRightWall = 3.1;
        double heightToFirstObject = 6.3;
        double blockWidth = 1.4;
        double blockHeight = 0.7;
        // Størrelse det første objekt fylder for canvas width.
        double percentageObjectOfCanvasWidth = (1.4 / 8.2);
        double blockWidthPixel = percentageObjectOfCanvasWidth * canvas.getWidth();
        ;
        // Længde fra væg til start af objekt:
        double distanceFromCanvasLeftWallToFirstObject = canvas.getWidth() * (1.7 / 8.2);

        double percentageObjectOfCanvasHeight = 0.7 / 13;
        double blockHeightPixel = percentageObjectOfCanvasHeight * canvas.getHeight();

        double distanceFromCanvasTopToObject = canvas.getHeight() * (6.3 / 13);


        boolean collidesWithObjectTop = cherry_y <= 950 && cherry_y >= canvas.getHeight() - height - 700 && cherry_x <= distanceFromCanvasLeftWallToFirstObject + blockWidthPixel && cherry_x >= distanceFromCanvasLeftWallToFirstObject;
        boolean cooolio = cherry_y <= distanceFromCanvasTopToObject + blockHeightPixel && cherry_y >= distanceFromCanvasTopToObject && cherry_x <= distanceFromCanvasLeftWallToFirstObject + blockWidthPixel && cherry_x >= distanceFromCanvasLeftWallToFirstObject;


        if (collidesWithRightWall) {
            x_dir = -speedX;

        }
        if (collidesWithLeftWall) {
            x_dir = speedX;

        }
        if (collidesWithBottomWall) {
            y_dir = -speedY;


        }
        if (collidesWithTopWall) {
            y_dir = speedY;

        }

        intersects = false;



        for (Character character : characters) {
            if (this.intersects(character)) {
                intersects = true;
                intesector = character;
                collesionManagement(character);
            }
        }


            this.cherry_x += x_dir;
            this.cherry_y += y_dir;




    }

    public void onScreenTouched(MotionEvent event) {
        this.cherry_x = (int) event.getRawX();
        this.cherry_y = (int) event.getRawY();
    }


    public void setPosition(int x, int y) {
        this.cherry_x = x;
        this.cherry_y = y;
    }

    public void setSpeed(int speedX, int speedY) {
        this.speedX = speedX;
        this.speedY = speedY;
        this.x_dir = speedX;
        this.y_dir = speedY;
    }

    public int getXSpeed() {
        return x_dir;
    }

    public int getYSpeed() {
        return y_dir;
    }

    //sd
    public Point getPosition() {
        return p1;
    }

    public void printCoordinates() {
        Log.w("Coordinates: ", "MinX: " + leftX + " MinY: " + topY + " MaxX: " + rightX + " MaxY: " + downY);
    }

    public int getLeftX() {
        return this.leftX;
    }

    public int getTopY() {
        return this.topY;
    }

    public int getDownY() {
        return this.downY;
    }

    public int getRightX() {
        return rightX;
    }

    public int getCherry_x() {
        return cherry_x;
    }

    public int getCherry_y() {
        return cherry_y;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getI() {
        return i;
    }

    public int getJoystick_X() {
        return joystick_X;
    }

    public int getJoystick_Y() {
        return joystick_Y;
    }

    public void setCherries(int startCoordinateX, int startCoordinateY) {
        this.joystick_X = startCoordinateX;
        this.joystick_Y = startCoordinateY;
    }

    public void collesionManagement(Character character) {


        int colissionVectorX;
        int colissionVectorY;

        if (character.equals(theView.getJoystickPlayer())) {
            colissionVectorX = -x_dir + theView.getX_dir() * 2;
            colissionVectorY = -y_dir + theView.getY_dir() * 2;
            Log.v("udgør", ""+theView.getX_dir()/x_dir);

        }
        else {
            Log.v("view", ""+theView.getX_dir());
            Log.v("charac", ""+x_dir);
            colissionVectorX = -x_dir + (character.getX_dir())*2;
            colissionVectorY = -y_dir + (character.getY_dir())*2;
        }

            Double cherryXD = cherry_x - (x_dir * 0.2);
            cherry_x = cherryXD.intValue();

            Double cherryYD = cherry_y - (y_dir * 0.2);
            cherry_y = cherryYD.intValue();


            x_dir = colissionVectorX;
            y_dir = colissionVectorY;


    }


    public int getCenterX() {
        return rightX / 2;
    }

    public int getCenterY() {
        return downY / 2;
    }

    public int getOldCenterX() {
        return oldRightX / 2;
    }

    public int getOldCenterY() {
        return oldDownY / 2;
    }

    public void setView(BouncingBallView theView) {
        this.theView = theView;

    }

    public void shortBreak() {
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
        }
    }

    public int settingVelX(Character one) {
        int vx1 = cherry_x;

//        for (Integer cherry : oldCharriesX){
//            oldCherry_x+=cherry;
//        }

        if (one.cherry_x < 0 && one.oldCherry_x < 0 && one.cherry_x < one.oldCherry_x) {
            vx1 = -1 * (one.cherry_x + one.oldCherry_x);
        } else if (one.cherry_x < 0 && one.oldCherry_x < 0 && one.cherry_x > one.oldCherry_x) {
            vx1 = one.cherry_x + one.oldCherry_x;

        } else {
            vx1 = one.cherry_x - one.oldCherry_x;

        }
        return vx1;
    }


    public void setOldCherry_x() {

        for (Integer cherry : oldCharriesX) {
            oldCherry_x += cherry;
        }

    }

    public void setOldCherry_Y() {

        for (Integer cherry : oldCharriesY) {
            oldCherry_y += cherry;
        }

    }


    public int settingVelY(Character one) {


        int vy1 = cherry_y;

        if (one.cherry_y < 0 && one.oldCherry_y < 0 && one.cherry_y < one.oldCherry_y) {
            vy1 = -1 * (one.cherry_y + one.oldCherry_y);
        } else if (one.cherry_y < 0 && one.oldCherry_y < 0 && one.cherry_y > one.oldCherry_y) {
            vy1 = one.cherry_y + one.oldCherry_y;

        } else {
            vy1 = one.cherry_y - one.oldCherry_y;
            Log.v("oh", "ohyep" + vy1);
            Log.v("oh", "old" + oldCherry_y);
            Log.v("oh", "new" + cherry_y);
        }
        return vy1;
    }

    public void setBouncingBallView(BouncingBallView bouncingBallView) {
        this.bouncingBallView = bouncingBallView;
    }

    public int getX_dir() {
        return x_dir;
    }

    public int getY_dir() {
        return y_dir;
    }
}

