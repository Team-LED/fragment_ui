package fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.denver.recorder_ui.R;
import com.example.denver.recorder_ui.RecordActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import database.RecordingDatabase;
import database.RecordingEntity;

public class DetailFragment extends Fragment {
    public DetailFragment() {}

    static final int REQUEST_IMAGE_CAPTURE = 1;
    EditText title_field;
    EditText first_field;
    EditText last_field;
    EditText date_field;
    EditText desc_field;
    ImageView photo_holder;
    ImageButton photo_button;
    ImageButton record_button;
    TextView cancel_button;
    TextView save_button;


    //Database
    RecordingDatabase RD;
    List<RecordingEntity> list;
    RecordingEntity item;

    //For Image File
    File image_directory;
    private Uri image_uri;
    private String image_file_path;
    int view_height, view_width, image_width, image_height, scale_factor;


    //Strings to hold user inputs
    protected String input_title = "Untitled";
    protected String input_first_name = "Unnamed";
    protected String input_last_name = "Unnamed";
    protected String input_date = "Undated";
    protected String input_desc = "Blank";
    public static String audio_file_name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View detail_frag_view = inflater.inflate(R.layout.fragment_detail_entry, container, false);

        RD = RecordingDatabase.getRecordingDatabase(getContext());
        image_directory = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);


        title_field = detail_frag_view.findViewById(R.id.edit_title);
        first_field = detail_frag_view.findViewById(R.id.edit_first_name);
        last_field = detail_frag_view.findViewById(R.id.edit_last_name);
        date_field = detail_frag_view.findViewById(R.id.edit_date);
        desc_field = detail_frag_view.findViewById(R.id.edit_description);
        photo_holder = detail_frag_view.findViewById(R.id.photo_view);
        final Intent i = new Intent(getActivity(), RecordActivity.class);
        record_button = detail_frag_view.findViewById(R.id.record_img);
        cancel_button = detail_frag_view.findViewById(R.id.cancel_button);
        save_button = detail_frag_view.findViewById(R.id.save_button);
        photo_button = detail_frag_view.findViewById(R.id.photo_img);

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getData()){
                    create_entry();
                    clearData();
                }else{
                    Toast.makeText(getContext(),"Enter Unique Title",Toast.LENGTH_SHORT).show();
                }

            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearData();
            }
        });

        record_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getData()) {
                    i.putExtra("FILE_NAME", audio_file_name);
                    startActivityForResult(i, 2);
                }else{
                    Toast.makeText(getContext(),"Enter Unique Title",Toast.LENGTH_SHORT).show();
                }
            }
        });


        photo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        return detail_frag_view;
    }

    private void clearData(){
        title_field.getText().clear();
        first_field.getText().clear();
        last_field.getText().clear();
        date_field.getText().clear();
        desc_field.getText().clear();
        image_file_path = null;
        audio_file_name = null;
        photo_holder.setImageResource(R.drawable.ic_portrait_black_24dp);
        photo_holder.setRotation(0);
    }

    private void dispatchTakePictureIntent() {

        if (!title_field.getText().toString().isEmpty()) {
            input_title = title_field.getText().toString();

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                File image_file = null;
                try {
                    image_file = createImageFile();
                } catch (IOException e) {
                    Toast.makeText(getActivity(), "Failure in dispatchTakePictureIntent", Toast.LENGTH_SHORT).show();
                }

                if (image_file != null) {
                    image_uri = FileProvider.getUriForFile(getActivity(), "com.example.android.fileprovider", image_file);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        } else
            Toast.makeText(getActivity(), "Please Provide a Title for Recording First", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == Activity.RESULT_OK) {

                    try {
//                        photo_holder.setImageURI(imageUri);
//                        photo_holder.setVisibility(View.VISIBLE);

                        File test = new File(image_file_path);
                        if (test.exists()) {
                            //Create a smaller version of the file to use less memory
                            view_width = view_height = photo_holder.getWidth();
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inJustDecodeBounds = true;
                            BitmapFactory.decodeFile(image_file_path, options);
                            image_height = options.outHeight;
                            image_width = options.outWidth;

                            //determine sizing
                            scale_factor = Math.min(image_width / view_width, image_height / view_height);

                            //Decode into the sized bitmap
                            options.inJustDecodeBounds = false;
                            options.inSampleSize = scale_factor;

                            Bitmap bitmap = BitmapFactory.decodeFile(image_file_path, options);
                            photo_holder.setImageBitmap(bitmap);
                            photo_holder.setRotation(90);
                            photo_holder.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        } else
                            Toast.makeText(getActivity(), "NO FILE FOUND", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "SOMETHING FUCKED UP", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case 2:
                if (resultCode == Activity.RESULT_OK) {
                   Toast.makeText(getActivity(), "Recording Successful", Toast.LENGTH_SHORT).show();
                    break;
                } else{
                    Toast.makeText(getActivity(), "File Creation Failed", Toast.LENGTH_SHORT).show();
                    break;
                }
            default:
                Toast.makeText(getActivity(), "Unexpected Error", Toast.LENGTH_SHORT).show();

        }

    }

   private boolean getData() {

        List<RecordingEntity> list = RD.RecordingDao().getAllRecordings();

       input_title = title_field.getText().toString();
       input_first_name = first_field.getText().toString();
       input_last_name = last_field.getText().toString();
       input_date = date_field.getText().toString();
       input_desc = desc_field.getText().toString();
       String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
       audio_file_name = ListFragment.directory.toString() + '/' + timeStamp + ".3gp";

       for(RecordingEntity r: list){
           if (r.getTitle().toLowerCase().contains(input_title.toLowerCase()) || input_title.equals("Untitled")){
               return false;
           }
       }

        return true;
    }

    private void create_entry() {
        //  RD = RecordingDatabase.getRecordingDatabase(getContext());
        item = new RecordingEntity();
        item.setFirstName(input_first_name);
        item.setLastName(input_last_name);
        item.setLength("0:00");
        item.setDate(input_date);
        item.setDescription(input_desc);
        item.setImgFile(image_file_path);
        item.setTitle(input_title);
        item.setAudioFile(audio_file_name);
        RD.RecordingDao().insert(item);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                image_directory  /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        image_file_path = image.getAbsolutePath();
        return image;
    }

}
