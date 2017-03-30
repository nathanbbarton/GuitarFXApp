package com.nathanbarton.guitarfxapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * Created by NathanBarton on 2016-11-29.
 */

public abstract class PedalActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected AudioPlayer         audioPlayer      = null; //audio player for sound input output
    protected MusicIntentReceiver myReceiver;              //receiver of headset plug intent
    protected final Handler       h                = new Handler(); //handler to test for latency

    private static final String TAG              = "PedalActivity";
    protected static final int BUFFER_TYPE_SHORT = 1;
    protected static final int BUFFER_TYPE_FLOAT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());

        //Every activity has a toolbar menu
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setBackgroundColor(Color.DKGRAY);
        setSupportActionBar(toolbar);

        //every activity times for latency
        final TextView latency = (TextView) findViewById(R.id.latency_value);

        //all activities use the drawer layout
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        //set the drawer toggle listener
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //set the toolbar listener
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(Gravity.LEFT)){
                    drawer.closeDrawer(Gravity.LEFT);
                } else {
                    drawer.openDrawer(Gravity.LEFT);
                }
            }
        });

        //set toggle button listener to switch effect on and off
        ToggleButton onOff = (ToggleButton) findViewById(R.id.on_off_button);

        onOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioPlayer.pedal.isApplyingFx = !audioPlayer.pedal.isApplyingFx;
            }
        });

        //set up navigation drawer view
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        myReceiver  = new MusicIntentReceiver(); //initialize the headphone intent receiver


        final short delay = 1000; //1 second

        h.postDelayed(new Runnable() {
            public void run() {
                latency.setText(audioPlayer.latency + "ms"); //post the latency every second

                h.postDelayed(this, delay);
            }
        }, delay);

        super.onStart();

    }

    private class MusicIntentReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
                int state = intent.getIntExtra("state", -1);

                switch (state) {
                    case 0:
                        Log.d(TAG, "Headset is unplugged"); //if headset is unplugged stop the audio thread and subsequently the audio playback
                        if(audioPlayer.isPlaying) {
                            audioPlayer.stopPlayback();
                            audioPlayer.isPlaying = false;
                        }
                        break;
                    case 1:
                        Log.d(TAG, "Headset is plugged"); //if headset plugged in start the thread and play back
                        if (!audioPlayer.isPlaying) {
                            audioPlayer.startPlayback();
                            audioPlayer.isPlaying = true;
                        }
                        break;
                    default:
                        Log.d(TAG, "I have no idea what the headset state is");
                }
            }
        }
    }

    @Override
    public void onResume() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(myReceiver, filter); //reregister the receiver to test for headphone connection
        super.onResume();
    }

    @Override
    public void onPause() {
        unregisterReceiver(myReceiver);//stop listening for the headset intent
        super.onPause();
        audioPlayer.stopPlayback();//stop thread when activity is paused
        finish();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Intent intent = new Intent();
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //start new pedal activities based on navigation selection
        if (id == R.id.nav_overdrive) {
            if (this.getLayoutResourceId() != R.layout.activity_overdrive) {
                intent = new Intent(this, OverdriveActivity.class);
                startActivity(intent);
            }

        } else if (id == R.id.nav_distortion) {
            if (this.getLayoutResourceId() != R.layout.activity_distortion){
                intent = new Intent(this,DistortionActivity.class);
                startActivity(intent);
            }
        } else if (id == R.id.nav_fuzz) {
            if (this.getLayoutResourceId() != R.layout.activity_fuzz){
                intent = new Intent(this,FuzzActivity.class);
                startActivity(intent);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected abstract int getLayoutResourceId();

}