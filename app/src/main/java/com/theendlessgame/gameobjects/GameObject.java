package com.theendlessgame.gameobjects;

import com.theendlessgame.app.GameActivity;

public class GameObject implements Runnable {

    public GameObject(){
        _PosY = 0;
    }

    public void startThread(){
        _Thread.start();
    }
    @Override
    public void run() {
        try {
            while (!_Stop) {
                _PosY += _Offset;
                if (verifyPlayerColission()) {
                    playerColission();
                    removeObject();
                    _Stop = true;
                }
                if (isObjectHeightMax()){
                    removeObject();
                    _Stop = true;
                }
                int iEnemyOnColission = verifyEnemyColission();
                if (iEnemyOnColission != -1){
                    enemyColission(iEnemyOnColission);
                    removeObject();
                    _Stop = true;
                }
                if (onShot()){
                    createShot();
                }
                _Thread.sleep(SPEED);
            }
            if (_Stop) {
                _Offset = 0;
                _Thread.interrupt();
            }

        }catch (InterruptedException e) {}
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
        return _Offset;
    }

    public void setSpeed(int _Speed) {
        this._Offset = _Speed;
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

    private int _PosY;
    private int _LaneNum;
    private int _Offset = 16;
    private boolean _Stop = false;
    private Thread _Thread;
    private int SPEED = 50;

}
