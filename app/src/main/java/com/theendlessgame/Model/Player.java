package com.theendlessgame.Model;

public class Player {
    private static Player _Instance = null;
    private int _Score;
    private int _Lives = 3;
    private int _Arm;
    private int _LaneNum = 3;
    private Player(){}

    private static void createInstance(){
        if(_Instance == null)
            _Instance = new Player();
    }
    public static synchronized Player getInstance(){
        createInstance();
        return _Instance;
    }
    protected void addPoints(int pAmount){
        _Score += pAmount;
    }
    protected boolean reduceLife(){
        _Lives--;
        if (_Lives == 0)
            return false;
        else
            return true;
    }
    public boolean moveLeft(){
        if (_LaneNum != 1){
            _LaneNum--;
            return true;
        }
        else
            return false;
    }
    public boolean moveRight(){
        if (_LaneNum != 5){
            _LaneNum++;
            return true;
        }
        else
            return false;
    }

    public int getLaneNum() {
        return _LaneNum;
    }
}
