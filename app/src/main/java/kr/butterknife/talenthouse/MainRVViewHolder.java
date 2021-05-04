package kr.butterknife.talenthouse;

import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        protected TextView title;
        protected TextView writer;
        protected TextView date;
        protected TextView subject;
        protected ViewStub viewStubImage;
        public ContentImageViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.rvimage_tv_title);
            writer = itemView.findViewById(R.id.rvimage_tv_writer);
            date = itemView.findViewById(R.id.rvimage_tv_date);
            subject = itemView.findViewById(R.id.rvimage_tv_subject);
            viewStubImage = itemView.findViewById(R.id.rvimage_vs);

        }
    }

    static class ContentMP3ViewHolder extends ContentRVHolder {
        public ContentMP3ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    static class ContentMP4ViewHolder extends ContentRVHolder {
        public ContentMP4ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

interface OnItemClickListener {
    void onItemClick(View v, int pos);
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