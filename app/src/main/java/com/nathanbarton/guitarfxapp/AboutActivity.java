package com.nathanbarton.guitarfxapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

/**
 * Created by NathanBarton on 2016-12-07.
 */

public class AboutActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "AboutActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);//Top toolabar of the UI
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setBackgroundColor(Color.DKGRAY);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);//Navigation Drawer set up
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close); //set listener for drawer use
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//Top toolbar listener
                if (drawer.isDrawerOpen(Gravity.LEFT)){
                    drawer.closeDrawer(Gravity.LEFT);
                } else {
                    drawer.openDrawer(Gravity.LEFT);
                }
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view); //Finalize navigation drawer view

        navigationView.setNavigationItemSelectedListener(this);

        TextView aboutText = (TextView) findViewById(R.id.about_text); //Text view to contain about information
        aboutText.setText("\n\n About Page" +
                "\n\nGuitarFxApp made by: Nathan Barton " +
                "\n\nStudent Number: 100792105" +
                "\nversion # 1.0 " +
                "\n\nDescription" +
                "\n\nGuitarFxApp is a an app that can be used to mimic a standard guitar effects pedal." +
                "It provides the framework to test and work with different sound manipulation algorithms." +
                "As well it shows the limitations of the pure Java android framework for real-time audio.");

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        finish();
    }

    //How to react to back pressed closes the navigation drawer
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

    public int getLayoutResourceId(){ return R.layout.activity_about; }


}
