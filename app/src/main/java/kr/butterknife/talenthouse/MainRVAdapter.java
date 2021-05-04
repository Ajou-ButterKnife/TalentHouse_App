package kr.butterknife.talenthouse;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import kr.butterknife.talenthouse.MainRVViewHolder.*;

import java.util.ArrayList;

public class MainRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<PostItem> arrayList;
    private Context context;

    public MainRVAdapter(Context context, ArrayList<PostItem> list) {
        arrayList = list;
        this.context = context;
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
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_video, parent, false);
            ContentVideoViewHolder vh = new ContentVideoViewHolder(view);
            vh.setOnItemClickListener(itemClickListener);
            return vh;
        }
        else if(viewType == ContentType.IMAGE.ordinal()) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_image, parent, false);
            ContentImageViewHolder vh = new ContentImageViewHolder(view);
            vh.setOnItemClickListener(itemClickListener);
            return vh;
        }
        else if(viewType == ContentType.LOADING.ordinal()) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_loading, parent, false);
            ContentLoadingViewHolder vh = new ContentLoadingViewHolder(view);
//            vh.setOnItemClickListener(itemClickListener);
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
            ContentNOViewHolder holder = (ContentNOViewHolder) _holder;
            holder.title.setText(arrayList.get(position).getTitle());
            holder.writer.setText(arrayList.get(position).getWriterNickname());
            holder.date.setText(Util.INSTANCE.unixTime2String(Long.parseLong(arrayList.get(position).getUpdateTime())));
//            holder.date.setText(Util.INSTANCE.getDate2String(arrayList.get(position).getUpdateTime()));
            holder.subject.setText(arrayList.get(position).getDescription());
        }
        else if(_holder instanceof ContentMP3ViewHolder) {

        }
        else if(_holder instanceof ContentVideoViewHolder) {
            ContentVideoViewHolder holder = (ContentVideoViewHolder) _holder;
            holder.title.setText(arrayList.get(position).getTitle());
            holder.writer.setText(arrayList.get(position).getWriterNickname());
            holder.date.setText(Util.INSTANCE.unixTime2String(Long.parseLong(arrayList.get(position).getUpdateTime())));
            holder.subject.setText(arrayList.get(position).getDescription());

            SimpleExoPlayer player = new SimpleExoPlayer.Builder(context).build();
            holder.pv.setPlayer(player);

            DataSource.Factory factory = new DefaultDataSourceFactory(context, "Ex98VideoAndExoPlayer");
            Uri videoUri = Uri.parse(arrayList.get(position).getVideoUrl());
            ProgressiveMediaSource mediaSource= new ProgressiveMediaSource.Factory(factory).createMediaSource(videoUri);

            player.addMediaSource(mediaSource);
            player.prepare();
            player.setPlayWhenReady(false);
        }
        else if(_holder instanceof ContentImageViewHolder) {

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

        if(item == null){
            return ContentType.LOADING.ordinal();
        }
        else if(item.getMp3Url() != null) {
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