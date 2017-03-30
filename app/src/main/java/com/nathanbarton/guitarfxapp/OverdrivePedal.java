package com.nathanbarton.guitarfxapp;

/**
 * Created by NathanBarton on 2016-11-08.
 */

public class OverdrivePedal extends Pedal {

    private double mult;
    private double topTH;
    private double bottomTH;

    OverdrivePedal(){

        super("Overdrive");

        mult        = 1.0;
        topTH       = 1.0;
        bottomTH    = 3.0;

        fxParameters = new double[3]; //3 changeable parameters

        fxParameters[0] = mult;
        fxParameters[1] = topTH;
        fxParameters[2] = bottomTH;

    }

    //Main effects application
    //input the audio buffer
    //output modified audio buffer
    //Algorithm used found on page 118 DAFX - Digital Audio Effects
    @Override
    public short[] applyFx(short[] audioBuffer){

        double th = topTH/bottomTH;
        double multiplier = mult/32767; // normalize input to double -1,1
        double out = 0.0;

        for(int i = 0;i < audioBuffer.length;i++){

            double in = multiplier * (double)audioBuffer[i]; //apply multiplier value (gain)
            double absIn = Math.abs(in);

            if(absIn <= th){
                out = (audioBuffer[i] * 2 * multiplier); //less then threshold multiply by 2
            }
            else if(absIn < 2 * th){
                if(in > 0) {
                    out = (3 - (2 - in * 3) * (2 - in * 3)) / 3;
                }
                else if(in<0) {
                    out = -(3 - (2 - absIn * 3) * (2 - absIn * 3)) / 3; //in between thresholds apply the compression equation
                }
            }
            else if(absIn >= 2 * th){ //greater than threshold set to 1
                if(in>0){
                    out = 1;
                }
                else if(in<0){
                    out = -1;
                }
            }
            audioBuffer[i] = (short)(out/multiplier); //output final modified data
        }

        return audioBuffer;

    }

    @Override
    public void updateParameters(){
        mult     = fxParameters[0];
        topTH    = fxParameters[1];
        bottomTH = fxParameters[2];
    }

}
