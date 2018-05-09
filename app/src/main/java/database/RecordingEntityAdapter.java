package database;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.denver.recorder_ui.R;

import java.io.File;
import java.util.List;


public class RecordingEntityAdapter extends RecyclerView.Adapter<RecordingEntityAdapter.ViewHolder> {

    public int focusedItem = -1;
    public int view_width, view_height, image_height, image_width, scale_factor;

    public interface OnItemClickListener{
        void onItemClick(RecordingEntity item);
    }

    private List<RecordingEntity> items;
    private int itemLayout;
    private final OnItemClickListener listener;

    public RecordingEntityAdapter(List<RecordingEntity> items, int itemLayout, OnItemClickListener listener, int q){
        this.items = items;
        this.itemLayout = itemLayout;
        this.listener = listener;
        view_width = view_height = q;
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
        public ImageView image_field = null;
        public String image_file_name;
        public ViewHolder(View itemView) {
            super(itemView);
            title_field = (TextView) itemView.findViewById(R.id.list_item_title);
            image_field = (ImageView) itemView.findViewById(R.id.list_item_icon);
        }

        public void bind( final RecordingEntity item, final int position, final OnItemClickListener listener){


            title_field.setText(item.getTitle());
            image_file_name = item.getImgFile();
            if(image_file_name != null) {
                File test = new File(image_file_name);
                if (test.exists()) {
//                //Create a smaller version of the file to use less memory
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(image_file_name, options);
                    image_height = options.outHeight;
                    image_width = options.outWidth;
//
//                //determine sizing
                    scale_factor = Math.min(image_width / view_width, image_height / view_height);
//
//                //Decode into the sized bitmap
                    options.inJustDecodeBounds = false;
                    options.inSampleSize = scale_factor;

                    Bitmap bitmap = BitmapFactory.decodeFile(image_file_name, options);
                    image_field.setImageBitmap(bitmap);
                    image_field.setRotation(90);
                    image_field.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }
            }

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

    public void update(List<RecordingEntity> filteredList){
        items = filteredList;
        notifyDataSetChanged();
    }




}
