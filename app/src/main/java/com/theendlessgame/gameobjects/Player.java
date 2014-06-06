package com.theendlessgame.gameobjects;

public class Player {
    private static Player _Instance = null;
    private int _Score = 0;
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

    public void addPoints(int pAmount){
        _Score += pAmount;
    }

    public int getScore(){
        return _Score;
    }
    public int getLaneNum() {
        return _LaneNum;
    }
}
