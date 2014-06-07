package com.theendlessgame.gameobjects.geneticArms;


import com.theendlessgame.gameobjects.Arm;

import java.util.Random;

public class Crossing {
    private static Crossing _Instance;
    private Arm _Father;
    private Arm _Mother;
    private final Arm _Children = new Arm();
    private final int TWO_BIT_MASK = 192;
    private final int SIX_BIT_MASK = 63;
    private Crossing(){}
    
    private static void createInstance(){
        if (_Instance == null)
            _Instance = new Crossing();
    }
    protected static synchronized Crossing getInstance(){
        createInstance();
        return _Instance;
    }
    protected Arm makeCrossing(Arm pFather, Arm pMother){
        _Father = pFather;
        _Mother = pMother;
        byte newBitPoints = crossBits(pFather.getBitCantPoints(), pMother.getBitCantPoints());
        byte newBitThickness = crossBits(pFather.getBitThickness(), pMother.getBitThickness());
        byte newBitColor = crossBits(pFather.getBitColor(), pMother.getBitColor());
        byte newBitRange = crossBits(pFather.getBitRange(), pMother.getBitRange());
        
        setChildrenColor(newBitColor);
        setChildrenRange(newBitRange);
        setChildrenThickness(newBitThickness);
        setChildrenPoints(newBitPoints);
        return _Children;
    }
    
    private byte crossBits(int pFather, int pMother){
        byte newBit = (byte)((pFather & TWO_BIT_MASK) | (pMother & SIX_BIT_MASK));
        Random rn = new Random();
        int random = rn.nextInt(100);
        if (random < 10)
            newBit = mutateBit(newBit);
        return newBit;
    }
    private byte mutateBit(byte pBits){
        Random rn = new Random();
        int random = rn.nextInt(8);
        if (isBitActivated(pBits, random)){
            return (byte)(pBits - (int)Math.pow(2, random));
        }
        else{
            return (byte)(pBits + (int)Math.pow(2, random));
        }        
    }
    private boolean isBitActivated(int pBits, int pPosition){
        for (int iPos = 7; iPos < 0; iPos-- ){
            if (pBits > Math.pow(2, iPos)){
                pBits -= Math.pow(2, iPos);
                if (iPos == pPosition)
                    return true;
            }
        }
        return false;
    }
    private void setChildrenColor(byte pNewBitColor){
        if ((pNewBitColor & 0xFF) > Population.getInstance().getColorBits().get(3)){
            int diferentColor = (pNewBitColor & 0xFF) - Population.getInstance().getColorBits().get(3);
            int redColor = diferentColor / 4 * 1;
            int greenColor = diferentColor / 4 * 2;
            int blueColor = diferentColor / 4 * 3;
            _Children.setColor(new Color(redColor, greenColor, blueColor));

        }
        else{
            for (int iColor = 2; iColor != -1; iColor --){
                if ((pNewBitColor & 0xFF) >= Population.getInstance().getColorBits().get(iColor)){
                    _Children.setColor(Population.getInstance().getColors().get(iColor));
                    break;
                }
            }
        }
        _Children.setBitColor(pNewBitColor);
    }
    private void setChildrenRange(byte pNewBitRange){
        for (int iRange = Population.getInstance().getMaxRange()-1; iRange != -1; iRange--){
            if ((pNewBitRange & 0xFF) >= Population.getInstance().getRangeBits().get(iRange)){
                _Children.setRange(iRange+1);
                break;
            }
        }
        _Children.setBitRange(pNewBitRange);
    }
    private void setChildrenThickness(byte pNewBitThickness){
        for (int iThickness = 9; iThickness != -1; iThickness--){
            if ((pNewBitThickness & 0xFF) >= Population.getInstance().getThicknessBits().get(iThickness)){
                _Children.setThickness(iThickness+5);
                break;
            }
        }
        _Children.setBitThickness(pNewBitThickness);
    }
    private void setChildrenPoints(byte pNewBitPoints){
        for (int iPoint = 3; iPoint != -1; iPoint--){
            if ((pNewBitPoints & 0xFF) >= Population.getInstance().getPointsBits().get(iPoint)){
                _Children.setAmountPoints(iPoint+3);
                break;
            }
        }
        _Children.setBitCantPoints(pNewBitPoints);
    }

            
            
    
}
