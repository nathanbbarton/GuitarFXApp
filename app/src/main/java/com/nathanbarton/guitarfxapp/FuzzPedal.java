package com.nathanbarton.guitarfxapp;

/**
 * Created by NathanBarton on 2016-12-07.
 */

public class FuzzPedal extends Pedal {

    private double gain;
    private double mix;


    FuzzPedal(){

        super("Fuzz");

        gain = 1;
        mix  = 1;

        fxParameters = new double[2]; //2 changeable parameters

        fxParameters[0] = gain;
        fxParameters[1] = mix;

    }

    //Main effects application
    //input the audio buffer
    //output modified audio buffer
    //Algorithm used found on page 125 DAFX - Digital Audio Effects
    @Override
    public short[] applyFx(short[] audioBuffer){

        double maxAbsoluteOfBuffer = 0, maxAbsoluteOfZ = 0, maxAbsoluteOfY = 0;

        double[] q = new double[audioBuffer.length];
        double[] z = new double[audioBuffer.length];
        double[] y = new double[audioBuffer.length];
        short[] returnBuffer = new short[audioBuffer.length];

        for (int i=0; i < audioBuffer.length; i++){
            if(Math.abs(audioBuffer[i]) > maxAbsoluteOfBuffer){
                maxAbsoluteOfBuffer =  Math.abs(audioBuffer[i]);//find max absolute value
            }
        }

        for (int i=0; i < audioBuffer.length; i++) {
            q[i] = (audioBuffer[i] * gain) / maxAbsoluteOfBuffer; //normalize
        }

        for (int i=0; i < audioBuffer.length; i++) {
            z[i] = (Math.signum(-q[i]) * (1 - Math.exp(Math.signum(-q[i]))) * q[i]); //apply equation
            if(z[i] > maxAbsoluteOfZ){
                maxAbsoluteOfZ = z[i];
            }
        }

        for (int i=0; i < audioBuffer.length; i++) {
            y[i] = (mix * z[i] * (maxAbsoluteOfBuffer/maxAbsoluteOfZ) + (1 - mix) * audioBuffer[i]); //mix modified and original sound
            if(y[i] > maxAbsoluteOfY){
                maxAbsoluteOfY = y[i];
            }
        }

        for (int i=0; i < audioBuffer.length; i++) {
            returnBuffer[i] = (short)(y[i] * maxAbsoluteOfBuffer / maxAbsoluteOfY); //final modification
        }

        return returnBuffer; //output modified data
    }

    @Override
    public void updateParameters(){

        gain = fxParameters[0];
        mix  = fxParameters[1];

    }
}
