package com.theendlessgame.app;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.theendlessgame.gameobjects.Arm;
import com.theendlessgame.gameobjects.Enemy;
import com.theendlessgame.gameobjects.Intersection;
import com.theendlessgame.gameobjects.geneticArms.Color;
import com.theendlessgame.logic.GameController;
import com.theendlessgame.gameobjects.Player;
import com.theendlessgame.gameobjects.Shot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.PriorityQueue;
import java.util.Random;

public class UIThread implements Runnable {

    private UIThread(){
        _Thread = new Thread(this);
    }

    private static void createInstance(){

        if (_Instance == null){
            _Instance = new UIThread();
        }
    }
    public static synchronized UIThread getInstance(){
        createInstance();
        return _Instance;
    }

    protected void start(){
        _Backgrounds = new HashMap<Integer, Integer>();
        _Scores = new HashMap<Boolean, Integer>();
        _AdLogos = new HashMap<Integer, Integer>();

        _Backgrounds.put(0, R.drawable.fondo);
        _Backgrounds.put(1,R.drawable.fondo_1_camino);
        _Backgrounds.put(2,R.drawable.fondo_2_caminos);
        _Backgrounds.put(3,R.drawable.fondo_3_caminos);
        _Scores.put(true, VISITED_INTERSECTION_POINTS);
        _Scores.put(false, UNVISITED_INTERSECTION_POINTS);
        _AdLogos.put(0, R.drawable.adidas);
        _AdLogos.put(1, R.drawable.amazon);
        _AdLogos.put(2, R.drawable.bcr);
        _AdLogos.put(3, R.drawable.coca_cola);
        _AdLogos.put(4, R.drawable.dos_pinos);
        _AdLogos.put(5, R.drawable.imperial);
        _AdLogos.put(6, R.drawable.mc_donalds);
        _AdLogos.put(7, R.drawable.movistar);
        _AdLogos.put(8, R.drawable.sony);
        _AdLogos.put(9, R.drawable.tec);

        refreshWayIndicator(GameController.getInstance().getBestWay());
        _Thread.start();
    }

