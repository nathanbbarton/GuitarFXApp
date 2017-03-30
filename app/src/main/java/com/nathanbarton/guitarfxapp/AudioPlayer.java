package com.nathanbarton.guitarfxapp;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.util.Log;

/**
 * Created by NathanBarton on 2016-11-08.
 */

public class AudioPlayer{

    private static final String TAG = "AudioPlayer"; //Debugging Tag

    private AudioRecord    audioRecord  = null;      //AudioRecord reads in the audio buffer stream
    private AudioManager   audioManager = null;      //AudioManager used for android audio managment
    private AudioTrack     audioTrack   = null;      //AudioTrack saves what AudioRecord captured and outputs it
    private Thread         audioThread  = null;      //Audio streaming has to run in thread or will interfere with UI
    public  Pedal          pedal;                    //Pedal to apply effect to

    public  static int  FREQUENCY = 44100;           //Standard android Sample Rate

    public  long    latency   = 0;                    //Used to measure input-output latency
    public  boolean isPlaying = false;
    private long    startTime;
    private long    endTime;
    private int     encoding;                        //Format the streams will be set to
    private int     bufferSize;                      //size of the audiobuffer
    private short[] audioBuffer;                     //audiobuffer for reading and writing

    //Constructor:
    //Input: AudioManager and type of buffer used for setting encoding
    AudioPlayer(AudioManager am, int bufferType){


        //Currently Redundant switch case current API does not support AudioFormat.ENCODING_PCM_FLOAT
        //Here for future updates and different pedals
        switch (bufferType) {
            case 1:
                pedal = new OverdrivePedal();
                encoding = AudioFormat.ENCODING_PCM_16BIT;
                bufferSize = AudioRecord.getMinBufferSize(FREQUENCY, AudioFormat.CHANNEL_IN_MONO, encoding);
                break;
            case 2:
                pedal = new DistortionPedal();
                encoding = AudioFormat.ENCODING_PCM_16BIT;
                bufferSize = AudioRecord.getMinBufferSize(FREQUENCY,AudioFormat.CHANNEL_IN_MONO,encoding);
                break;
            default:
                break;

        }

        audioManager = am;

    }

    //Starts the loop to record and play audio
    public void startAudio(){

        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

        //Make sure their is no feedback in the audioStreams
        refreshAudioStreams();

        //Start the AudioRecord and AudioTrack
        audioRecord.startRecording();
        audioTrack.play();

        //Deprecated call to check for plugged in headset
        //Without when unplugging headphones feedback loop can start
        if(audioManager.isWiredHeadsetOn()) {
            //while audioThread is running
            while (!audioThread.isInterrupted()) {
                try {

                    startTime = System.currentTimeMillis(); //start latency timing

                    audioRecord.read(audioBuffer, 0, audioBuffer.length); //read input data

                    //If applying an effect do so otherwise simply output the input stream
                    if (pedal.isApplyingFx) {
                        audioTrack.write(pedal.applyFx(audioBuffer), 0, bufferSize);
                    } else {
                        audioTrack.write(audioBuffer, 0, bufferSize);
                    }

                    //Calculate latency
                    endTime = System.currentTimeMillis();
                    latency = endTime - startTime;

                } catch (Throwable t) {
                    Log.e("Error", "Read write failed");
                }
            }
        } else {
            stopPlayback();//Stop thread if headset is unplugged
        }
    }


    public void startPlayback(){

        //Initialize a new thread on each new start,restarting same thread causes errors
        audioThread = new Thread(new Runnable() {
            @Override
            public void run() {
                startAudio();
            }
        });

        audioThread.start();
    }

    public void stopPlayback(){

        //make sure thread to be stopped is actually running
        if (audioThread == null) { return; }
        if (audioThread.isAlive()){
            Log.d(TAG,"AudioThread getting interrupted");
            audioThread.interrupt(); //stop thread
            audioRecord.stop();      //stop recording
            audioTrack.pause();      //pause the output
            audioTrack.flush();      //flush all the date out of the output stream
            audioTrack.stop();       //stop the output stream

        }
    }

    public void refreshAudioStreams(){

        //Initialize both audioTrack and AudioRecord
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, FREQUENCY, AudioFormat.CHANNEL_IN_MONO, encoding, bufferSize * 3);
        audioTrack  = new AudioTrack(AudioManager.STREAM_MUSIC, FREQUENCY, AudioFormat.CHANNEL_OUT_MONO, encoding, bufferSize * 3, AudioTrack.MODE_STREAM);

        //Set Sample Rate and initialize the audioBuffer
        audioTrack.setPlaybackRate(FREQUENCY);
        audioBuffer = new short[bufferSize];

    }
}
