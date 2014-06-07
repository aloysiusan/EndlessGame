package com.theendlessgame.gameobjects;


import com.theendlessgame.gameobjects.geneticArms.Adaptability;
import com.theendlessgame.gameobjects.geneticArms.Color;
import com.theendlessgame.gameobjects.geneticArms.Population;
import com.theendlessgame.logic.GameController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Random;

public class Arm extends GameObject {

    public Arm(){}
    public Arm(Arm pMother){
        Arm newArm = Adaptability.getInstance().applyFuncton(pMother);
        Population.getInstance().addArm(_ActualArm);
        if (Population.getInstance().getPreviousArms().size() > 20){
            Population.getInstance().getPreviousArms().remove(20);
        }
        Player.getInstance().set_Arm(newArm);
        Arm.setActualArm(newArm);
    }
    public Arm(int pLaneNumber, int pPosY){
        setLaneNum(pLaneNumber);
        setPosY(pPosY);
        setThread(new Thread(this));

        GameController.getInstance().getCurrentIntersection().set_Arm(this);
    }
    @Override
    public void playerColission() throws InterruptedException {
        Arm randomArm = createRandomArm();
        new Arm(randomArm);
        Arm.setRefreshImg(1);
    }
    @Override
    public void removeObject() throws InterruptedException {
        GameController.getInstance().getCurrentIntersection().set_Arm(null);
        _ToRemove = 0;
    }

    protected static Arm createRandomArm(){
        Arm randomArm = new Arm();
        Random rn = new Random();

        randomArm.setBitRange((byte)rn.nextInt(256));
        randomArm.setRange((int)((randomArm.getBitRange() & 0xFF) / (256.0/3.0))+1);

        randomArm.setBitThickness((byte)rn.nextInt(256));
        randomArm.setThickness((int)((randomArm.getBitThickness() & 0xFF) / (256.0/10.0))+5);

        randomArm.setBitCantPoints((byte)rn.nextInt(192));
        randomArm.setAmountPoints((int)((randomArm.getBitCantPoints() & 0xFF) / (256.0/4.0))+3);


        randomArm.setBitColor((byte)rn.nextInt(192));
        for (int iColor = 2; iColor != -1; iColor--){
            if ((randomArm.getBitColor() & 0xFF) > Population.getInstance().getColorBits().get(iColor)){
                randomArm.setColor(Population.getInstance().getColors().get(iColor));
                break;
            }
        }
        return randomArm;
    }
    public HashMap<Integer, ArrayList<Integer>> getPoints(){
        int gradesXPoint = 360 / this.getAmountPoints();
        int lenght = 60 ;
        int gradeXlenght = 90 / lenght;
        HashMap<Integer,ArrayList<Integer>> points = new HashMap<Integer,ArrayList<Integer>>();
        ArrayList <Integer> point = new ArrayList<Integer>();
        point.add(lenght);
        point.add(0);
        points.put(0,point);
        for(int iPoint = 0; iPoint != this.getAmountPoints()-1; iPoint++){
            point = new ArrayList<Integer>();
            if (gradesXPoint*(iPoint+1)/90 < 1){
                point.add(lenght- (iPoint+1)*gradesXPoint/gradeXlenght);
                point.add(0 - (iPoint+1)*gradesXPoint/gradeXlenght);
            }
            else if (gradesXPoint*(iPoint+1)/90 < 2){
                point.add(0 - ((iPoint+1)*gradesXPoint-90)/gradeXlenght);
                point.add(-lenght + ((iPoint+1)*gradesXPoint-90)/gradeXlenght);
            }
            else if (gradesXPoint*(iPoint+1)/90 < 3){
                point.add(-lenght +  ((iPoint+1)*gradesXPoint-180)/gradeXlenght);
                point.add(0 + ((iPoint+1)*gradesXPoint-180)/gradeXlenght);
            }
            else{
                point.add(0 + ((iPoint+1)*gradesXPoint-270)/gradeXlenght);
                point.add(lenght - ((iPoint+1)*gradesXPoint-270)/gradeXlenght);
            }
            points.put(iPoint+1,point);
        }
        return points;
    }


    public static int getRefreshImg() {
        return refreshImg;
    }

    public static void setRefreshImg(int refreshImg) {
        Arm.refreshImg = refreshImg;
    }

    public static void setActualArm(Arm _ActualArm) {
        Arm._ActualArm = _ActualArm;
    }

    public Color getColor() {
        return _Color;
    }

    public void setColor(Color _Color) {
        this._Color = _Color;
    }

    public int getRange() {
        return _Range;
    }

    public void setRange(int _Range){
        this._Range = _Range;
    }

    public int getThickness() {
        return _Thickness;
    }

    public void setThickness(int _Thickness) {
        this._Thickness = _Thickness;
    }

    public int getAmountPoints() {
        return _amountPoints;
    }

    public void setAmountPoints(int _CantPoints) {
        this._amountPoints = _CantPoints;
    }

    public byte getBitColor() {
        return _BitColor;
    }

    public void setBitColor(byte _BitColor) {
        this._BitColor = _BitColor;
    }

    public byte getBitRange() {
        return _BitRange;
    }

    public void setBitRange(byte _BitRange) {
        this._BitRange = _BitRange;
    }

    public byte getBitThickness() {
        return _BitThickness;
    }

    public void setBitThickness(byte _BitThickness) {
        this._BitThickness = _BitThickness;
    }

    public byte getBitCantPoints() {
        return _BitCantPoints;
    }

    public static int get_ToRemove() {
        return _ToRemove;
    }

    public static void set_ToRemove(int _ToRemove) {
        Arm._ToRemove = _ToRemove;
    }

    public int getShots() {
        return _Shots;
    }

    public void setShots(int _Shots) {
        this._Shots = _Shots;
    }

    public static int getToAdd() {
        return _ToAdd;
    }

    public static void setToAdd(int pToAdd) {
        Arm._ToAdd = pToAdd;
    }

    public void setBitCantPoints(byte _BitCantPoints) {
        this._BitCantPoints = _BitCantPoints;
    }

    private static Arm _ActualArm;
    private static int _ToRemove = -1;
    private static int _ToAdd = -1;
    private static int refreshImg = -1;
    private int _Shots = 3;
    private Color _Color;
    private byte _BitColor;
    private int _Range;
    private byte _BitRange;
    private int _Thickness;
    private byte _BitThickness;
    private int _amountPoints;
    private byte _BitCantPoints;

}
