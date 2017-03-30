package com.nathanbarton.guitarfxapp;

import android.content.Context;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by NathanBarton on 2016-12-05.
 */

public class DistortionActivity extends PedalActivity {

    private static final String TAG = "DistortionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE); //Initialize audio manager

        audioPlayer = new AudioPlayer(am, BUFFER_TYPE_FLOAT); //give audio player the audio manager


        //SET UP OF EFFECT SPECIFIC UI

        //Set pedal name and font
        TextView pedalName = (TextView) findViewById(R.id.pedal_name);
        Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/Note_this.ttf");
        pedalName.setTypeface(tf);

        //Initialize proper number of seekbars
        SeekBar gainControl       = (SeekBar) findViewById(R.id.gain_control);
        SeekBar QControl          = (SeekBar) findViewById(R.id.Q_control);
        SeekBar distortionControl = (SeekBar) findViewById(R.id.distortion_control);
        SeekBar mixControl        = (SeekBar) findViewById(R.id.mix_control);

        //Set their starting values
        distortionControl.setProgress(19);
        gainControl.setProgress(4);
        QControl.setProgress(4);
        mixControl.setProgress(3);

        //Initialize text views, final because they have to be accessed inside seek bar listeners
        final TextView gainValue       = (TextView) findViewById(R.id.gain_value);
        final TextView QValue          = (TextView) findViewById(R.id.Q_value);
        final TextView distortionValue = (TextView) findViewById(R.id.distortion_value);
        final TextView mixValue        = (TextView) findViewById(R.id.mix_value);

        //set their starting values
        gainValue.setText("5");
        QValue.setText("-4");
        distortionValue.setText("20");
        mixValue.setText("1");

        //SET SEEK BAR LISTENERS
        //Any new seek bad must implement a listener that updates its respective pedal effect parameter
        //EX: gainControl updates the gain parameter in audioPlayer.pedal

        gainControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioPlayer.pedal.fxParameters[0] = progress + 1;
                audioPlayer.pedal.updateParameters();

                gainValue.setText(String.valueOf(progress + 1));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        QControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioPlayer.pedal.fxParameters[1] = -1 * (progress + 1);
                audioPlayer.pedal.updateParameters();

                QValue.setText(String.valueOf(-1 * (progress + 1)));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });


        distortionControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioPlayer.pedal.fxParameters[2] = progress + 1;
                audioPlayer.pedal.updateParameters();

                distortionValue.setText(String.valueOf(progress + 1));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        mixControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioPlayer.pedal.fxParameters[3] = (double)(progress / 2);
                audioPlayer.pedal.updateParameters();

                mixValue.setText(String.valueOf((double)(progress / 2)));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });



    }

    @Override
    public int getLayoutResourceId() { return R.layout.activity_distortion; }
}
