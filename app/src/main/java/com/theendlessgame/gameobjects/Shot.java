package com.theendlessgame.gameobjects;

import com.theendlessgame.logic.GameController;

import java.util.ArrayList;

public class Shot extends GameObject {

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
        if(iShot != -1) {
            GameController.getInstance().getCurrentIntersection().removeShot(iShot);
            addToRemove(iShot);
            getThread().sleep(100);
        }
    }

    @Override
    protected void playerColission(){
        Player.getInstance().reduceLife();
    }
    @Override
    protected void enemyColission(int iEnemy){
        Enemy.removeObject(iEnemy);
    }

    private static synchronized void addToRemove(int iShot){
        _ToRemove.add(iShot);
    }
    public static synchronized int getToRemove(){
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
    public static synchronized ArrayList<Shot> getShotsToAdd() {
        return _ShotsToAdd;
    }

    private static ArrayList<Integer> _ToRemove = new ArrayList<Integer>();
    private static ArrayList<Shot> _ShotsToAdd = new ArrayList<Shot>();
    private boolean _IsToPlayer;
}
