package com.theendlessgame.Model;

import com.theendlessgame.app.GameActivity;
import java.util.ArrayList;

public class Intersection {
    private ArrayList<Enemy> _Enemies = new ArrayList<Enemy>();
    private ArrayList<Shot> _Shots = new ArrayList<Shot>();
    private final long _ID;

    public Intersection(long pRootNodeId){
       _ID = pRootNodeId;
        int enemiesCount = (int)_ID%3 + 1;
        while (enemiesCount > 0){
            addEnemy(enemiesCount, enemiesCount*100, enemiesCount*100);
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

    public ArrayList<Enemy> getEnemies() {
        return _Enemies;
    }

    public ArrayList<Shot> getShots() {
        return _Shots;
    }
}
