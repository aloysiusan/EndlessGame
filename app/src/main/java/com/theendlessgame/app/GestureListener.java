package com.theendlessgame.app;

import android.view.GestureDetector;
import android.view.MotionEvent;

import com.theendlessgame.Logic.GameLogic;
import com.theendlessgame.Model.Intersection;
import com.theendlessgame.Model.Player;
import com.theendlessgame.Model.Shot;

/**
 * Created by Christian on 28/05/2014.
 */
public class GestureListener extends GestureDetector.SimpleOnGestureListener {
    private final int SWIPE_THRESHOLD = 100;
    private final int SWIPE_VELOCITY_THRESHOLD = 100;

    protected GestureListener(){
        System.out.println("created gestureListener");
    }
    public boolean onSwipeRight() {
        System.out.println("DERECHA");
        if (Player.getInstance().moveRight())
            GameActivity.getInstance().setPlayerLane(Player.getInstance().get_LaneNum());
        return true;
    }

    public boolean onSwipeLeft() {
        System.out.println("IZQUIERDA");
        if (Player.getInstance().moveLeft())
            GameActivity.getInstance().setPlayerLane(Player.getInstance().get_LaneNum());
        return true;
    }

    public boolean onSwipeTop() {
        return false;
    }

    public boolean onSwipeBottom() {
        return false;
    }


    @Override
    public boolean onDoubleTap(MotionEvent e){
        Shot shot = new Shot(Player.getInstance().get_LaneNum(), (int)GameActivity.getInstance().get_ScreenHeight()-350, false);
        GameLogic.getInstance().getCurrentIntersection().addShot(shot);
        shot.startThread();
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        boolean result = false;
        try {
            float diffY = e2.getY() - e1.getY();
            float diffX = e2.getX() - e1.getX();
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        result = onSwipeRight();
                    } else {
                        result = onSwipeLeft();
                    }
                }
            } else {
                if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        result = onSwipeBottom();
                    } else {
                        result = onSwipeTop();
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return result;
    }

}
