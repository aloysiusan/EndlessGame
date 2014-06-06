package com.theendlessgame.gameobjects;

import com.theendlessgame.app.GameActivity;
import java.util.ArrayList;
import java.util.Random;

public class Intersection {
    private ArrayList<Enemy> _Enemies = new ArrayList<Enemy>();
    private ArrayList<Shot> _Shots = new ArrayList<Shot>();
    private final long _ID;

    public Intersection(long pRootNodeId){
       _ID = pRootNodeId;
        byte enemiesCount = (byte)(_ID%3 + 1);
        Random rand = new Random();
        while (enemiesCount > 0){
            addEnemy(rand.nextInt(5) + 1, enemiesCount*100, enemiesCount*150);
            enemiesCount--;
        }
    }

    private void addEnemy(int pLaneNumber, int pPosYEnemy, int pPosyShot){
        GameActivity.getInstance().addEnemy(pLaneNumber, pPosYEnemy);
        Enemy enemy = new Enemy(pLaneNumber,pPosYEnemy, pPosyShot);
        _Enemies.add(enemy);
        enemy.startThread();
    }

    public void addShot(Shot pShot){
        _Shots.add(pShot);
    }
    public void removeEnemy(int pEnemyIndex){
        _Enemies.remove(pEnemyIndex);
    }
    public void removeShot(int pShotIndex) {_Shots.remove(pShotIndex);}
    public ArrayList<Enemy> getEnemies() {
        return _Enemies;
    }

    public void setEnemies(ArrayList<Enemy> _Enemies) {
        this._Enemies = _Enemies;
    }

    public void setShots(ArrayList<Shot> _Shots) {
        this._Shots = _Shots;
    }

    public ArrayList<Shot> getShots() {
        return _Shots;
    }
}
