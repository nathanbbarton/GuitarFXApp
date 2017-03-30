package com.nathanbarton.guitarfxapp;

/**
 * Created by NathanBarton on 2016-12-07.
 */

//Abstract Filters class made to simply call its static filtering functions
public abstract class Filters {

    //Low Pass Filter:
    //Input: audioStream, smoothing value
    //output: smoothed audiostream
    static short[] lowPassFilter(short[] input, double rl){

        short value = input[0]; // start with the first input
        for (int i=1; i < input.length; i++){
            short currentValue = input[i];
            value += (currentValue - value) / rl;
            input[i] = value;
        }
        return input;
    }

    //High Pass Filter:
    //Input: audioStream, smoothing value
    //output: smoothed audiostream
    static short[] highPassFilter(short[] input, double rh){

        short[] output = new short[input.length];

        output[0] = input[0];

        for (int i=1; i < input.length; i++){
            output[i] = (short)(rh * (output[i-1] + input[i] - input[i-1]));
        }

        return output;

    }
}
