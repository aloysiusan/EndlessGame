package com.theendlessgame.gameobjects;

import com.theendlessgame.app.GameActivity;
import java.util.ArrayList;
import java.util.Random;

public class Intersection {

    public Intersection(long pRootNodeId){
       _ID = pRootNodeId;
        byte enemiesCount = (byte)(_ID%MAX_ENEMIES + 1);
        Enemy.set_ToAdd(enemiesCount);
        Arm.setToAdd(enemiesCount);
        Player.getInstance().get_Arm().setShots(MAX_SHOTS);
    }

    public void addEnemy(int pLaneNumber, int pPosYEnemy, int pPosyShot){
        GameActivity.getInstance().addEnemy(pLaneNumber, pPosYEnemy);
        Enemy enemy = new Enemy(pLaneNumber,pPosYEnemy, pPosyShot);
        _Enemies.add(enemy);
        enemy.startThread();
    }
    public void addArm(int pLaneNumber, int pPosY){
        GameActivity.getInstance().addArm(pLaneNumber,pPosY);
        Arm arm = new Arm(pLaneNumber,pPosY);
        arm.startThread();
    }

    public long getID(){
        return _ID;
    }
    public void addShot(Shot pShot){
        _Shots.add(pShot);
    }
    public synchronized void  removeEnemy(int pEnemyIndex){
        _Enemies.remove(pEnemyIndex);
    }
    public synchronized void removeShot(int pShotIndex) {_Shots.remove(pShotIndex);}
    public ArrayList<Enemy> getEnemies() {
        return _Enemies;
    }

    public Arm get_Arm() {
        return _Arm;
    }

    public void set_Arm(Arm _Arm) {
        this._Arm = _Arm;
    }

    public ArrayList<Shot> getShots() {
        return _Shots;
    }

    private ArrayList<Enemy> _Enemies = new ArrayList<Enemy>();
    private ArrayList<Shot> _Shots = new ArrayList<Shot>();
    private Arm _Arm = null;
    private final long _ID;
    private final int MAX_SHOTS = 3;
    private final int MAX_ENEMIES = 3;
}
