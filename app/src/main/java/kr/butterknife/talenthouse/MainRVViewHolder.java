package kr.butterknife.talenthouse;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;

import java.util.List;

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

    static class ContentImageViewHolder_1 extends ContentRVHolder {
        protected TextView title;
        protected TextView writer;
        protected TextView date;
        protected TextView subject;
        protected ViewStub viewStubImage;
        protected PostItem postItem;
        View inflated;

        public ContentImageViewHolder_1(@NonNull View itemView, ViewStub viewStub) {
            super(itemView);
            title = itemView.findViewById(R.id.rvimage_tv_title);
            writer = itemView.findViewById(R.id.rvimage_tv_writer);
            date = itemView.findViewById(R.id.rvimage_tv_date);
            subject = itemView.findViewById(R.id.rvimage_tv_subject);
            this.viewStubImage = viewStub;
            viewStubImage.setLayoutResource(R.layout.viewstub_main_image_1);
            inflated = viewStubImage.inflate();
        }

        void onBind(PostItem postItem, Context context) {
            this.postItem = postItem;
            title.setText(postItem.getTitle());
            writer.setText(postItem.getWriterNickname());
            date.setText(Util.INSTANCE.unixTime2String(Long.parseLong(postItem.getUpdateTime())));
            subject.setText(postItem.getDescription());
            List<String> urlList = postItem.getImageUrl();

            Glide.with(context)
                    .load(urlList.get(0))
                    .into((ImageView) inflated.findViewById(R.id.vs_main_iv_1));
        }
    }

    static class ContentImageViewHolder_2 extends ContentRVHolder {
        protected TextView title;
        protected TextView writer;
        protected TextView date;
        protected TextView subject;
        protected ViewStub viewStubImage;
        protected PostItem postItem;
        View inflated;

        public ContentImageViewHolder_2(@NonNull View itemView, ViewStub viewStub) {
            super(itemView);
            title = itemView.findViewById(R.id.rvimage_tv_title);
            writer = itemView.findViewById(R.id.rvimage_tv_writer);
            date = itemView.findViewById(R.id.rvimage_tv_date);
            subject = itemView.findViewById(R.id.rvimage_tv_subject);
            this.viewStubImage = viewStub;
            viewStubImage.setLayoutResource(R.layout.viewstub_main_image_2);
            inflated = viewStubImage.inflate();
        }

        void onBind(PostItem postItem, Context context) {
            this.postItem = postItem;
            title.setText(postItem.getTitle());
            writer.setText(postItem.getWriterNickname());
            date.setText(Util.INSTANCE.unixTime2String(Long.parseLong(postItem.getUpdateTime())));
            subject.setText(postItem.getDescription());
            List<String> urlList = postItem.getImageUrl();

            Glide.with(context)
                    .load(urlList.get(0))
                    .into((ImageView) inflated.findViewById(R.id.vs_main_iv2_1));
            Glide.with(context)
                    .load(urlList.get(1))
                    .into((ImageView) inflated.findViewById(R.id.vs_main_iv2_2));
        }
    }

    static class ContentImageViewHolder_3 extends ContentRVHolder {
        protected TextView title;
        protected TextView writer;
        protected TextView date;
        protected TextView subject;
        protected ViewStub viewStubImage;
        protected PostItem postItem;
        View inflated;

        public ContentImageViewHolder_3(@NonNull View itemView, ViewStub viewStub) {
            super(itemView);
            title = itemView.findViewById(R.id.rvimage_tv_title);
            writer = itemView.findViewById(R.id.rvimage_tv_writer);
            date = itemView.findViewById(R.id.rvimage_tv_date);
            subject = itemView.findViewById(R.id.rvimage_tv_subject);
            this.viewStubImage = viewStub;
            viewStubImage.setLayoutResource(R.layout.viewstub_main_image_3);
            inflated = viewStubImage.inflate();
        }

        void onBind(PostItem postItem, Context context) {
            this.postItem = postItem;
            title.setText(postItem.getTitle());
            writer.setText(postItem.getWriterNickname());
            date.setText(Util.INSTANCE.unixTime2String(Long.parseLong(postItem.getUpdateTime())));
            subject.setText(postItem.getDescription());
            List<String> urlList = postItem.getImageUrl();

            Glide.with(context)
                    .load(urlList.get(0))
                    .into((ImageView) inflated.findViewById(R.id.vs_main_iv3_1));
            Glide.with(context)
                    .load(urlList.get(1))
                    .into((ImageView) inflated.findViewById(R.id.vs_main_iv3_2));
            Glide.with(context)
                    .load(urlList.get(2))
                    .into((ImageView) inflated.findViewById(R.id.vs_main_iv3_3));
        }
    }
    static class ContentImageViewHolder_4 extends ContentRVHolder {
        protected TextView title;
        protected TextView writer;
        protected TextView date;
        protected TextView subject;
        protected ViewStub viewStubImage;
        protected PostItem postItem;
        View inflated;

        public ContentImageViewHolder_4(@NonNull View itemView, ViewStub viewStub) {
            super(itemView);
            title = itemView.findViewById(R.id.rvimage_tv_title);
            writer = itemView.findViewById(R.id.rvimage_tv_writer);
            date = itemView.findViewById(R.id.rvimage_tv_date);
            subject = itemView.findViewById(R.id.rvimage_tv_subject);
            this.viewStubImage = viewStub;
            viewStubImage.setLayoutResource(R.layout.viewstub_main_image_4);
            inflated = viewStubImage.inflate();
        }

        void onBind(PostItem postItem, Context context) {
            this.postItem = postItem;
            title.setText(postItem.getTitle());
            writer.setText(postItem.getWriterNickname());
            date.setText(Util.INSTANCE.unixTime2String(Long.parseLong(postItem.getUpdateTime())));
            subject.setText(postItem.getDescription());
            List<String> urlList = postItem.getImageUrl();

            Glide.with(context)
                    .load(urlList.get(0))
                    .into((ImageView) inflated.findViewById(R.id.vs_main_iv4_1));
            Glide.with(context)
                    .load(urlList.get(1))
                    .into((ImageView) inflated.findViewById(R.id.vs_main_iv4_2));
            Glide.with(context)
                    .load(urlList.get(2))
                    .into((ImageView) inflated.findViewById(R.id.vs_main_iv4_3));
            Glide.with(context)
                    .load(urlList.get(3))
                    .into((ImageView) inflated.findViewById(R.id.vs_main_iv4_4));
        }
    }

    static class ContentImageViewHolder_5 extends ContentRVHolder {
        protected TextView title;
        protected TextView writer;
        protected TextView date;
        protected TextView subject;
        protected ViewStub viewStubImage;
        protected PostItem postItem;
        View inflated;

        public ContentImageViewHolder_5(@NonNull View itemView, ViewStub viewStub) {
            super(itemView);
            title = itemView.findViewById(R.id.rvimage_tv_title);
            writer = itemView.findViewById(R.id.rvimage_tv_writer);
            date = itemView.findViewById(R.id.rvimage_tv_date);
            subject = itemView.findViewById(R.id.rvimage_tv_subject);
            this.viewStubImage = viewStub;
            viewStubImage.setLayoutResource(R.layout.viewstub_main_image_5);
            inflated = viewStubImage.inflate();
        }

        void onBind(PostItem postItem, Context context) {
            this.postItem = postItem;
            title.setText(postItem.getTitle());
            writer.setText(postItem.getWriterNickname());
            date.setText(Util.INSTANCE.unixTime2String(Long.parseLong(postItem.getUpdateTime())));
            subject.setText(postItem.getDescription());
            List<String> urlList = postItem.getImageUrl();

            Glide.with(context)
                    .load(urlList.get(0))
                    .into((ImageView) inflated.findViewById(R.id.vs_main_iv5_1));
            Glide.with(context)
                    .load(urlList.get(1))
                    .into((ImageView) inflated.findViewById(R.id.vs_main_iv5_2));
            Glide.with(context)
                    .load(urlList.get(2))
                    .into((ImageView) inflated.findViewById(R.id.vs_main_iv5_3));
            Glide.with(context)
                    .load(urlList.get(3))
                    .into((ImageView) inflated.findViewById(R.id.vs_main_iv5_4));
            Glide.with(context)
                    .load(urlList.get(4))
                    .into((ImageView) inflated.findViewById(R.id.vs_main_iv5_5));
        }
    }

    static class ContentImageViewHolder_6 extends ContentRVHolder {
        protected TextView title;
        protected TextView writer;
        protected TextView date;
        protected TextView subject;
        protected ViewStub viewStubImage;
        protected PostItem postItem;
        View inflated;

        public ContentImageViewHolder_6(@NonNull View itemView, ViewStub viewStub) {
            super(itemView);
            title = itemView.findViewById(R.id.rvimage_tv_title);
            writer = itemView.findViewById(R.id.rvimage_tv_writer);
            date = itemView.findViewById(R.id.rvimage_tv_date);
            subject = itemView.findViewById(R.id.rvimage_tv_subject);
            this.viewStubImage = viewStub;
            viewStubImage.setLayoutResource(R.layout.viewstub_main_image_5);
            inflated = viewStubImage.inflate();
        }

        void onBind(PostItem postItem, Context context) {
            this.postItem = postItem;
            title.setText(postItem.getTitle());
            writer.setText(postItem.getWriterNickname());
            date.setText(Util.INSTANCE.unixTime2String(Long.parseLong(postItem.getUpdateTime())));
            subject.setText(postItem.getDescription());
            List<String> urlList = postItem.getImageUrl();

            Glide.with(context)
                    .load(urlList.get(0))
                    .into((ImageView) inflated.findViewById(R.id.vs_main_iv5_1));
            Glide.with(context)
                    .load(urlList.get(1))
                    .into((ImageView) inflated.findViewById(R.id.vs_main_iv5_2));
            Glide.with(context)
                    .load(urlList.get(2))
                    .into((ImageView) inflated.findViewById(R.id.vs_main_iv5_3));
            Glide.with(context)
                    .load(urlList.get(3))
                    .into((ImageView) inflated.findViewById(R.id.vs_main_iv5_4));
            Glide.with(context)
                    .load(urlList.get(3))
                    .into((ImageView) inflated.findViewById(R.id.vs_main_iv5_5));

            ImageView imageView = inflated.findViewById(R.id.vs_main_iv5_5);
            TextView textView = inflated.findViewById(R.id.vs_main_tv5_5);
            int tempNum = urlList.size() - 5;
            textView.setText("+" + tempNum + "개 이상");
            imageView.setColorFilter(Color.parseColor("#55050900"));
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