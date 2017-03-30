package com.nathanbarton.guitarfxapp;

/**
 * Created by NathanBarton on 2016-11-01.
 */

public class DistortionPedal extends Pedal{

    private static final String TAG = "DistortionPedal";

    private double gain,      //The Amount of Distortion
                   Q,         //work point Controls the linearity of the transfer function for low input levels, more negative = more linear
                   dist,      //controls the distortions character, a higher number gives a harder distortion
                   rh,        //absolute value of rh < 1, but close to 1. Placement of poles in the High Pass Filter
                   rl,        //0<rl<1. The pole placement in the low pass filter used to simulate capacitances in a tube amplifier
                   mix;       //mix of original and distored sound, 1 = only distorted


    DistortionPedal(){

        super("Distortion");

        fxParameters = new double[4]; //4 changable parameters

        gain = 20.0;
        Q    = -5.0;
        dist = 5.0;
        mix  = 1.0;
        rl   = 2;
        rh   = 1;

        fxParameters[0] = gain; //associate parameters to elements in the parameters array
        fxParameters[1] = Q;
        fxParameters[2] = dist;
        fxParameters[3] = mix;

    }

    //Main effects application
    //input the audio buffer
    //output modified audio buffer
    //Algorithm used found on page 123-124 DAFX - Digital Audio Effects
    @Override
    public short[] applyFx(short[] audioBuffer){

        short maxAbsoluteOfBuffer = 0,maxAbsoluteOfZ = 0,maxAbsoluteOfY = 0;

        double[] q = new double[audioBuffer.length];
        double[] z = new double[audioBuffer.length];
        double[] y = new double[audioBuffer.length];
        short[] returnBuffer = new short[audioBuffer.length];

        for (int i=0; i < audioBuffer.length; i++){
            if(Math.abs(audioBuffer[i]) > maxAbsoluteOfBuffer){
                maxAbsoluteOfBuffer = (short) Math.abs(audioBuffer[i]); //Find max absolute value
            }
        }

        for (int i=0; i < audioBuffer.length; i++) {
            q[i] = (audioBuffer[i] * gain) / maxAbsoluteOfBuffer; //normalize with gain

            if(Q==0){
                z[i] = q[i] / (1 - Math.exp(-dist * q[i]));
            } else {
                z[i] = (Q /(1 - Math.exp(dist * Q))) + ((q[i] - Q) / (1 - Math.exp(-dist * (q[i] - Q)))); //apply equation to buffer
            }
        }

        //check for outlier cases
        if (Q == 0) {
            for (int i = 0; i < q.length; i++){
                if (q[i] == Q){
                    z[i] = 1 / dist;
                }
            }
        } else {
            for (int i = 0; i < q.length; i++){
                if (q[i] == Q){
                    z[i] =  ( 1 /  ((dist + Q)) / ((1 - Math.exp(dist * Q))));
                }
            }
        }

        for (int i=0; i < z.length; i++){
            if(Math.abs(z[i]) > maxAbsoluteOfZ){
                maxAbsoluteOfZ = (short) Math.abs(z[i]); //find new absolute max value
            }
        }

        for (int i=0; i < y.length; i++){
            y[i] = (mix * z[i] * maxAbsoluteOfBuffer) / maxAbsoluteOfZ + (1 - mix) * audioBuffer[i]; //modify again

            if(Math.abs(y[i]) > maxAbsoluteOfY){
                maxAbsoluteOfY = (short) Math.abs(y[i]); //new max absolute value
            }
        }

        for (int i=0; i < y.length; i++){
            returnBuffer[i] = (short)(y[i] * maxAbsoluteOfBuffer / maxAbsoluteOfY); //final modification

        }

        returnBuffer = Filters.lowPassFilter(returnBuffer,rl); //run through low pass filter

        return returnBuffer; //return modified buffer

    }

    @Override
    public void updateParameters(){

        //update the parameters associated with the array
        gain = fxParameters[0];
        Q    = fxParameters[1];
        dist = fxParameters[2];
        mix  = fxParameters[3];
    }

}


