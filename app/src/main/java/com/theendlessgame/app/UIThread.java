package com.theendlessgame.app;

import android.app.ActionBar;
import android.view.animation.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.theendlessgame.Model.Enemy;
import com.theendlessgame.Logic.GameLogic;
import com.theendlessgame.Model.Player;
import com.theendlessgame.Model.Shot;

import java.util.HashMap;

/**
 * Created by Christian on 01/06/2014.
 */
public class UIThread implements Runnable {

    private static UIThread _Instance = null;
    private Thread _Thread;
    private  GameActivity _GameActivity;
    private boolean _Stop = false;
    private ImageView background;
    private ImageView background2;
    private ImageView wayIndicator;

    private HashMap<Integer, Integer> _Backgrounds;

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
        background = (ImageView)_GameActivity.findViewById(R.id.imageView);
        background2 = (ImageView)_GameActivity.findViewById(R.id.imageView2);
        wayIndicator = (ImageView)_GameActivity.findViewById(R.id.imageView3);
        _Backgrounds = new HashMap<Integer, Integer>();
        _Backgrounds.put(0, R.drawable.fondo);
        _Backgrounds.put(1,R.drawable.fondo_1_camino);
        _Backgrounds.put(2,R.drawable.fondo_2_caminos);
        _Backgrounds.put(3,R.drawable.fondo_3_caminos);
        refreshWayIndicator(GameLogic.getInstance().getBestWay());
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
                Thread.sleep(45);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private void refreshEnemies(){
        for (int iEnemy = 0; iEnemy < GameLogic.getInstance().getCurrentIntersection().getEnemies().size(); iEnemy++) {
            Enemy enemy = GameLogic.getInstance().getCurrentIntersection().getEnemies().get(iEnemy);
            _GameActivity.setObjectLane(_GameActivity.getImgEnemies().get(iEnemy), enemy.getLaneNum(), enemy.getPosY());
        }
        int iEnemy = Enemy.getToRemove();
        if (iEnemy != -1){
            removeEnemy(iEnemy);
        }
    }

    private void removeEnemy(int pIEnemy){
        ImageView imgEnemy = _GameActivity.getImgEnemies().get(pIEnemy);
        RelativeLayout relativeLayoutGame = (RelativeLayout) _GameActivity.findViewById(R.id.rLayoutGame);
        relativeLayoutGame.removeView(imgEnemy);
        _GameActivity.getImgEnemies().remove(pIEnemy);
        _GameActivity.setContentView(relativeLayoutGame);
        Enemy.setToRemove(-1);
    }

    private void refreshShots(){
        if (Shot.getShotsToAdd().size() !=0){
            Shot shot = Shot.getShotsToAdd().get(0);
            _GameActivity.addShot(shot.getLaneNum(),shot.getPosY());
            Shot.getShotsToAdd().remove(0);
        }
        for (int iShot = 0; iShot < GameLogic.getInstance().getCurrentIntersection().getShots().size(); iShot++) {
            Shot shot = GameLogic.getInstance().getCurrentIntersection().getShots().get(iShot);
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
        if(background.getY() >= _GameActivity.getScreenHeight()) {
            background.setY(-_GameActivity.getScreenHeight());
        }
        else{
            int intersectionsCount = GameLogic.getInstance().getNextIntersectionPathsCount();
            if (background2.getY() >= _GameActivity.getScreenHeight()) {
                background2.setY(-_GameActivity.getScreenHeight());
                background2.setRotation(0);
                background2.setLayoutParams(new RelativeLayout.LayoutParams(background2.getWidth(), (int)_GameActivity.getScreenHeight()));
                background2.setBackgroundResource(_Backgrounds.get(intersectionsCount));
            }
            else if(background2.getY() == 16){
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
        background.setY(background.getY() + 16);
        background2.setY(background2.getY() + 16);
    }

    private void refreshWayIndicator(GameLogic.Direction pDirection){
        if(pDirection == GameLogic.Direction.LEFT)
            wayIndicator.setRotation(-90);
        else if(pDirection == GameLogic.Direction.CENTER)
            wayIndicator.setRotation(0);
        else
            wayIndicator.setRotation(90);

    }
    private void goToLeft(){
        background2.setRotation(90f);
        background2.setLayoutParams(new RelativeLayout.LayoutParams((int)_GameActivity.getScreenHeight(), background2.getWidth()));
        background2.setY(_GameActivity.getScreenHeight()/1.5f);
        background.setY(-_GameActivity.getScreenHeight()/3f);
        GameLogic.getInstance().goToDirection(GameLogic.Direction.LEFT);
        refreshWayIndicator(GameLogic.getInstance().getBestWay());
    }

    private void goToRight(){
        background2.setRotation(-90f);
        background2.setLayoutParams(new RelativeLayout.LayoutParams((int)_GameActivity.getScreenHeight(), background2.getWidth()));
        background2.setY(_GameActivity.getScreenHeight()/1.5f);
        background.setY(-_GameActivity.getScreenHeight()/3f);
        GameLogic.getInstance().goToDirection(GameLogic.Direction.RIGHT);
        refreshWayIndicator(GameLogic.getInstance().getBestWay());
    }

    private void goToCenter(){
        GameLogic.getInstance().goToDirection(GameLogic.Direction.CENTER);
        refreshWayIndicator(GameLogic.getInstance().getBestWay());
    }

    public GameActivity getGameActivity() {
        return _GameActivity;
    }

    public void setGameActivity(GameActivity _GameActivity) {
        this._GameActivity = _GameActivity;
    }
}
