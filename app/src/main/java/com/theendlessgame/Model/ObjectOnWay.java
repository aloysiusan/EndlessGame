package com.theendlessgame.Model;

import com.theendlessgame.app.GameActivity;

public class ObjectOnWay implements Runnable {
    private int _PosY;
    private int _LaneNum;
    private int _Speed = 15;
    private boolean _Stop = false;
    private Thread _Thread;

    public ObjectOnWay(){
        _PosY = 1000;
    }
    public void startThread(){
        _Thread.start();
    }
    @Override
    public void run() {
        try {
            while (!_Stop) {
                _PosY += _Speed;
                if (verifyPlayerColission()) {
                    System.out.println("removed playerColission");
                    playerColission();
                    removeObject();
                    _Stop = true;
                }
                if (isObjectHeightMax()){
                    System.out.println("removed height max:");
                    removeObject();
                    _Stop = true;
                }
                int iEnemyOnColission = verifyEnemyColission();
                if (iEnemyOnColission != -1){
                    System.out.println("enemyColission");
                    enemyColission(iEnemyOnColission);
                    removeObject();
                    _Stop = true;
                }
                if (onShot()){
                    createShot();
                }
                _Thread.sleep(500);
            }
            if (_Stop) {
                _Speed = 0;
                _Thread.join();
            }
        }catch (InterruptedException e) {
                e.printStackTrace();
            }
    }
    protected boolean isObjectHeightMax(){
        if (_PosY > GameActivity.getInstance().get_ScreenHeight() || _PosY < 0 )
            return true;
        else
            return false;
    }
    protected boolean verifyPlayerColission(){
        if ( _PosY >= GameActivity.getInstance().get_ScreenHeight()-300  &&Player.getInstance().get_LaneNum() == _LaneNum )
            return true;
        else
            return false;
    }

    protected int verifyEnemyColission(){
        return -1;
    }
    protected void playerColission() throws InterruptedException {}
    protected void removeObject() throws InterruptedException {};
    protected void enemyColission(int iEnemy) throws InterruptedException {};
    protected boolean onShot() {return false;}
    protected boolean createShot(){return false;}

    public int get_PosY() {
        return _PosY;
    }

    public void set_PosY(int _PosY) {this._PosY = _PosY;}

    public int get_LaneNum() {
        return _LaneNum;
    }

    public void set_LaneNum(int _LaneNum) {
        this._LaneNum = _LaneNum;
    }
    public int get_Speed() {
        return _Speed;
    }

    public void set_Speed(int _Speed) {
        this._Speed = _Speed;
    }

    public boolean is_Stop() {
        return _Stop;
    }


    public void set_Stop(boolean _Stop) {
        this._Stop = _Stop;
    }


    public Thread get_Thread() {
        return _Thread;
    }

    public void set_Thread(Thread _Thread) {
        this._Thread = _Thread;
    }
}
