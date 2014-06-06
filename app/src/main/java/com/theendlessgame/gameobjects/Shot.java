package com.theendlessgame.gameobjects;

import com.theendlessgame.logic.GameController;

import java.util.ArrayList;

public class Shot extends GameObject {
    private static int _ToRemove = -1;
    private static ArrayList<Shot> _ShotsToAdd = new ArrayList<Shot>();
    private boolean _IsToPlayer;

    public Shot(int pLaneNumber, int pPosY,boolean pToPlayer){
        setLaneNum(pLaneNumber);
        setPosY(pPosY);
        _IsToPlayer = pToPlayer;
        if (!_IsToPlayer)
            setSpeed(getSpeed()*-5);
        else
            setSpeed(getSpeed()*5);
        setThread(new Thread(this));
        _ShotsToAdd.add(this);

    }
    @Override
    protected int verifyEnemyColission(){
        if (!_IsToPlayer) {
            for (int iEnemy = 0; iEnemy != GameController.getInstance().getCurrentIntersection().getEnemies().size(); iEnemy++) {
                Enemy tempEnemy = GameController.getInstance().getCurrentIntersection().getEnemies().get(iEnemy);
                if (this.getPosY() < tempEnemy.getPosY()+100 && tempEnemy.getLaneNum() == this.getLaneNum()) {
                    return iEnemy;
                }
            }
        }
        return -1;
    }
    @Override
    protected void removeObject() throws InterruptedException {
        int iShot = GameController.getInstance().getCurrentIntersection().getShots().indexOf(this);
        GameController.getInstance().getCurrentIntersection().removeShot(iShot);
        getThread().sleep(100);
        _ToRemove = iShot;

    }
    @Override
    protected void playerColission(){
        Player.getInstance().reduceLife();
    }
    @Override
    protected void enemyColission(int iEnemy){
        Enemy.removeObject(iEnemy);
    }

    public static int getToRemove() {
        return _ToRemove;
    }

    public static void setToRemove(int toRemove) {
        Shot._ToRemove = toRemove;
    }

    public static ArrayList<Shot> getShotsToAdd() {
        return _ShotsToAdd;
    }

    public static void setShotsToAdd(ArrayList<Shot> _ShotsToAdd) {
        Shot._ShotsToAdd = _ShotsToAdd;
    }
}
