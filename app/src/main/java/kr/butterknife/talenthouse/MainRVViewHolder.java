package kr.butterknife.talenthouse;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;

//https://youngest-programming.tistory.com/69
public class MainRVViewHolder {
    static class ContentNOViewHolder extends ContentRVHolder {
        protected TextView title;
        protected TextView writer;
        protected TextView date;
        protected TextView subject;

        public ContentNOViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.rvtext_tv_title);
            writer = itemView.findViewById(R.id.rvtext_tv_writer);
            date = itemView.findViewById(R.id.rvtext_tv_date);
            subject = itemView.findViewById(R.id.rvtext_tv_subject);
        }
    }

    static class ContentImageViewHolder extends ContentRVHolder {
        public ContentImageViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    static class ContentMP3ViewHolder extends ContentRVHolder {
        public ContentMP3ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    static class ContentVideoViewHolder extends ContentRVHolder {
        protected TextView title;
        protected TextView writer;
        protected TextView date;
        protected TextView subject;
        protected PlayerView pv;
//        protected PlayerControlView pcv;

        public ContentVideoViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.rvvideo_video_tv_title);
            writer = itemView.findViewById(R.id.rvvideo_video_tv_writer);
            date = itemView.findViewById(R.id.rvvideo_video_tv_date);
            subject = itemView.findViewById(R.id.rvvideo_video_tv_subject);
            pv = itemView.findViewById(R.id.rvvideo_video_player);
//            pcv = itemView.findViewById(R.id.rvvideo_video_controller);
        }
    }

    static class ContentLoadingViewHolder extends ContentRVHolder {
        public ContentLoadingViewHolder(@NonNull View itemView) { super(itemView);}
    }
}

class ContentRVHolder extends RecyclerView.ViewHolder {

    private OnItemClickListener itemClickListener;

    void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public ContentRVHolder(@NonNull View itemView) {
        super(itemView);

        itemView.setOnClickListener(v -> {
            int pos = getAdapterPosition();
            if(pos != RecyclerView.NO_POSITION) {
                itemClickListener.onItemClick(v, pos);
            }
        });
    }
}