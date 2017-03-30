package com.nathanbarton.guitarfxapp;

import android.content.Context;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

public class OverdriveActivity extends PedalActivity {

    private static final String TAG = "OverdriveActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE); //initialize audio manager

        audioPlayer = new AudioPlayer(am, BUFFER_TYPE_SHORT); // give audio player audio manager

        //SET UI PARAMETERS

        //pedal name and font
        TextView pedalName = (TextView) findViewById(R.id.pedal_name);
        Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/Note_this.ttf");
        pedalName.setTypeface(tf);

        //Seek bars for parameter control
        SeekBar overdriveControl    = (SeekBar) findViewById(R.id.overdrive_control);
        SeekBar clipThreshTop = (SeekBar) findViewById(R.id.clipping_thresh_top_control);
        SeekBar clipThreshBottom    = (SeekBar) findViewById(R.id.clipping_thresh_bottom_control);

        clipThreshTop.setProgress(5);
        clipThreshBottom.setProgress(5);

        //Textview for parameter values
        final TextView overdriveValue    = (TextView) findViewById(R.id.overdrive_value);
        final TextView threshTopValue    = (TextView) findViewById(R.id.clipping_control_top_value);
        final TextView threshBottomValue = (TextView) findViewById(R.id.clipping_control_bottom_value);


        overdriveValue.setText("1");
        threshTopValue.setText("1.0");
        threshBottomValue.setText("3.0");

        //SET SEEK BAR LISTENERS
        //Any new seek bad must implement a listener that updates its respective pedal effect parameter
        //EX: gainControl updates the gain parameter in audioPlayer.pedal

        overdriveControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioPlayer.pedal.fxParameters[0] = progress + 1;
                audioPlayer.pedal.updateParameters();

                overdriveValue.setText(String.valueOf(progress + 1));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        clipThreshTop.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioPlayer.pedal.fxParameters[1] = (double) (progress + 5)/ 10;
                audioPlayer.pedal.updateParameters();

                threshTopValue.setText(String.valueOf((double) (progress + 5)/ 10));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}

        });

        clipThreshBottom.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioPlayer.pedal.fxParameters[2] = (double)(progress + 25) / 10;
                audioPlayer.pedal.updateParameters();

                threshBottomValue.setText(Double.toString((double)(progress + 25)/10));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}

        });

    }

    @Override
    public int getLayoutResourceId() { return R.layout.activity_overdrive; }

}
