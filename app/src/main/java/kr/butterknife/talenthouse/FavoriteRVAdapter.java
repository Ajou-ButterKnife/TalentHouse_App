package kr.butterknife.talenthouse;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class FavoriteRVAdapter extends RecyclerView.Adapter<FavoriteRVAdapter.FavoriteRVHolder> {
    private ArrayList<likePerson> likePersonList = new ArrayList<>();
    Context context;
    BottomSheetDialog bottomSheetDialog;

    public FavoriteRVAdapter(BottomSheetDialog bottomSheetDialog){ this.bottomSheetDialog = bottomSheetDialog; }

    static class FavoriteRVHolder extends RecyclerView.ViewHolder{
        TextView tv_nickname;
        ImageView profile;
        Context mainContext;

        public FavoriteRVHolder(@NonNull View itemView, Context context) {
            super(itemView);
            tv_nickname = itemView.findViewById(R.id.bottom_item_nickname);
            profile = itemView.findViewById(R.id.bottom_item_profile);
            this.mainContext = context;
        }
    }

    @NonNull
    @Override
    public FavoriteRVAdapter.FavoriteRVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bottom_item, parent,false);
        context = parent.getContext();
        return new FavoriteRVHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteRVAdapter.FavoriteRVHolder holder, int position) {
        if(likePersonList.get(position).getProfile().equals("") == false){
            Glide.with(context).load(likePersonList.get(position).getProfile()).circleCrop().into(holder.profile);
        }
        holder.tv_nickname.setText(likePersonList.get(position).getNickname());
        holder.tv_nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                MainActivity mainActivity = (MainActivity) holder.mainContext;
                mainActivity.setMyPageID(likePersonList.get(position).getWriterId());
                mainActivity.outsideMyPageClick();
            }
        });
    }

    @Override
    public int getItemCount() {
        return likePersonList.size();
    }

    public void addItem(likePerson lp){
        likePersonList.add(lp);
    }

}
