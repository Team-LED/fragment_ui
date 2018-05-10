package com.example.denver.recorder_ui;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class PlaybackActivity extends AppCompatActivity{


    ImageButton playPause,restart,exit;
    TextView leftTime,rightTime;
    SeekBar seekBar;
    MediaPlayer player;
    String audio_file_name;
    private int duration,current;
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback);


        playPause = (ImageButton) findViewById(R.id.play_pause);
        restart = (ImageButton)findViewById(R.id.restart_button);
        exit = (ImageButton) findViewById(R.id.exit_button);
        seekBar = (SeekBar) findViewById(R.id.seek_bar);
        Bundle extras = getIntent().getExtras();
        player = new MediaPlayer();
        handler = new Handler();

        if(extras == null){
            Toast.makeText(getApplicationContext(),
                    "Failed to get Audio File Name",
                    Toast.LENGTH_SHORT).show();
        }else{
            audio_file_name = extras.getString("FILE_NAME");
            //Toast.makeText(getApplicationContext(),
                 //   audio_file_name,
                //    Toast.LENGTH_SHORT).show();
        }

        try {
            File newFile = new File(audio_file_name);
            if(!newFile.exists())
                Toast.makeText(this, "FUCK", Toast.LENGTH_SHORT).show();
            player.setDataSource(audio_file_name);
            player.prepare();

        } catch (IOException e) {
            Toast.makeText(getApplicationContext(),
                    "Failed to get Audio File Name",
                    Toast.LENGTH_SHORT).show();
        }

        seekBar.setMax(player.getDuration());





        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                player.start();
                initializeSeekBar();

            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


    }

    void initializeSeekBar(){
        seekBar.setMax(player.getDuration());

        runnable  = new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(player.getCurrentPosition());
                handler.postDelayed(this,10);
            }
        };

        handler.postDelayed(runnable,10);

    }








}