package com.theendlessgame.Model;
import com.theendlessgame.Logic.GameLogic;
public class Enemy extends ObjectOnWay{

    private int _AmountShots = 0;
    private int _PosYShot;
    private final int _MaxAmountShots = 1;
    private static int _ToRemove = -1;


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
            //GameActivity.getInstance().addShot(get_LaneNum(), _PosYShot);
            GameLogic.getInstance().getCurrentIntersection().addShot(shot);
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
        System.out.println("Enemigo removido");
        int iEnemy = GameLogic.getInstance().getCurrentIntersection().getEnemies().indexOf(this);
        GameLogic.getInstance().getCurrentIntersection().removeEnemy(iEnemy);
        getThread().sleep(100);
        _ToRemove = iEnemy;
    }
    public static void removeObject(int iEnemy){
        System.out.println("removing by shot");
        System.out.println(iEnemy);
        GameLogic.getInstance().getCurrentIntersection().getEnemies().get(iEnemy).setStop(true);
        //System.out.println(Intersection.get_ActualIntersection().get_Enemies().get(0).is_Stop());
        GameLogic.getInstance().getCurrentIntersection().removeEnemy(iEnemy);
        _ToRemove = iEnemy;
    }

    public static int getToRemove() {
        return _ToRemove;
    }

    public static void setToRemove(int _ToRemove) {
        Enemy._ToRemove = _ToRemove;
    }



}
