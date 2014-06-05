package com.theendlessgame.Model;

import com.theendlessgame.app.GameActivity;

public class ObjectOnWay implements Runnable {
    private int _PosY;
    private int _LaneNum;
    private int _Speed = 16;
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
                _Thread.sleep(70);
            }
            if (_Stop) {
                _Speed = 0;
                _Thread.interrupt();
            }
        }catch (InterruptedException e) {
                e.printStackTrace();
         }
    }
    protected boolean isObjectHeightMax(){
        if (_PosY > GameActivity.getInstance().getScreenHeight() || _PosY < 0 )
            return true;
        else
            return false;
    }
    protected boolean verifyPlayerColission(){
        if ( _PosY >= GameActivity.getInstance().getScreenHeight()-300  &&Player.getInstance().getLaneNum() == _LaneNum )
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

    public int getPosY() {
        return _PosY;
    }

    public void setPosY(int _PosY) {this._PosY = _PosY;}

    public int getLaneNum() {
        return _LaneNum;
    }

    public void setLaneNum(int _LaneNum) {
        this._LaneNum = _LaneNum;
    }
    public int getSpeed() {
        return _Speed;
    }

    public void setSpeed(int _Speed) {
        this._Speed = _Speed;
    }

    public boolean isStop() {
        return _Stop;
    }


    public void setStop(boolean _Stop) {
        this._Stop = _Stop;
    }


    public Thread getThread() {
        return _Thread;
    }

    public void setThread(Thread _Thread) {
        this._Thread = _Thread;
    }
}
