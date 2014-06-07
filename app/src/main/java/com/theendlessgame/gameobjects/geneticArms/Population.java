package com.theendlessgame.gameobjects.geneticArms;


import com.theendlessgame.gameobjects.Arm;

import java.util.ArrayList;
import java.util.Hashtable;

public class Population {

    private Population (){
        setColorBits();
        setColors();
        setRangeBits();
        setThicknessBits();
        setPointsBits();
    }    
    private static void createInstance(){
        if (_Instance == null)
            _Instance = new Population();
    }
    public static synchronized Population getInstance(){
        createInstance();
        return _Instance;        
    }
    public void addArm(Arm pArm){
        if (_PreviousArms.size() == 0)
            _PreviousArms.add(pArm);
        else if (pArm.getBitRange() > _PreviousArms.get(0).getBitRange() )
            _PreviousArms.add(pArm);
        else{
            int numArm = 0;
            for(int iArm = 0 ;iArm != _PreviousArms.size(); iArm++){
                if (pArm.getBitRange() <= _PreviousArms.get(iArm).getBitRange())
                    numArm = iArm;
            }
            _PreviousArms.add(numArm+1, pArm);
        }
        verifyLenght();
    }
    private void verifyLenght(){
        if (_PreviousArms.size() > 20){
            _PreviousArms.remove(20);
        }
    }
   
    private void setColorBits(){
        int bitsXColor = 256 / _ColorAmount;
        for( int iColor = 0; iColor != _ColorAmount; iColor++){
            _ColorsBits.put(iColor, bitsXColor*iColor);
        }        
    }
    private void setColors(){
        _Colors.put(0, new Color(255,0,0));
        _Colors.put(1, new Color(0,255,0));
        _Colors.put(2, new Color(0,0,255));
    }
    private void setRangeBits(){
        double bitsXRange = Math.round(256.0 / (double)_MaxRange);
        for( int iRange = 0; iRange != _MaxRange; iRange++){
            _RangeBits.put(iRange, (int)(bitsXRange*iRange));
        }  
    }
    private void setThicknessBits(){
        double bitsXThickness = Math.round(256.0 / 10.0);
        for( int iThickness = 0; iThickness != 10; iThickness++){
            _ThicknessBits.put(iThickness, (int)(bitsXThickness*iThickness));
        } 
    }
    private void setPointsBits(){
        double bitsXPoint = Math.round(256.0 / 3.0);
        for( int iPoints = 0; iPoints != 4; iPoints++){
            _PointsBits.put(iPoints, (int)(bitsXPoint*iPoints));
        } 
    }    
    public ArrayList <Arm> getPreviousArms() {
        return _PreviousArms;
    }

    public Hashtable<Integer, Integer> getColorBits() {
        return _ColorsBits;
    }

    public Hashtable<Integer, Color> getColors() {
        return _Colors;
    }

    public Hashtable<Integer, Integer> getRangeBits() {
        return _RangeBits;
    }

    public int getMaxRange() {
        return _MaxRange;
    }

    public Hashtable<Integer, Integer> getThicknessBits() {
        return _ThicknessBits;
    }

    public Hashtable<Integer, Integer> getPointsBits() {
        return _PointsBits;
    }

    private ArrayList <Arm>  _PreviousArms = new ArrayList<Arm>();
    private Hashtable <Integer, Color> _Colors = new Hashtable <Integer, Color>();
    private Hashtable <Integer, Integer> _ColorsBits = new Hashtable <Integer, Integer>();
    private Hashtable <Integer, Integer> _RangeBits = new Hashtable <Integer, Integer>();
    private Hashtable <Integer, Integer> _ThicknessBits = new Hashtable <Integer, Integer>();
    private Hashtable <Integer, Integer> _PointsBits = new Hashtable <Integer, Integer>();
    private static Population _Instance = null;
    private final int _ColorAmount = 4;
    private final int _MaxRange = 3;

}
