package com.theendlessgame.gameobjects.geneticArms;

/**
 * Created by Christian on 05/06/2014.
 */
public class Color {
    private int _Red;
    private int _Green;
    private int _Blue;

    public Color(int pRed, int pGreen, int pBlue){
        _Red = pRed;
        _Green = pGreen;
        _Blue = pBlue;
    }

    public int getGreen() {
        return _Green;
    }

    public int getRed() {
        return _Red;
    }

    public int getBlue() {
        return _Blue;
    }
}
