package kr.butterknife.talenthouse;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import kr.butterknife.talenthouse.MainRVViewHolder.*;

import java.util.ArrayList;

public class MainRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<RVItem> arrayList;

    public MainRVAdapter(ArrayList<RVItem> list) {
        arrayList = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;

        if(viewType == ContentType.MP3.ordinal()) {
            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_mp3, parent, false);
            ContentMP3ViewHolder vh = new ContentMP3ViewHolder(view);
            return vh;
        }
        else if(viewType == ContentType.MP4.ordinal()) {
            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_mp4, parent, false);
            ContentMP4ViewHolder vh = new ContentMP4ViewHolder(view);
            return vh;
        }
        else if(viewType == ContentType.IMAGE.ordinal()) {
            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_image, parent, false);
            ContentImageViewHolder vh = new ContentImageViewHolder(view);
            return vh;
        }

        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_text, parent, false);
        ContentNOViewHolder vh = new ContentNOViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder _holder, int position) {
        if(position == ContentType.NO.ordinal()) {
            ContentNOViewHolder holder = (ContentNOViewHolder) _holder;
            holder.title.setText(arrayList.get(position).getTitle());
            holder.writer.setText(arrayList.get(position).getWriter());
            holder.date.setText(arrayList.get(position).getDate());
            holder.subject.setText(arrayList.get(position).getSubject());
        }
        else if(position == ContentType.MP3.ordinal()) {

        }
        else if(position == ContentType.MP4.ordinal()) {

        }
        else if(position == ContentType.IMAGE.ordinal()) {

        }
        else {

        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        RVItem item = arrayList.get(position);

        if(item.getMp3Url() != null) {
            return ContentType.MP3.ordinal();
        }
        else if(item.getMp4Url() != null) {
            return ContentType.MP4.ordinal();
        }
        else if(item.getImageUrl() != null) {
            return ContentType.IMAGE.ordinal();
        }

        return ContentType.NO.ordinal();
    }
}