package kr.butterknife.talenthouse;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import kr.butterknife.talenthouse.MainRVViewHolder.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<PostItem> arrayList;
    Context context;

    public MainRVAdapter(Context context, ArrayList<PostItem> list) {
        this.context = context;
        arrayList = list;
    }

    private OnItemClickListener itemClickListener = null;

    public void setOnItemClickListener(OnItemClickListener listener) {
        itemClickListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;

        if(viewType == ContentType.MP3.ordinal()) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_mp3, parent, false);
            ContentMP3ViewHolder vh = new ContentMP3ViewHolder(view);
            vh.setOnItemClickListener(itemClickListener);
            return vh;
        }
        else if(viewType == ContentType.VIDEO.ordinal()) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_mp4, parent, false);
            ContentMP4ViewHolder vh = new ContentMP4ViewHolder(view);
            vh.setOnItemClickListener(itemClickListener);
            return vh;
        }
        else if(viewType == ContentType.IMAGE.ordinal()) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_image, parent, false);
            ContentImageViewHolder vh = new ContentImageViewHolder(view);
            vh.setOnItemClickListener(itemClickListener);
            return vh;
        }

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_text, parent, false);
        ContentNOViewHolder vh = new ContentNOViewHolder(view);
        vh.setOnItemClickListener(itemClickListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder _holder, int position) {
        if(_holder instanceof ContentNOViewHolder) {
            System.out.println(position + " : " + "asdf");
            ContentNOViewHolder holder = (ContentNOViewHolder) _holder;
            holder.title.setText(arrayList.get(position).getTitle());
            holder.writer.setText(arrayList.get(position).getWriterNickname());
            holder.date.setText(arrayList.get(position).getUpdateTime());
//            holder.date.setText(Util.INSTANCE.getDate2String(arrayList.get(position).getUpdateTime()));
            holder.subject.setText(arrayList.get(position).getDescription());
        }
        else if(_holder instanceof ContentMP3ViewHolder) {

        }
        else if(_holder instanceof ContentMP4ViewHolder) {

        }
        else if(_holder instanceof ContentImageViewHolder) {
            ContentImageViewHolder holder = (ContentImageViewHolder) _holder;
            holder.title.setText(arrayList.get(position).getTitle());
            holder.writer.setText(arrayList.get(position).getWriterNickname());
            holder.date.setText(arrayList.get(position).getUpdateTime());
            holder.subject.setText(arrayList.get(position).getDescription());
            holder.viewStubImage.setLayoutResource(R.layout.viewstub_main_image);
            View inflated = holder.viewStubImage.inflate();
            ImageView imageView = inflated.findViewById(R.id.vs_main_iv);
            String temp = arrayList.get(position).getImageUrl().get(0);
            Glide.with(context)
                    .load(temp)
                    .into(imageView);
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
        PostItem item = arrayList.get(position);

        if(item.getMp3Url() != null) {
            return ContentType.MP3.ordinal();
        }
        else if(item.getVideoUrl() != null) {
            return ContentType.VIDEO.ordinal();
        }
        else if(item.getImageUrl() != null) {
            return ContentType.IMAGE.ordinal();
        }

        return ContentType.NO.ordinal();
    }
}