package com.theendlessgame.app;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.theendlessgame.gameobjects.Enemy;
import com.theendlessgame.logic.GameController;
import com.theendlessgame.gameobjects.Player;
import com.theendlessgame.gameobjects.Shot;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Christian on 01/06/2014.
 */
public class UIThread implements Runnable {

    private static UIThread _Instance = null;
    private Thread _Thread;
    private  GameActivity _GameActivity;
    private boolean _Stop = false;
    private ImageView wayBackground;
    private ImageView intersectionBackground;
    private ImageView wayIndicator;
    private TextView lblScore;

    private final int VISITED_INTERSECTION_POINTS = 1;
    private final int UNVISITED_INTERSECTION_POINTS = 3;
    private final int REFRESH_DELAY = 50;

    private HashMap<Integer, Integer> _Backgrounds;
    private HashMap<Boolean,Integer> _Scores;

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
        wayBackground = (ImageView)_GameActivity.findViewById(R.id.wayBackground);
        intersectionBackground = (ImageView)_GameActivity.findViewById(R.id.intersectionBackground);
        wayIndicator = (ImageView)_GameActivity.findViewById(R.id.wayIndicator);
        lblScore = (TextView)_GameActivity.findViewById(R.id.lblScore);
        _Backgrounds = new HashMap<Integer, Integer>();
        _Scores = new HashMap<Boolean, Integer>();
        _Backgrounds.put(0, R.drawable.fondo);
        _Backgrounds.put(1,R.drawable.fondo_1_camino);
        _Backgrounds.put(2,R.drawable.fondo_2_caminos);
        _Backgrounds.put(3,R.drawable.fondo_3_caminos);
        _Scores.put(true, VISITED_INTERSECTION_POINTS);
        _Scores.put(false, UNVISITED_INTERSECTION_POINTS);
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
                        refreshEnemies();
                        refreshShots();
                        refreshBackground();
                    }
                });
                Thread.sleep(REFRESH_DELAY);
            }
            catch (InterruptedException e) {}
        }
    }
    private void refreshEnemies(){
        int iEnemy = Enemy.getToRemove();
        if (iEnemy != -1){
            removeEnemy(iEnemy);
        }
        for (iEnemy = 0; iEnemy < GameController.getInstance().getCurrentIntersection().getEnemies().size(); iEnemy++) {
            Enemy enemy = GameController.getInstance().getCurrentIntersection().getEnemies().get(iEnemy);
            _GameActivity.setObjectLane(_GameActivity.getImgEnemies().get(iEnemy), enemy.getLaneNum(), enemy.getPosY());
        }

    }

    private void removeEnemy(int pIEnemy){
        ImageView imgEnemy = _GameActivity.getImgEnemies().get(pIEnemy);
        RelativeLayout relativeLayoutGame = (RelativeLayout) _GameActivity.findViewById(R.id.rLayoutGame);
        relativeLayoutGame.removeView(imgEnemy);
        _GameActivity.getImgEnemies().remove(pIEnemy);
        _GameActivity.setContentView(relativeLayoutGame);
    }

    private void refreshShots(){
        if (Shot.getShotsToAdd().size() !=0){
            Shot shot = Shot.getShotsToAdd().get(0);
            _GameActivity.addShot(shot.getLaneNum(),shot.getPosY());
            Shot.getShotsToAdd().remove(0);
        }
        for (int iShot = 0; iShot < GameController.getInstance().getCurrentIntersection().getShots().size(); iShot++) {
            Shot shot = GameController.getInstance().getCurrentIntersection().getShots().get(iShot);
            _GameActivity.setObjectLane(_GameActivity.getImgShots().get(iShot), shot.getLaneNum(), shot.getPosY());
        }
        int iShot = Shot.getToRemove();
        if (iShot != -1){
            removeShot(iShot);
        }
    }

    private void removeShot(int pIShot){
        ImageView imgShot = _GameActivity.getImgShots().get(pIShot);
        RelativeLayout relativeLayoutGame = (RelativeLayout) _GameActivity.findViewById(R.id.rLayoutGame);
        relativeLayoutGame.removeView(imgShot);
        _GameActivity.getImgShots().remove(pIShot);
        _GameActivity.setContentView(relativeLayoutGame);
        Shot.setToRemove(-1);
    }

    private void refreshBackground(){
        if(wayBackground.getY() >= _GameActivity.getScreenHeight()) {
            wayBackground.setY(-_GameActivity.getScreenHeight());
        }
        else{
            int intersectionsCount = GameController.getInstance().getNextIntersectionPathsCount();
            if (intersectionBackground.getY() >= _GameActivity.getScreenHeight()) {
                intersectionBackground.setY(-_GameActivity.getScreenHeight());
                intersectionBackground.setRotation(0);
                intersectionBackground.setLayoutParams(new RelativeLayout.LayoutParams(intersectionBackground.getWidth(), (int)_GameActivity.getScreenHeight()));
                intersectionBackground.setBackgroundResource(_Backgrounds.get(intersectionsCount));
            }
            else if(intersectionBackground.getY() == 16){
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
        }
        wayBackground.setY(wayBackground.getY() + 16);
        intersectionBackground.setY(intersectionBackground.getY() + 16);
    }

    private void refreshWayIndicator(GameController.Direction pDirection){
        if(pDirection == GameController.Direction.LEFT)
            wayIndicator.setRotation(-90);
        else if(pDirection == GameController.Direction.CENTER)
            wayIndicator.setRotation(0);
        else
            wayIndicator.setRotation(90);

    }
    private void goToLeft(){
        intersectionBackground.setRotation(90f);
        intersectionBackground.setLayoutParams(new RelativeLayout.LayoutParams((int) _GameActivity.getScreenHeight(), intersectionBackground.getWidth()));
        intersectionBackground.setY(_GameActivity.getScreenHeight() / 1.5f);
        wayBackground.setY(-_GameActivity.getScreenHeight()/3f);
        destroy();
        GameController.getInstance().goToDirection(GameController.Direction.LEFT);
        Player.getInstance().addPoints(_Scores.get(GameController.getInstance().wasCurrentIntersectionVisited()));
        lblScore.setText("Score: " + Player.getInstance().getScore());
        refreshWayIndicator(GameController.getInstance().getBestWay());
    }

    private void goToRight(){
        intersectionBackground.setRotation(-90f);
        intersectionBackground.setLayoutParams(new RelativeLayout.LayoutParams((int)_GameActivity.getScreenHeight(), intersectionBackground.getWidth()));
        intersectionBackground.setY(_GameActivity.getScreenHeight()/1.5f);
        wayBackground.setY(-_GameActivity.getScreenHeight()/3f);
        destroy();
        GameController.getInstance().goToDirection(GameController.Direction.RIGHT);
        Player.getInstance().addPoints(_Scores.get(GameController.getInstance().wasCurrentIntersectionVisited()));
        lblScore.setText("Score: " + Player.getInstance().getScore());
        refreshWayIndicator(GameController.getInstance().getBestWay());
    }

    private void goToCenter(){
        destroy();
        GameController.getInstance().goToDirection(GameController.Direction.CENTER);
        Player.getInstance().addPoints(_Scores.get(GameController.getInstance().wasCurrentIntersectionVisited()));
        lblScore.setText("Score: " + Player.getInstance().getScore());
        refreshWayIndicator(GameController.getInstance().getBestWay());
    }

    private void removeShots(){
        for (int iShot = 0; iShot < GameController.getInstance().getCurrentIntersection().getShots().size(); iShot++) {
            GameController.getInstance().getCurrentIntersection().getShots().get(iShot).setStop(true);
            removeShot(0);
        }
        GameController.getInstance().getCurrentIntersection().setShots(new ArrayList<Shot>());
    }

    private void removeEnemies(){
        for (int iEnemy = 0; iEnemy < GameController.getInstance().getCurrentIntersection().getEnemies().size(); iEnemy++) {
            GameController.getInstance().getCurrentIntersection().getEnemies().get(iEnemy).setStop(true);
            removeEnemy(0);
        }
        GameController.getInstance().getCurrentIntersection().setEnemies(new ArrayList<Enemy>());
    }

    private void destroy(){
        /*_GameActivity.getImgEnemies().clear();
        _GameActivity.getImgShots().clear();
        GameLogic.getInstance().getCurrentIntersection().getEnemies().clear();
        GameLogic.getInstance().getCurrentIntersection().getShots().clear();*/
        removeEnemies();
        removeShots();
    }
    public void setGameActivity(GameActivity _GameActivity) {
        this._GameActivity = _GameActivity;
    }
}
