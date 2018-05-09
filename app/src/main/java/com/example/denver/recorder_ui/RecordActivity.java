package com.example.denver.recorder_ui;

import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import database.RecordingDatabase;
import fragments.DetailFragment;

public class RecordActivity extends AppCompatActivity {

    private static final String LOG_TAG = "RecordActivity";
    ImageView record_button;
    TextView cancel_button;
    MediaRecorder recorder = null;

    //states
    boolean recording = false;
    String file_name = "Invalid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_record);
        record_button = findViewById(R.id.start_recording_button);
        record_button.setOnClickListener(record_listener);
        cancel_button = findViewById(R.id.cancel_button);
        cancel_button.setOnClickListener(cancel_listener);
        file_name = getIntent().getStringExtra("FILE_NAME");
        Toast.makeText(this, file_name, Toast.LENGTH_SHORT).show();
    }
    private void onRecord(boolean start) {
        if (start)
            startRecording();
        else
            stopRecording();
    }
    private View.OnClickListener record_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            recording = !recording;
            if(recording) {
                record_button.setImageResource(R.drawable.ic_stop_white_48dp);
                startRecording();
            }
            else{
                stopRecording();
                finish();
            }
        }
    };

    private View.OnClickListener cancel_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(recording){
                recorder.stop();
                recorder.release();
                recorder = null;
                File file = new File(file_name);
                boolean deleted = file.delete();
            }
            setResult(RESULT_CANCELED);
            finish();
        }
    };

    private View.OnClickListener stop_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            stopRecording();
            finish();
        }
    };

    private void startRecording() {
        recorder = new MediaRecorder();

        //It might be best to put this whole thing inside a try/catch
        //block? I'm not sure.

        //Sets up the recorder for audio as opposed to video,
        //and a few other things related to file format and encoding.
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

        //I think here is where I need to add the internal storage
        //directory as an argument
        //TODO have this be sent by putExtra
        recorder.setOutputFile(file_name);

        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        //Does some final magic to get the recorder ready.
        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        recorder.start();
    }

    private void stopRecording() {
        //refreshContents();
        recorder.stop();
        recorder.release();
        Toast.makeText(RecordActivity.this, "Saved to: " + file_name, Toast.LENGTH_SHORT).show();
        recorder = null;
        setResult(RESULT_OK);
    }
}
