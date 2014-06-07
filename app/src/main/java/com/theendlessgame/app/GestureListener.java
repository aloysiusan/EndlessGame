package com.theendlessgame.app;

import android.view.GestureDetector;
import android.view.MotionEvent;

import com.theendlessgame.logic.GameController;
import com.theendlessgame.gameobjects.Player;
import com.theendlessgame.gameobjects.Shot;

public class GestureListener extends GestureDetector.SimpleOnGestureListener {

    protected GestureListener(){}

    public boolean onSwipeRight() {
        if (Player.getInstance().moveRight())
            GameActivity.getInstance().setPlayerLane(Player.getInstance().getLaneNum());
        return true;
    }

    public boolean onSwipeLeft() {
        if (Player.getInstance().moveLeft())
            GameActivity.getInstance().setPlayerLane(Player.getInstance().getLaneNum());
        return true;
    }

    public boolean onSwipeTop() {
        return false;
    }

    public boolean onSwipeBottom() {
        return false;
    }


    @Override
    public boolean onSingleTapUp(MotionEvent e){
        if (Player.getInstance().getArm().getShots() != 0) {
            int range = Player.getInstance().getArm().getRange();
            if (range == 1) {
                Shot shot = new Shot(Player.getInstance().getLaneNum(), (int) GameActivity.getInstance().getScreenHeight() - SHOT_OFFSET, false);
                GameController.getInstance().getCurrentIntersection().addShot(shot);
                shot.startThread();
            } else {
                for (int amountShot = 0; amountShot != range; amountShot++) {
                    if ((Player.getInstance().getLaneNum() - 1 + amountShot) > 0 && (Player.getInstance().getLaneNum() - 1 + amountShot) <= MAX_LANE) {
                        Shot shot = new Shot(Player.getInstance().getLaneNum() - 1 + amountShot, (int) GameActivity.getInstance().getScreenHeight() - SHOT_OFFSET, false);
                        GameController.getInstance().getCurrentIntersection().addShot(shot);
                        shot.startThread();
                    }
                }
            }
            Player.getInstance().getArm().setShots(Player.getInstance().getArm().getShots()-1);
        }
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
    private final int SWIPE_THRESHOLD = 100;
    private final int SWIPE_VELOCITY_THRESHOLD = 100;
    private final int SHOT_OFFSET = 350;
    private final int MAX_LANE = 5;
}
