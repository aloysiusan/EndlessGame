package com.theendlessgame.Model;

import java.util.ArrayList;

public class Shot extends ObjectOnWay{
    private static int _ToRemove = -1;
    private static ArrayList<Shot> _ShotsToAdd = new ArrayList<Shot>();
    private boolean _IsToPlayer;

    public Shot(int pLaneNumber, int pPosY,boolean pToPlayer){
        set_LaneNum(pLaneNumber);
        set_PosY(pPosY);
        _IsToPlayer = pToPlayer;
        if (!_IsToPlayer)
            set_Speed(get_Speed()*-5);
        else
            set_Speed(get_Speed()*5);
        set_Thread(new Thread(this));
        _ShotsToAdd.add(this);
        System.out.println("shot");

    }
    @Override
    protected int verifyEnemyColission(){
        if (!_IsToPlayer) {
            for (int iEnemy = 0; iEnemy != Intersection.get_ActualIntersection().get_Enemies().size(); iEnemy++) {
                Enemy tempEnemy = Intersection.get_ActualIntersection().get_Enemies().get(iEnemy);
                if (this.get_PosY() < tempEnemy.get_PosY()+100 && tempEnemy.get_LaneNum() == this.get_LaneNum()) {
                    return iEnemy;
                }
            }
        }
        return -1;
    }
    @Override
    protected void removeObject() throws InterruptedException {
        int iShot = Intersection.get_ActualIntersection().get_Shots().indexOf(this);
        Intersection.get_ActualIntersection().removeShot(iShot);
        get_Thread().sleep(100);
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

    public static ArrayList<Shot> get_ShotsToAdd() {
        return _ShotsToAdd;
    }

    public static void set_ShotsToAdd(ArrayList<Shot> _ShotsToAdd) {
        Shot._ShotsToAdd = _ShotsToAdd;
    }
}
