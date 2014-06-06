package com.theendlessgame.gameobjects;


import com.theendlessgame.gameobjects.geneticArms.Adaptability;
import com.theendlessgame.gameobjects.geneticArms.Color;
import com.theendlessgame.gameobjects.geneticArms.Population;
import com.theendlessgame.logic.GameController;

import java.util.Random;

public class Arm extends GameObject {
    private static Arm _ActualArm;
    private static int _ToRemove = -1;
    private static int _ToAdd = -1;
    private int _Shots = 3;
    private Color _Color;
    private byte _BitColor;
    private int _Range;
    private byte _BitRange;
    private int _Thickness;
    private byte _BitThickness;
    private int _amountPoints; 
    private byte _BitCantPoints;
    
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
        //Player.getInstance().set_Arm(this);
        //Arm.setActualArm(this);
        GameController.getInstance().getCurrentIntersection().set_Arm(this);
    }
    @Override
    public void playerColission() throws InterruptedException {
        Arm randomArm = createRandomArm();
        new Arm(randomArm);
    }
    @Override
    public void removeObject() throws InterruptedException {
        //Player.getInstance().set_Arm(null);
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

    public static Arm getActualArm() {
        return _ActualArm;
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

    public int get_Shots() {
        return _Shots;
    }

    public void set_Shots(int _Shots) {
        this._Shots = _Shots;
    }

    public static int get_ToAdd() {
        return _ToAdd;
    }

    public static void set_ToAdd(int pToAdd) {
        Arm._ToAdd = pToAdd;
    }

    public void setBitCantPoints(byte _BitCantPoints) {
        this._BitCantPoints = _BitCantPoints;
    }  
    public void printArm(){
        //System.out.println("-------------Actual Arm----------");
        //System.out.println("Cantidad de puntos: "+_ActualArm.getAmountPoints());
        //System.out.println("Rango: "+_ActualArm.getRange());
        //System.out.println("Grosor: "+_ActualArm.getThickness());
        //System.out.println("Color: "+_ActualArm.getColor().toString());
    }
}
