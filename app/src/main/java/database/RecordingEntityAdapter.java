package database;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.denver.recorder_ui.R;

import java.util.List;


public class RecordingEntityAdapter extends RecyclerView.Adapter<RecordingEntityAdapter.ViewHolder> {

    public int focusedItem = -1;

    public interface OnItemClickListener{
        void onItemClick(RecordingEntity item);
    }

    private List<RecordingEntity> items;
    private int itemLayout;
    private final OnItemClickListener listener;

    public RecordingEntityAdapter(List<RecordingEntity> items, int itemLayout, OnItemClickListener listener){
        this.items = items;
        this.itemLayout = itemLayout;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecordingEntityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull RecordingEntityAdapter.ViewHolder holder, final int position) {
       holder.bind(items.get(position), position, listener);
       holder.itemView.setSelected(focusedItem == position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title_field = null;
        public ViewHolder(View itemView) {
            super(itemView);
            title_field = (TextView) itemView.findViewById(R.id.list_item_title);
        }

        public void bind( final RecordingEntity item, final int position, final OnItemClickListener listener){
            title_field.setText(item.getTitle());
            final int p = position;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    focusedItem = p;
                    listener.onItemClick(item);
                    focusedItem = position;

                }
            });
        }

    }

    public void filterList(List<RecordingEntity> filteredList){
        items = filteredList;
        notifyDataSetChanged();
    }

}
