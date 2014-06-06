package com.theendlessgame.gameobjects;

import com.theendlessgame.app.GameActivity;
import java.util.ArrayList;
import java.util.Random;

public class Intersection {
    private ArrayList<Enemy> _Enemies = new ArrayList<Enemy>();
    private ArrayList<Shot> _Shots = new ArrayList<Shot>();
    private Arm _Arm = null;
    private final long _ID;

    public Intersection(long pRootNodeId){
       _ID = pRootNodeId;
        byte enemiesCount = (byte)(_ID%3 + 1);


        Random rand = new Random();
        Enemy.set_ToAdd(enemiesCount);
//        while (enemiesCount > 0){
//            addEnemy(rand.nextInt(5) + 1, enemiesCount*100, enemiesCount*150);
//            enemiesCount--;
//        }
        Arm.set_ToAdd(enemiesCount);
        Player.getInstance().get_Arm().set_Shots(3);
        //addArm(rand.nextInt(5) + 1, enemiesCount*100);

    }

    public void addEnemy(int pLaneNumber, int pPosYEnemy, int pPosyShot){
        GameActivity.getInstance().addEnemy(pLaneNumber, pPosYEnemy);
        Enemy enemy = new Enemy(pLaneNumber,pPosYEnemy, pPosyShot);
        _Enemies.add(enemy);
        enemy.startThread();
    }
    public void addArm(int pLaneNumber, int pPosY){
        GameActivity.getInstance().addArm(pLaneNumber,pPosY);
        System.out.println("agregaddaaaaaa");
        Arm arm = new Arm(pLaneNumber,pPosY);
        arm.startThread();
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

    public void setEnemies(ArrayList<Enemy> _Enemies) {
        this._Enemies = _Enemies;
    }

    public void setShots(ArrayList<Shot> _Shots) {
        this._Shots = _Shots;
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
}
