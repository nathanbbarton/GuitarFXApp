package com.nathanbarton.guitarfxapp;

import android.content.Context;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;


/**
 * Created by NathanBarton on 2016-12-07.
 */

public class FuzzActivity extends PedalActivity {

    private static final String TAG = "FuzzActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE); //Initialize audio manager

        audioPlayer = new AudioPlayer(am, BUFFER_TYPE_FLOAT); //give audio player audio manager

        //SET UI PARAMETERS

        //pedal name text
        TextView pedalName = (TextView) findViewById(R.id.pedal_name);
        Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/Note_this.ttf");
        pedalName.setTypeface(tf);

        //Seek bars for parameter control
        SeekBar gainControl = (SeekBar) findViewById(R.id.gain_control);
        SeekBar mixControl  = (SeekBar) findViewById(R.id.mix_control);

        gainControl.setProgress(4);
        mixControl.setProgress(2);

        //textviews for parameter values
        final TextView gainValue       = (TextView) findViewById(R.id.gain_value);
        final TextView mixValue        = (TextView) findViewById(R.id.mix_value);

        gainValue.setText("5");
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

        mixControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioPlayer.pedal.fxParameters[1] = (double)(progress / 2);
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
    public int getLayoutResourceId() { return R.layout.activity_fuzz; }
}

