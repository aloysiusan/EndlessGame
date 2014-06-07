package com.theendlessgame.gameobjects;
import com.theendlessgame.logic.GameController;

import java.util.ArrayList;

public class Enemy extends GameObject {

    private int _AmountShots = 0;
    private int _PosYShot;
    private final int _MaxAmountShots = 1;
    private static ArrayList<Integer> _ToRemove = new ArrayList<Integer>();
    private static int _ToAdd = 0;


    public Enemy( int pLineNumber, int pPosYEnemy, int pPosyShot){
        setPosY(pPosYEnemy);
        setLaneNum(pLineNumber);
        _PosYShot = pPosyShot;
        setThread(new Thread(this));
    }

    @Override
    protected boolean onShot(){
        if (getPosY() >= _PosYShot)
            return true;
        else
            return false;
    }
    @Override
    protected boolean createShot(){
        if (_AmountShots < _MaxAmountShots ){
            Shot shot = new Shot(getLaneNum(),_PosYShot,true);
            shot.startThread();
            GameController.getInstance().getCurrentIntersection().addShot(shot);
            ++_AmountShots;
            return true;
        }
        return false;
    }
    @Override
    public void playerColission() throws InterruptedException {
        Player.getInstance().reduceLife();
    }
    @Override
    public void removeObject() throws InterruptedException {
        int iEnemy = GameController.getInstance().getCurrentIntersection().getEnemies().indexOf(this);
        GameController.getInstance().getCurrentIntersection().removeEnemy(iEnemy);
        _ToRemove.add(iEnemy);
    }
    public static void removeObject(int iEnemy){
        GameController.getInstance().getCurrentIntersection().getEnemies().get(iEnemy).setStop(true);
        GameController.getInstance().getCurrentIntersection().removeEnemy(iEnemy);
        _ToRemove.add(iEnemy);
    }

    public static synchronized int getToRemove() {
        int toRemove = -1;
        if (_ToRemove.size() == 0){
            return toRemove;
        }
        else{
            toRemove  = _ToRemove.get(0);
            _ToRemove.remove(0);
            return toRemove;
        }

    }

    public static int getToAdd() {
        return _ToAdd;
    }

    public static void setToAdd(int _ToAdd) {
        Enemy._ToAdd = _ToAdd;
    }
}
