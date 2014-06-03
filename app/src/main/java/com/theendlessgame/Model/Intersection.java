package com.theendlessgame.Model;

import com.theendlessgame.app.GameActivity;
import java.util.ArrayList;
import java.util.Random;

public class Intersection {
    private ArrayList<Enemy> _Enemies = new ArrayList<Enemy>();
    private ArrayList<Shot> _Shots = new ArrayList<Shot>();

    public Intersection(long pRootNodeId){
        Random rn = new Random();
        addEnemy(1,800,rn.nextInt((int)GameActivity.getInstance().get_ScreenHeight()-800)+800);
    }

    public void addEnemy(int pLaneNumber, int pPosYEnemy, int pPosyShot){
        GameActivity.getInstance().addEnemy(pLaneNumber, pPosYEnemy);
        Enemy enemy = new Enemy(pLaneNumber,pPosYEnemy, pPosyShot);
        _Enemies.add(enemy);
        enemy.startThread();
    }

    public void addShot(Shot pShot){_Shots.add(pShot);}
    protected void removeEnemy(int pIEnemy){
        _Enemies.remove(pIEnemy);
    }
    protected void removeShot(int pIShot){
        _Shots.remove(pIShot);
    }

    public ArrayList<Enemy> getEnemies() {
        return _Enemies;
    }

    public ArrayList<Shot> getShots() {
        return _Shots;
    }
}
