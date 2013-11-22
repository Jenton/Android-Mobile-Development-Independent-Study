package com.jenton.bathroomwhitenoise;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;
import android.widget.ToggleButton;
import java.util.Random;


import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainActivity extends ActionBarActivity {

    //Media Players
    private MediaPlayer fanPlayer;
    private MediaPlayer flushPlayer;
    private MediaPlayer faucetPlayer;

    //Timer
    Timer flushTimer = new Timer();
    Timer faucetTimer = new Timer();
    Timer fanTimer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public class PlaceholderFragment extends Fragment {

        private final Logger logger = Logger.getLogger(PlaceholderFragment.class.getName());


        Switch fanSwitch;
        Switch flushSwitch;
        Switch faucetSwitch;
        Switch doorSwitch;
        Switch handDryerSwitch;
        SeekBar volumeSeekBar;
        AudioManager audioManager;




        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            //audio controls
            setVolumeControlStream(AudioManager.STREAM_MUSIC);

            //initializing buttons
            fanSwitch = (Switch) rootView.findViewById(R.id.fanSwitch);
            flushSwitch = (Switch) rootView.findViewById(R.id.flushSwitch);
            faucetSwitch = (Switch) rootView.findViewById(R.id.faucetSwitch);
            volumeSeekBar = (SeekBar) rootView.findViewById(R.id.volumeSeekBar);
            audioManager = (AudioManager) getSystemService(MainActivity.AUDIO_SERVICE);
            volumeSeekBar.setMax(audioManager
                    .getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            volumeSeekBar.setProgress(audioManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC));

            volumeSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
            {
                @Override
                public void onStopTrackingTouch(SeekBar arg0)
                {
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0)
                {
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2)
                {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            progress, 0);
                }
            });


            fanSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {
                        // The toggle is enabled
                        logger.log(Level.INFO, "Switch On");
                        fanPlayer = MediaPlayer.create(MainActivity.this, R.raw.fan_apartment_cardoid_wav);

                        fanPlayer.setLooping(true);
                        fanPlayer.start();


                    } else {
                        // The toggle is disabled
                        logger.log(Level.INFO, "Switch Off");
                        fanPlayer.stop();
                    }
                }
            });

            flushSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        // The toggle is enabled

                        Random random = new Random();

                        logger.log(Level.INFO, "Flush On");
                        flushPlayer = MediaPlayer.create(MainActivity.this, R.raw.flush_apartment_fanon_cardoid);
                        //currentSong = R.raw.song1;

                        //after waiting a random amount of seconds between 1-10 seconds, flush fires
                        //then will wait a random amount of seconds between 46-105 seconds before firing again
                        flushTimer.schedule(
                                new TimerTask() {
                                    @Override
                                    public void run() {
                                        // your code here
                                        flushPlayer.start();
                                    }
                                },
                                (random.nextInt(10)+1)*1000,
                                (random.nextInt(60)+46)*1000
                        );

                    } else {
                        // The toggle is disabled
                        logger.log(Level.INFO, "Flush Off");
                        flushPlayer.stop();
                    }
                }
            });
            faucetSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        // The toggle is enabled

                        Random random = new Random();

                        logger.log(Level.INFO, "Faucet On");
                        faucetPlayer = MediaPlayer.create(MainActivity.this, R.raw.faucet_apartment_fanon_omni);

                        //after waiting a random amount of seconds between 1-10 seconds, faucet fires
                        //then will wait a random amount of seconds between 11-70 seconds before firing again
                        faucetTimer.schedule(
                                new TimerTask() {
                                    @Override
                                    public void run() {
                                        // your code here
                                        faucetPlayer.start();
                                    }
                                },
                                (random.nextInt(10)+1)*1000,
                                (random.nextInt(60)+11)*1000
                                //(random.nextInt(10)+1)*1000
                        );


                    } else {
                        // The toggle is disabled
                        logger.log(Level.INFO, "Faucet Off");
                        faucetPlayer.stop();
                    }
                }
            });


            return rootView;

        }

    }

}
