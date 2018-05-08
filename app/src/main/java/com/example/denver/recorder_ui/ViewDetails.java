package com.example.denver.recorder_ui;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.List;

import database.RecordingDatabase;
import database.RecordingEntity;

public class ViewDetails extends AppCompatActivity {

    private static final String LOG_TAG = "ViewDetails";
    private EditText title_field, first_name_field, last_name_field, date_field, desc_field;
    ImageView image_field;
    private AppCompatButton play_button, cancel_button;
    private RecordingDatabase RD;
    private String audio_file_name;
    private MediaPlayer player = null;
    private String image_file_name;
    int view_height, view_width, image_width, image_height, scale_factor;
    boolean clicked;
    protected static List<RecordingEntity> list;
    private boolean isPlaying = true;
    TextView edit_button;
    TextView delete_button;
    TextView save_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_edit);

        int id = getIntent().getIntExtra("INPUT_RECORDING_ID", -1);
        clicked = false;
        if(id == -1){
            Toast.makeText(this, "FAILED SEARCH", Toast.LENGTH_SHORT).show();
            finish();
        }

        RD = RecordingDatabase.getRecordingDatabase(this);
        list = RD.RecordingDao().searchId(id);
        final RecordingEntity item = list.get(0);
        //Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();

        title_field = findViewById(R.id.edit_title);
        first_name_field = findViewById(R.id.edit_first_name);
        last_name_field = findViewById(R.id.edit_last_name);
        date_field = findViewById(R.id.edit_date);
        desc_field = findViewById(R.id.edit_description);
        image_field = findViewById(R.id.photo_view);
        //play_button = findViewById(R.id.cancel_button);

        setFields(item);
//        image_field.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                clicked = !clicked;
//                if(clicked){
//                    image_field.getLayoutParams().height = (int)getResources().getDimension(R.dimen.full_image_size);
//                    image_field.getLayoutParams().width = (int)getResources().getDimension(R.dimen.full_image_size);
//                    setImage((int)getResources().getDimension(R.dimen.full_image_size));
//                }
//                else {
//                    image_field.getLayoutParams().height = (int) getResources().getDimension(R.dimen.larger_image_size);
//                    image_field.getLayoutParams().width = (int) getResources().getDimension(R.dimen.larger_image_size);
//                    setImage((int) getResources().getDimension(R.dimen.larger_image_size));
//                }
//
//            }
//        });

        final AlertDialog deleteDialog = deleteDialog(item);
        final AlertDialog saveDialog = saveDialog(item);

        edit_button = (TextView) findViewById(R.id.edit_button);
        delete_button = (TextView) findViewById(R.id.delete_button);
        save_button= (TextView) findViewById(R.id.save_button);

        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Toast.makeText(getApplicationContext(),"Edit Button",Toast.LENGTH_SHORT).show();

            }
        });

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.show();
            }
        });

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setTitle(title_field.getText().toString());
                item.setFirstName(first_name_field.getText().toString());
                item.setLastName(last_name_field.getText().toString());
                item.setDate(date_field.getText().toString());
                item.setDescription(desc_field.getText().toString());
                saveDialog.show();

            }
        });


    }
    AlertDialog deleteDialog(final RecordingEntity item){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Delete");
        builder.setMessage("Delete "+item.getTitle()+"?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                RD.RecordingDao().delete(item);
                finish();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();

        return alert;
    }


    AlertDialog saveDialog(final RecordingEntity item){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Save");
        builder.setMessage("Update this recording?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                RD.RecordingDao().update(item);
                finish();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();

        return alert;
    }
    void setFields(RecordingEntity item){
        //Uncomment to make fields unselectable by default
        title_field.setText(item.getTitle());
        //title_field.setKeyListener(null);
        first_name_field.setText(item.getFirstName());
        //first_name_field.setKeyListener(null);
        last_name_field.setText(item.getLastName());
       // last_name_field.setKeyListener(null);
        date_field.setText(item.getDate());
       // date_field.setKeyListener(null);
        desc_field.setText(item.getDescription());
        //desc_field.setKeyListener(null);
        audio_file_name = item.getAudioFile();
        image_file_name = item.getImgFile();
        if(image_file_name != null)
            setImage((int) getResources().getDimension(R.dimen.extra_large_image_size));
    }

    View.OnClickListener play_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onPlay(isPlaying);
            if (isPlaying) {
                play_button.setText("Stop Playing");
                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        play_button.setText("Start Playing");
                        stopPlaying();
                        isPlaying = !isPlaying;
                    }
                });
            } else {
                play_button.setText("Start Playing");
            }
            isPlaying = !isPlaying;
        }
    };

    private void onPlay(boolean start) {
        if (start)
            startPlaying();
        else
            stopPlaying();
    }

    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(audio_file_name);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Couldn't load media file: " + audio_file_name);
        }
    }

    //TODO: add a pause button for playback

    //Release resources when finished to get memory back
    private void stopPlaying() {
        player.release();
        player = null;
    }
    void setImage(int size){

            File test = new File(image_file_name);
            if (test.exists()) {
                //Create a smaller version of the file to use less memory
                view_width = view_height = size;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(image_file_name, options);
                image_height = options.outHeight;
                image_width = options.outWidth;

                //determine sizing
                scale_factor = Math.min(image_width / view_width, image_height / view_height);

                //Decode into the sized bitmap
                options.inJustDecodeBounds = false;
                options.inSampleSize = scale_factor;

                Bitmap bitmap = BitmapFactory.decodeFile(image_file_name, options);
                image_field.setImageBitmap(bitmap);
                image_field.setRotation(90);
                image_field.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
    }

}
