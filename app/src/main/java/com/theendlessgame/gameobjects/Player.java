package com.theendlessgame.gameobjects;

import com.theendlessgame.gameobjects.geneticArms.Population;

public class Player {
    private static Player _Instance = null;
    private int _Score = 0;
    private int _Lives = 3;
    private Arm _Arm = null;
    private int _LaneNum = 3;
    private Player(){
        Population.getInstance();
        Arm initialArm = Arm.createRandomArm();
        Population.getInstance().addArm(initialArm);
        this._Arm = initialArm;
        System.out.println("arma inicial :");
        System.out.println(_Arm.getRange());
        Arm.setActualArm(initialArm);
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

    public Arm get_Arm() {
        return _Arm;
    }

    public void set_Arm(Arm _Arm) {
        System.out.println("Agregado arma a jugador");
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
}
