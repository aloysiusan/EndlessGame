package com.theendlessgame.Model;

public class Enemy extends ObjectOnWay{

    private int _AmountShots = 0;
    private int _PosYShot;
    private final int _MaxAmountShots = 1;
    private static int _ToRemove = -1;


    public Enemy( int pLineNumber, int pPosYEnemy, int pPosyShot){
        set_PosY(pPosYEnemy);
        set_LaneNum(pLineNumber);
        _PosYShot = pPosyShot;
        set_Thread(new Thread(this));
    }

    @Override
    protected boolean onShot(){
        if (get_PosY() >= _PosYShot)
            return true;
        else
            return false;
    }
    @Override
    protected boolean createShot(){
        if (_AmountShots < _MaxAmountShots ){
            Shot shot = new Shot(get_LaneNum(),_PosYShot,true);
            shot.startThread();
            //GameActivity.getInstance().addShot(get_LaneNum(), _PosYShot);
            Intersection.get_ActualIntersection().addShot(shot);
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
        int iEnemy = Intersection.get_ActualIntersection().get_Enemies().indexOf(this);
        Intersection.get_ActualIntersection().removeEnemy(iEnemy);
        get_Thread().sleep(100);
        _ToRemove = iEnemy;
    }
    public static void removeObject(int iEnemy){
        System.out.println("removing by shot");
        System.out.println(iEnemy);
        Intersection.get_ActualIntersection().get_Enemies().get(iEnemy).set_Stop(true);
        //System.out.println(Intersection.get_ActualIntersection().get_Enemies().get(0).is_Stop());
        Intersection.get_ActualIntersection().removeEnemy(iEnemy);
        _ToRemove = iEnemy;
    }

    public static int get_ToRemove() {
        return _ToRemove;
    }

    public static void set_ToRemove(int _ToRemove) {
        Enemy._ToRemove = _ToRemove;
    }



}
