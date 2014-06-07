package com.theendlessgame.gameobjects;

import com.theendlessgame.gameobjects.geneticArms.Population;

public class Player {

    private Player(){
        Population.getInstance();
        Arm initialArm = Arm.createRandomArm();
        Population.getInstance().addArm(initialArm);
        this._Arm = initialArm;
        Arm.setActualArm(initialArm);
        Arm.setRefreshImg(1);
    }

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

    public int getLivesCount(){
        return _Lives;
    }

    public Arm getArm() {
        return _Arm;
    }

    public void setArm(Arm _Arm) {
        this._Arm = _Arm;
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

    private static Player _Instance = null;
    private int _Score = 0;
    private int _Lives = 3;
    private Arm _Arm = null;
    private int _LaneNum = 3;
}
