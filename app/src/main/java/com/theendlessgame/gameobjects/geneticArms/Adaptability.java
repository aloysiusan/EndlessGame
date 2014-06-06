 package com.theendlessgame.gameobjects.geneticArms;

import com.theendlessgame.gameobjects.Arm;

import java.util.Hashtable;
import java.util.Random;


public class Adaptability {
    private static Adaptability _Instance;
    private int _SelectedArm;
    private Hashtable <Byte, Byte> _FitnessArms;
    private final int porcent = 50;
        
    private Adaptability(){}
    
    private static void createInstance(){
        if (_Instance == null)
            _Instance = new Adaptability();    
    }
    public static synchronized Adaptability getInstance(){
        createInstance();
        return _Instance;
    }
    public Arm applyFuncton(Arm pMother){
        getFitnessArms();
        Random rn = new Random();
        System.out.println("size"+_FitnessArms.size());
        byte random = (byte)rn.nextInt(_FitnessArms.size());
        System.out.println("random"+random);
        _SelectedArm = _FitnessArms.get(random);
        return Crossing.getInstance().makeCrossing(pMother, Population.getInstance().getPreviousArms().get(_SelectedArm));
    }
    
    protected void getFitnessArms(){
        byte amountSelected = (byte)Math.round(Population.getInstance().getPreviousArms().size() * ((double)porcent / 100.0));
        _FitnessArms = new Hashtable <Byte, Byte>();
        if (amountSelected == 0)
            _FitnessArms.put((byte)0, (byte)0);
        for (byte iArm = 0; iArm != amountSelected; iArm++){
            _FitnessArms.put(iArm, iArm);
        }
    }
    
}