    @Override
    public void run() {
        while (!_Stop) {
            try {
                _GameActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            refreshEnemies();
                            refreshShots();
                            refreshArm();
                            refreshBackground();
                            refreshLives();
                            refreshArmImg();
                        }
                        catch (Exception e){}
                    }
                });
                Thread.sleep(REFRESH_DELAY);
            }
            catch (Exception e) {}
        }
    }
    private void refreshEnemies(){
        int iEnemy = Enemy.getToRemove();
        if (iEnemy != OBJECT_DELETED_INDEX){
            removeEnemy(iEnemy);
        }
        for (iEnemy = 0; iEnemy < GameController.getInstance().getCurrentIntersection().getEnemies().size(); iEnemy++) {
            Enemy enemy = GameController.getInstance().getCurrentIntersection().getEnemies().get(iEnemy);
            _GameActivity.setObjectLane(_GameActivity.getImgEnemies().get(iEnemy), enemy.getLaneNum(), enemy.getPosY());
        }
        int toAdd = Enemy.get_ToAdd();
        Random rn = new Random();
        while (toAdd > 0) {
            GameController.getInstance().getCurrentIntersection().addEnemy(rn.nextInt(5) + 1, toAdd * ENEMY_POSITION_OFFSET, toAdd * SHOT_POSITION_OFFSET);
            toAdd--;
        }
        Enemy.set_ToAdd(0);


    }
    private void refreshArm(){
        if (Arm.get_ToRemove() != OBJECT_DELETED_INDEX) {
            removeArm();
            Arm.set_ToRemove(OBJECT_DELETED_INDEX);
        }
        if (GameController.getInstance().getCurrentIntersection().get_Arm() != null) {
            _GameActivity.setObjectLane(_GameActivity.getArm(), GameController.getInstance().getCurrentIntersection().get_Arm().getLaneNum(),GameController.getInstance().getCurrentIntersection().get_Arm().getPosY());
        }
        int toAdd = Arm.get_ToAdd();
        Random rn  = new Random();
        if (toAdd != OBJECT_DELETED_INDEX){
            GameController.getInstance().getCurrentIntersection().addArm(rn.nextInt(5) + 1, toAdd*SHOT_POSITION_OFFSET);
        }
        Arm.set_ToAdd(OBJECT_DELETED_INDEX);

    }
    private synchronized void removeArm(){
        ImageView imgArm = _GameActivity.getArm();
        RelativeLayout relativeLayoutGame = (RelativeLayout) _GameActivity.findViewById(R.id.rLayoutGame);
        relativeLayoutGame.removeView(imgArm);
        _GameActivity.setArm(null);
        _GameActivity.setContentView(relativeLayoutGame);
    }

    private synchronized void removeEnemy(int pIEnemy){
        ImageView imgEnemy = _GameActivity.getImgEnemies().get(pIEnemy);
        RelativeLayout relativeLayoutGame = (RelativeLayout) _GameActivity.findViewById(R.id.rLayoutGame);
        relativeLayoutGame.removeView(imgEnemy);
        _GameActivity.getImgEnemies().remove(pIEnemy);
        _GameActivity.setContentView(relativeLayoutGame);
    }

    private void refreshShots(){
        int iShot = Shot.getToRemove();

        if (iShot != OBJECT_DELETED_INDEX){
            removeShot(iShot);
        }
        while (Shot.getShotsToAdd().size() !=0){
            Shot shot = Shot.getShotsToAdd().get(0);
            _GameActivity.addShot(shot.getLaneNum(),shot.getPosY());
            Shot.getShotsToAdd().remove(0);
        }
        for (iShot = 0; iShot < GameController.getInstance().getCurrentIntersection().getShots().size(); iShot++) {
            Shot shot = GameController.getInstance().getCurrentIntersection().getShots().get(iShot);
            _GameActivity.setObjectLane(_GameActivity.getImgShots().get(iShot), shot.getLaneNum(), shot.getPosY());
        }
    }

    private synchronized void removeShot(int pIShot){
        ImageView imgShot = _GameActivity.getImgShots().get(pIShot);
        RelativeLayout relativeLayoutGame = (RelativeLayout) _GameActivity.findViewById(R.id.rLayoutGame);
        relativeLayoutGame.removeView(imgShot);
        _GameActivity.getImgShots().remove(pIShot);
        _GameActivity.setContentView(relativeLayoutGame);
    }

    private void refreshBackground(){
        if(_GameActivity.wayBackground.getY() >= _GameActivity.getScreenHeight()) {
            _GameActivity.wayBackground.setY(-_GameActivity.getScreenHeight());
        }
        else{
            int intersectionsCount = GameController.getInstance().getNextIntersectionPathsCount();
            if (_GameActivity.intersectionBackground.getY() >= _GameActivity.getScreenHeight()) {
                _GameActivity.intersectionBackground.setY(-_GameActivity.getScreenHeight());
                _GameActivity.intersectionBackground.setRotation(NO_ROTATION);
                _GameActivity.intersectionBackground.setLayoutParams(new RelativeLayout.LayoutParams(_GameActivity.intersectionBackground.getWidth(), (int)_GameActivity.getScreenHeight()));
                _GameActivity.intersectionBackground.setBackgroundResource(_Backgrounds.get(intersectionsCount));
            }
            else if(_GameActivity.intersectionBackground.getY() == INTERSECTION_CHANGE_OFFSET){
                if(intersectionsCount == 1)
                    goToLeft();
                else if(intersectionsCount == 2) {
                    if (Player.getInstance().getLaneNum() < 3)
                        goToLeft();
                    else
                        goToCenter();
                }
                else{
                    if(Player.getInstance().getLaneNum() < 3)
                        goToLeft();
                    else if(Player.getInstance().getLaneNum() > 3)
                        goToRight();
                    else
                        goToCenter();
                }
            }

            else if(_GameActivity.wayBackground.getY() == AD_HIDE_OFFSET){
                hideAd();
            }
        }
        _GameActivity.wayBackground.setY(_GameActivity.wayBackground.getY() + GAME_OFFSET);
        _GameActivity.intersectionBackground.setY(_GameActivity.intersectionBackground.getY() + GAME_OFFSET);
    }

    private void refreshWayIndicator(GameController.Direction pDirection){
        if(pDirection == GameController.Direction.LEFT)
            _GameActivity.wayIndicator.setRotation(LEFT_ROTATION);
        else if(pDirection == GameController.Direction.CENTER)
            _GameActivity.wayIndicator.setRotation(NO_ROTATION);
        else
            _GameActivity.wayIndicator.setRotation(RIGHT_ROTATION);

    }

    private void refreshLives(){
        if(Player.getInstance().getLivesCount() == 0) {
            _GameActivity.livesView.setVisibility(View.INVISIBLE);
            _Stop = true;
            _GameActivity.endGame();
        }
        _GameActivity.livesView.setNumStars(Player.getInstance().getLivesCount());
    }

    private void goToLeft(){
        _GameActivity.intersectionBackground.setRotation(RIGHT_ROTATION);
        _GameActivity.intersectionBackground.setLayoutParams(new RelativeLayout.LayoutParams((int) _GameActivity.getScreenHeight(), _GameActivity.intersectionBackground.getWidth()));
        _GameActivity.intersectionBackground.setY(_GameActivity.getScreenHeight() / ON_INTERSECTION_CHANGE_NEW_WAY_DIVISOR);
        _GameActivity.wayBackground.setY(-_GameActivity.getScreenHeight()/ON_INTERSECTION_CHANGE_OLD_WAY_DIVISOR);
        clear();
        GameController.getInstance().goToDirection(GameController.Direction.LEFT);
        Player.getInstance().addPoints(_Scores.get(GameController.getInstance().wasCurrentIntersectionVisited()));
        refreshWayIndicator(GameController.getInstance().getBestWay());
        showAd();
    }

    private void goToRight(){
        _GameActivity.intersectionBackground.setRotation(LEFT_ROTATION);
        _GameActivity.intersectionBackground.setLayoutParams(new RelativeLayout.LayoutParams((int)_GameActivity.getScreenHeight(), _GameActivity.intersectionBackground.getWidth()));
        _GameActivity.intersectionBackground.setY(_GameActivity.getScreenHeight()/ON_INTERSECTION_CHANGE_NEW_WAY_DIVISOR);
        _GameActivity.wayBackground.setY(-_GameActivity.getScreenHeight()/ON_INTERSECTION_CHANGE_OLD_WAY_DIVISOR);
        clear();
        GameController.getInstance().goToDirection(GameController.Direction.RIGHT);
        Player.getInstance().addPoints(_Scores.get(GameController.getInstance().wasCurrentIntersectionVisited()));
        refreshWayIndicator(GameController.getInstance().getBestWay());
        showAd();
    }

    private void goToCenter(){
        clear();
        GameController.getInstance().goToDirection(GameController.Direction.CENTER);
        Player.getInstance().addPoints(_Scores.get(GameController.getInstance().wasCurrentIntersectionVisited()));
        refreshWayIndicator(GameController.getInstance().getBestWay());
        showAd();
    }

    private void refreshArmImg(){
        if (Arm.getRefreshImg() != OBJECT_DELETED_INDEX) {
            ImageView arm = GameActivity.getInstance().getImgActualArm();
            int width = (int) GameActivity.convertDpToPixel(ARM_SIZE, GameActivity.getInstance().getApplicationContext());
            int height = (int) GameActivity.convertDpToPixel(ARM_SIZE, GameActivity.getInstance().getApplicationContext());

            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(bmp);
            Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
            Color color = Player.getInstance().get_Arm().getColor();
            p.setARGB(255, color.getRed(), color.getGreen(), color.getBlue());
            p.setStrokeWidth(Player.getInstance().get_Arm().getThickness());
            HashMap<Integer, ArrayList<Integer>> points = Player.getInstance().get_Arm().getPoints();
            HashMap<Integer, ArrayList<Integer>> pointsCenter = new HashMap<Integer, ArrayList<Integer>>();
            int amountPoints = Player.getInstance().get_Arm().getAmountPoints();
            for (int iPoint = 0; iPoint != amountPoints; iPoint++) {
                int x = points.get(iPoint).get(X) + width / 2;
                int y = points.get(iPoint).get(Y) + height / 2;
                ArrayList<Integer> point = new ArrayList<Integer>();
                point.add(x);
                point.add(y);
                pointsCenter.put(iPoint, point);
            }
            for (int iPoint = 0; iPoint != amountPoints-1; iPoint++) {
                c.drawLine(pointsCenter.get(iPoint).get(X), pointsCenter.get(iPoint).get(Y), pointsCenter.get(iPoint + 1).get(X), pointsCenter.get(iPoint + 1).get(Y), p);
            }
            c.drawLine(pointsCenter.get(amountPoints-1).get(X), pointsCenter.get(amountPoints-1).get(Y), pointsCenter.get(0).get(X), pointsCenter.get(0).get(Y), p);
            arm.setImageBitmap(bmp);
            Arm.setRefreshImg(OBJECT_DELETED_INDEX);
        }

    }

    private void removeShots(){
        while (GameController.getInstance().getCurrentIntersection().getShots().size() != 0){
            GameController.getInstance().getCurrentIntersection().removeShot(0);
            removeShot(0);
        }
    }

    private void removeEnemies(){

        while (GameController.getInstance().getCurrentIntersection().getEnemies().size() != 0){
            GameController.getInstance().getCurrentIntersection().removeEnemy(0);
            removeEnemy(0);
        }
    }


    private void clear(){
        removeEnemies();
        removeShots();
        removeArm();
    }

    private void showAd() {
        int logoIndex = (int) ((GameController.getInstance().getCurrentIntersection().getID() / 2) % 10);
        int score = Player.getInstance().getScore();
        _GameActivity.adLogoView.setImageResource(_AdLogos.get(logoIndex));
        _GameActivity.lblNodeId.setText(String.valueOf(GameController.getInstance().getCurrentIntersection().getID()));
        _GameActivity.lblScore.setText("Score: " + score + "pts");
        if(GameController.getInstance().wasCurrentIntersectionVisited()){
            _GameActivity.adView.setBackgroundColor(VISITED_AD_COLOR);
        }
        else{
            _GameActivity.adView.setBackgroundColor(DEFAULT_AD_COLOR);
        }
        _GameActivity.adView.setVisibility(View.VISIBLE);
    }

    private void hideAd(){
        _GameActivity.adView.setVisibility(View.INVISIBLE);
    }

    public void setGameActivity(GameActivity _GameActivity) {
        this._GameActivity = _GameActivity;
    }


    private static UIThread _Instance = null;
    private Thread _Thread;
    private  GameActivity _GameActivity;
    private boolean _Stop = false;

    private final int VISITED_INTERSECTION_POINTS = 1;
    private final int UNVISITED_INTERSECTION_POINTS = 3;
    private final int REFRESH_DELAY = 50;

    private final int DEFAULT_AD_COLOR = 0xc8000000;
    private final int VISITED_AD_COLOR = 0xc800aeff;
    private final int INTERSECTION_CHANGE_OFFSET = 16;
    private final int GAME_OFFSET = 16;
    private final int AD_HIDE_OFFSET = 128;
    private final int LEFT_ROTATION = -90;
    private final int NO_ROTATION = 0;
    private final int RIGHT_ROTATION = 90;
    private final int OBJECT_DELETED_INDEX = -1;
    private final int ON_INTERSECTION_CHANGE_OLD_WAY_DIVISOR = 3;
    private final float ON_INTERSECTION_CHANGE_NEW_WAY_DIVISOR = 1.5f;
    private final int ARM_SIZE = 60;
    private final int X = 0;
    private final int Y = 1;
    private final int ENEMY_POSITION_OFFSET = 80;
    private final int SHOT_POSITION_OFFSET = 150;

    private HashMap<Integer, Integer> _Backgrounds;
    private HashMap<Boolean,Integer> _Scores;
    private HashMap<Integer, Integer> _AdLogos;
}
