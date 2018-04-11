package fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.denver.recorder_ui.R;
import java.util.List;

import database.RecordingDatabase;
import database.RecordingEntity;


public class DatabaseTestFragment extends Fragment {
    public DatabaseTestFragment() {}

    //Declare an instance of the database
    RecordingDatabase RD;

    //Declare Buttons, TextViews, etc.
    Button SB;

    //Declare ArrayList to hold database objects
    List<RecordingEntity> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_database_test, container, false);

        SB = view.findViewById(R.id.save_button);

        //Set the database
        RD = RecordingDatabase.getRecordingDatabase(getContext());


        SB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Create an instance of RecordingEntity to store your variables
                final RecordingEntity newR = new RecordingEntity();
                newR.setFirstName("Denver");
                newR.setLastName("Gregory");
                newR.setLength("01:12");
                newR.setDescription("This is a new test recording");
                newR.setImgFile("img234.jpg");
                newR.setTitle("New Title");
                newR.setAudioFile("aud123.MP3");


                //This is how you add to the database, variables can be null
                RD.RecordingDao().insert(newR);
            }
        });

        //This is how you would store all of the recordings into an ArrayList
        list = RD.RecordingDao().getAllRecordings();

        //To retrieve single entity from database. I'll write a query to receive a single item later
        RecordingEntity e = list.get(0);

        //To clear the items from database. I'll write a query to delete a single item later
        RD.RecordingDao().deleteAll();



        return view;
    }

}
