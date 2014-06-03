package com.theendlessgame.app;

import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.theendlessgame.Model.Enemy;
import com.theendlessgame.Model.Intersection;
import com.theendlessgame.Model.Shot;

/**
 * Created by Christian on 01/06/2014.
 */
public class UIThread implements Runnable {

    private static UIThread _Instance = null;
    private Thread _Thread;
    private  GameActivity _GameActivity;
    private boolean _Stop = false;

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
        _Thread.start();
    }

    @Override
    public void run() {
        while (!_Stop) {
            try{
            _GameActivity.runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    refreshEnemies();
                    refreshShots();

                }
            });
            _Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private void refreshEnemies(){
        for (int iEnemy = 0; iEnemy != Intersection.get_ActualIntersection().get_Enemies().size(); iEnemy++) {
            Enemy enemy = Intersection.get_ActualIntersection().get_Enemies().get(iEnemy);
            _GameActivity.setObjectLane(_GameActivity.get_ImgEnemies().get(iEnemy), enemy.get_LaneNum(), enemy.get_PosY());
        }
        int iEnemy = Enemy.get_ToRemove();
        if (iEnemy != -1){
            removeEnemy(iEnemy);
        }
    }
    private void removeEnemy(int pIEnemy){
        ImageView imgEnemy = _GameActivity.get_ImgEnemies().get(pIEnemy);
        RelativeLayout relativeLayoutGame = (RelativeLayout) _GameActivity.findViewById(R.id.rLayoutGame);
        relativeLayoutGame.removeView(imgEnemy);
        _GameActivity.get_ImgEnemies().remove(pIEnemy);
        _GameActivity.setContentView(relativeLayoutGame);
        Enemy.set_ToRemove(-1);
    }
    private void refreshShots(){
        if (Shot.get_ShotsToAdd().size() !=0){
            Shot shot = Shot.get_ShotsToAdd().get(0);
            _GameActivity.addShot(shot.get_LaneNum(),shot.get_PosY());
            Shot.get_ShotsToAdd().remove(0);
        }
        for (int iShot = 0; iShot != Intersection.get_ActualIntersection().get_Shots().size(); iShot++) {
            Shot shot = Intersection.get_ActualIntersection().get_Shots().get(iShot);
            _GameActivity.setObjectLane(_GameActivity.get_ImgShots().get(iShot), shot.get_LaneNum(), shot.get_PosY());
        }
        int iShot = Shot.getToRemove();
        if (iShot != -1){
            removeShot(iShot);
        }
    }
    private void removeShot(int pIShot){
        ImageView imgShot = _GameActivity.get_ImgShots().get(pIShot);
        RelativeLayout relativeLayoutGame = (RelativeLayout) _GameActivity.findViewById(R.id.rLayoutGame);
        relativeLayoutGame.removeView(imgShot);
        _GameActivity.get_ImgShots().remove(pIShot);
        _GameActivity.setContentView(relativeLayoutGame);
        Shot.setToRemove(-1);
    }

    public GameActivity get_GameActivity() {
        return _GameActivity;
    }

    public void set_GameActivity(GameActivity _GameActivity) {
        this._GameActivity = _GameActivity;
    }
}
