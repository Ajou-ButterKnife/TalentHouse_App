package kr.butterknife.talenthouse;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import kr.butterknife.talenthouse.MainRVViewHolder.*;
import kr.butterknife.talenthouse.network.ButterKnifeApi;
import kr.butterknife.talenthouse.network.response.CommentRes;
import kr.butterknife.talenthouse.network.response.CommonResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import java.util.ArrayList;
import java.util.List;

public class MainRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<PostItem> arrayList;
    private Context context;
    private boolean isLoading = false;
    private int page = 0;
    private ArrayList<SimpleExoPlayer> playerList = new ArrayList<>();

    public MainRVAdapter(Context context, ArrayList<PostItem> list) {
        arrayList = list;
        this.context = context;
    }

    private OnItemClickListener itemClickListener = null;

    public void setOnItemClickListener(OnItemClickListener listener) {
        itemClickListener = listener;
    }

    interface OnItemReloadListener {
        void reloadItem();
    }

    private OnItemReloadListener itemReloadListener = null;

    public void setOnItemReloadListener(OnItemReloadListener listener) {
        itemReloadListener = listener;
    }

    interface OnMyPageListener {
        void gotoMyPage(String writerId);
    }

    private OnMyPageListener myPageListener = null;

    public void setOnMyPageListener(OnMyPageListener listener) {
        myPageListener = listener;
    }

    public void doItemReload() {
        if(itemReloadListener != null)
            itemReloadListener.reloadItem();
    }

    interface OnSettingListener {
        void onSetting(View view, String postId);
    }

    private OnSettingListener onSettingListener = null;

    public void setOnSettingListener(OnSettingListener listener) {
        onSettingListener = listener;
    }

    public int getPageNum() {
        return page;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;

        if(viewType == ContentType.VIDEO.ordinal()) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_video, parent, false);
            ContentVideoViewHolder vh = new ContentVideoViewHolder(view);
            vh.setOnItemClickListener(itemClickListener);
            return vh;
        }else if(viewType == ContentType.IMAGE_1.ordinal()) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_image, parent, false);
            ViewStub viewStub = view.findViewById(R.id.rvimage_vs);
            ContentImageViewHolder_1 vh = new ContentImageViewHolder_1(view, viewStub);
            vh.setOnItemClickListener(itemClickListener);
            return vh;
        }else if(viewType == ContentType.IMAGE_2.ordinal()) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_image, parent, false);
            ViewStub viewStub = view.findViewById(R.id.rvimage_vs);
            ContentImageViewHolder_2 vh = new ContentImageViewHolder_2(view, viewStub);
            vh.setOnItemClickListener(itemClickListener);
            return vh;
        }else if(viewType == ContentType.IMAGE_3.ordinal()) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_image, parent, false);
            ViewStub viewStub = view.findViewById(R.id.rvimage_vs);
            ContentImageViewHolder_3 vh = new ContentImageViewHolder_3(view, viewStub);
            vh.setOnItemClickListener(itemClickListener);
            return vh;
        }else if(viewType == ContentType.IMAGE_4.ordinal()) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_image, parent, false);
            ViewStub viewStub = view.findViewById(R.id.rvimage_vs);
            ContentImageViewHolder_4 vh = new ContentImageViewHolder_4(view, viewStub);
            vh.setOnItemClickListener(itemClickListener);
            return vh;
        }else if(viewType == ContentType.IMAGE_5.ordinal()) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_image, parent, false);
            ViewStub viewStub = view.findViewById(R.id.rvimage_vs);
            ContentImageViewHolder_5 vh = new ContentImageViewHolder_5(view, viewStub);
            vh.setOnItemClickListener(itemClickListener);
            return vh;
        }else if(viewType == ContentType.IMAGE_6.ordinal()) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_image, parent, false);
            ViewStub viewStub = view.findViewById(R.id.rvimage_vs);
            ContentImageViewHolder_6 vh = new ContentImageViewHolder_6(view, viewStub);
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
            holder.writer.setOnClickListener(v -> myPageListener.gotoMyPage(arrayList.get(position).getWriterId()));
        }
        else if(_holder instanceof ContentImageViewHolder_1) {
            ContentImageViewHolder_1 holder = (ContentImageViewHolder_1) _holder;
            holder.onBind(arrayList.get(position), context);
            holder.writer.setOnClickListener(v -> myPageListener.gotoMyPage(arrayList.get(position).getWriterId()));
            if(arrayList.get(position).getWriterId().equals(LoginInfo.INSTANCE.getLoginInfo(context)[0])) {
                holder.settingBtn.setVisibility(View.VISIBLE);
                holder.settingBtn.setOnClickListener((v) -> {
                    onSettingListener.onSetting(v, arrayList.get(position).get_id());
                });
            }
        }else if(_holder instanceof ContentImageViewHolder_2) {
            ContentImageViewHolder_2 holder = (ContentImageViewHolder_2) _holder;
            holder.onBind(arrayList.get(position), context);
            holder.writer.setOnClickListener(v -> myPageListener.gotoMyPage(arrayList.get(position).getWriterId()));
            if(arrayList.get(position).getWriterId().equals(LoginInfo.INSTANCE.getLoginInfo(context)[0])) {
                holder.settingBtn.setVisibility(View.VISIBLE);
                holder.settingBtn.setOnClickListener((v) -> {
                    onSettingListener.onSetting(v, arrayList.get(position).get_id());
                });
            }
        }else if(_holder instanceof ContentImageViewHolder_3) {
            ContentImageViewHolder_3 holder = (ContentImageViewHolder_3) _holder;
            holder.onBind(arrayList.get(position), context);
            holder.writer.setOnClickListener(v -> myPageListener.gotoMyPage(arrayList.get(position).getWriterId()));
            if(arrayList.get(position).getWriterId().equals(LoginInfo.INSTANCE.getLoginInfo(context)[0])) {
                holder.settingBtn.setVisibility(View.VISIBLE);
                holder.settingBtn.setOnClickListener((v) -> {
                    onSettingListener.onSetting(v, arrayList.get(position).get_id());
                });
            }
        }else if(_holder instanceof ContentImageViewHolder_4) {
            ContentImageViewHolder_4 holder = (ContentImageViewHolder_4) _holder;
            holder.onBind(arrayList.get(position), context);
            holder.writer.setOnClickListener(v -> myPageListener.gotoMyPage(arrayList.get(position).getWriterId()));
            if(arrayList.get(position).getWriterId().equals(LoginInfo.INSTANCE.getLoginInfo(context)[0])) {
                holder.settingBtn.setVisibility(View.VISIBLE);
                holder.settingBtn.setOnClickListener((v) -> {
                    onSettingListener.onSetting(v, arrayList.get(position).get_id());
                });
            }
        }else if(_holder instanceof ContentImageViewHolder_5) {
            ContentImageViewHolder_5 holder = (ContentImageViewHolder_5) _holder;
            holder.onBind(arrayList.get(position), context);
            holder.writer.setOnClickListener(v -> myPageListener.gotoMyPage(arrayList.get(position).getWriterId()));
            if(arrayList.get(position).getWriterId().equals(LoginInfo.INSTANCE.getLoginInfo(context)[0])) {
                holder.settingBtn.setVisibility(View.VISIBLE);
                holder.settingBtn.setOnClickListener((v) -> {
                    onSettingListener.onSetting(v, arrayList.get(position).get_id());
                });
            }
        }else if(_holder instanceof ContentImageViewHolder_6) {
            ContentImageViewHolder_6 holder = (ContentImageViewHolder_6) _holder;
            holder.onBind(arrayList.get(position), context);
            holder.writer.setOnClickListener(v -> myPageListener.gotoMyPage(arrayList.get(position).getWriterId()));
            if(arrayList.get(position).getWriterId().equals(LoginInfo.INSTANCE.getLoginInfo(context)[0])) {
                holder.settingBtn.setVisibility(View.VISIBLE);
                holder.settingBtn.setOnClickListener((v) -> {
                    onSettingListener.onSetting(v, arrayList.get(position).get_id());
                });
            }
        }
        else if(_holder instanceof ContentVideoViewHolder) {
            ContentVideoViewHolder holder = (ContentVideoViewHolder) _holder;
            holder.title.setText(arrayList.get(position).getTitle());
            holder.writer.setOnClickListener(v -> myPageListener.gotoMyPage(arrayList.get(position).getWriterId()));
            holder.writer.setText(arrayList.get(position).getWriterNickname());
            holder.date.setText(Util.INSTANCE.unixTime2String(Long.parseLong(arrayList.get(position).getUpdateTime())));
            holder.subject.setText(arrayList.get(position).getDescription());
            if(arrayList.get(position).getWriterId().equals(LoginInfo.INSTANCE.getLoginInfo(context)[0])) {
                holder.settingBtn.setVisibility(View.VISIBLE);
                holder.settingBtn.setOnClickListener((v) -> {
                    onSettingListener.onSetting(v, arrayList.get(position).get_id());
                });
            }

            SimpleExoPlayer player = new SimpleExoPlayer.Builder(context).build();
            holder.pv.setPlayer(player);

            DataSource.Factory factory = new DefaultDataSourceFactory(context, "Ex98VideoAndExoPlayer");
            Uri videoUri = Uri.parse(arrayList.get(position).getVideoUrl());
            ProgressiveMediaSource mediaSource= new ProgressiveMediaSource.Factory(factory).createMediaSource(videoUri);
            player.addMediaSource(mediaSource);
            player.prepare();
            player.setPlayWhenReady(false);
            playerList.add(player);

            boolean check = false;
            for(String id : arrayList.get(position).getLikeIDs()){
                if(id.equals(LoginInfo.INSTANCE.getLoginInfo(context)[0])){
                    check = true;
                    break;
                }
            }
            if(check)
                holder.likeBtn.setText("좋아요 취소");
            else
                holder.likeBtn.setText("좋아요");
            holder.likeCnt.setText("좋아요 " + arrayList.get(position).getLikeCnt() + "개");
            holder.likeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.updateLike(arrayList.get(position).get_id(), LoginInfo.INSTANCE.getLoginInfo(context)[0]);
                }
            });


        }
        else {

        }
    }

    public void clearPlayerList(){
        for(SimpleExoPlayer player : playerList){
            player.release();
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
        else if(item.getVideoUrl() != null) {
            return ContentType.VIDEO.ordinal();
        }
        else if(item.getImageUrl() != null) {
            switch(item.getImageUrl().size()){
                case 1:
                    return ContentType.IMAGE_1.ordinal();
                case 2:
                    return ContentType.IMAGE_2.ordinal();
                case 3:
                    return ContentType.IMAGE_3.ordinal();
                case 4:
                    return ContentType.IMAGE_4.ordinal();
                case 5:
                    return ContentType.IMAGE_5.ordinal();
                default:
                    return ContentType.IMAGE_6.ordinal();
            }
        }
        return ContentType.NO.ordinal();
    }

      
    public void initScrollListener(RecyclerView rv) {
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if(!isLoading) {
                    if(layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == arrayList.size() - 1) {
                        loadMore();
                        Log.d("TESTTEST","isloading");
                        isLoading = true;
                    }
                }
            }
        });
    }

    private void loadMore() {
        arrayList.add(null);
        int tempSize = arrayList.size() - 1;
        notifyItemInserted(tempSize);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                arrayList.remove(tempSize);
                int scrollPosition = arrayList.size();
                notifyItemRemoved(tempSize - 1);
                int currentSize = scrollPosition;
                page++;
                itemReloadListener.reloadItem();
                notifyDataSetChanged();
                isLoading = false;
            }
        }, 500);

    }

    public void setPage(int page){
        this.page = page;
    }

}