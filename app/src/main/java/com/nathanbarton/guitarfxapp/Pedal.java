package com.nathanbarton.guitarfxapp;

/**
 * Created by NathanBarton on 2016-11-29.
 */

//All pedals super class
//new pedals must extend this as well as Override applyFx and updateParameters
public abstract class Pedal {

    private String pedalName;
    public  boolean isApplyingFx = false;
    public  double[] fxParameters; //Array of changing parameters for each pedal

    Pedal(String name){
        pedalName = name;
    }

    //Mandatory overdride function to apply an effect
    public abstract short[] applyFx(short[] audioBuffer);
    //mandatory override function to update parameters of effect algorithm
    public abstract void    updateParameters();
    public          String  getPedalName(){ return pedalName; }


}
