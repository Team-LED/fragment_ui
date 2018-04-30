package fragments;


import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.denver.recorder_ui.MainActivity;
import com.example.denver.recorder_ui.R;
import com.example.denver.recorder_ui.ViewDetails;

import database.RecordingEntityAdapter;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import database.RecordingDatabase;
import database.RecordingEntity;

public class ListFragment extends Fragment {

    private static final String LOG_TAG = "ListFragment";

    //For populating the list of recordings
    protected static RecordingEntityAdapter adapter = null;

    //For accessing the database
    protected static List<RecordingEntity> list;
    protected RecordingDatabase RD;

    //Element declarations
    RecyclerView recyclerView;

    //For accessing and storing audio files
    public static File directory = null;
    public static File Recordings_Contents[] = null;

    String file_name = null;

    //States to keep track of which buttons should be enabled, to prevent
    //stuff like playing and recording at the same time.
    boolean startPlaying = true;


    //The bits the do the actual playing and recording
    //private MediaRecorder recorder = null;
    private MediaPlayer player = null;



    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get the name of our directory and load all the files currently stored in it
        directory = getActivity().getDir("Recordings", Context.MODE_PRIVATE);
        Recordings_Contents = directory.listFiles();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View list_frag_view = inflater.inflate(R.layout.fragment_list, container, false);
        final int icon_size = (int)getResources().getDimension(R.dimen.small_image_size);

        RD = RecordingDatabase.getRecordingDatabase(getContext());

        list = RD.RecordingDao().getAllRecordings();

        adapter = new RecordingEntityAdapter(list, R.id.list_item, new RecordingEntityAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(RecordingEntity item) {
                file_name = item.getAudioFile();
                openItemDetails(item);
            }
        }, icon_size);

        //Hook up our recyclerView to its layout
        recyclerView = list_frag_view.findViewById(R.id.recording_container);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);


        // Set up the search bar
        EditText search = list_frag_view.findViewById(R.id.search_box);

        // Allow the text box to alter the recycler view if anything is changed
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            // We only care if after the text has been altered
            @Override
            public void afterTextChanged(Editable s) {
                // Create new list to alter the recycler view
                filter(s.toString());
            }
        });

        // Refreshes the Recycler View by pulling down on the list.
        final SwipeRefreshLayout refresh = (SwipeRefreshLayout) list_frag_view.findViewById(R.id.swipe_refresh);

                refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        list = RD.RecordingDao().getAllRecordings();
                        adapter.update(list);
                        refresh.setRefreshing(false);
                    }
                });

        return list_frag_view;
    }

    private void filter(String text){
        List<RecordingEntity> filteredList = new ArrayList<>();

        // Create a list of only entities that match the search
        for(RecordingEntity entity : list){
            if(entity.getTitle().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(entity);
            }
        }
        adapter.update(filteredList);
    }

    @Override
    public void onStart() {
        super.onStart();
   }


    void openItemDetails(RecordingEntity item){
        Intent i = new Intent(getActivity(), ViewDetails.class);
        i.putExtra("INPUT_RECORDING_ID", item.getRecordingId());

        //TODO change this to a start activity for result to see if file was edited or not
        startActivity(i);
    }



}
