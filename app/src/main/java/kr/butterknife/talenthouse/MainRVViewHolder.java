package kr.butterknife.talenthouse;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MainRVViewHolder {
    static class ContentNOViewHolder extends RecyclerView.ViewHolder {
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

    static class ContentImageViewHolder extends RecyclerView.ViewHolder {
        public ContentImageViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    static class ContentMP3ViewHolder extends RecyclerView.ViewHolder {
        public ContentMP3ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    static class ContentMP4ViewHolder extends RecyclerView.ViewHolder {
        public ContentMP4ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
