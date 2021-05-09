package kr.butterknife.talenthouse;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import kr.butterknife.talenthouse.MainRVViewHolder.*;
import java.util.ArrayList;
import java.util.List;

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

        if(viewType == ContentType.VIDEO.ordinal()) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_mp4, parent, false);
            ContentMP4ViewHolder vh = new ContentMP4ViewHolder(view);
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
            holder.date.setText(arrayList.get(position).getUpdateTime());
//            holder.date.setText(Util.INSTANCE.getDate2String(arrayList.get(position).getUpdateTime()));
            holder.subject.setText(arrayList.get(position).getDescription());
        }else if(_holder instanceof ContentMP4ViewHolder) {

        }else if(_holder instanceof ContentImageViewHolder_1) {
            ContentImageViewHolder_1 holder = (ContentImageViewHolder_1) _holder;
            holder.onBind(arrayList.get(position), context);
        }else if(_holder instanceof ContentImageViewHolder_2) {
            ContentImageViewHolder_2 holder = (ContentImageViewHolder_2) _holder;
            holder.onBind(arrayList.get(position), context);
        }else if(_holder instanceof ContentImageViewHolder_3) {
            ContentImageViewHolder_3 holder = (ContentImageViewHolder_3) _holder;
            holder.onBind(arrayList.get(position), context);
        }else if(_holder instanceof ContentImageViewHolder_4) {
            ContentImageViewHolder_4 holder = (ContentImageViewHolder_4) _holder;
            holder.onBind(arrayList.get(position), context);
        }else if(_holder instanceof ContentImageViewHolder_5) {
            ContentImageViewHolder_5 holder = (ContentImageViewHolder_5) _holder;
            holder.onBind(arrayList.get(position), context);
        }else if(_holder instanceof ContentImageViewHolder_6) {
            ContentImageViewHolder_6 holder = (ContentImageViewHolder_6) _holder;
            holder.onBind(arrayList.get(position), context);
        }else {}
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        PostItem item = arrayList.get(position);

        if(item.getVideoUrl() != null) {
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

}