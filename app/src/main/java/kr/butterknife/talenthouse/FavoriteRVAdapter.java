package kr.butterknife.talenthouse;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FavoriteRVAdapter extends RecyclerView.Adapter<FavoriteRVAdapter.ViewHolder> {

    private ArrayList<likePerson> likePersonList = new ArrayList<>();

    @NonNull
    @Override
    public FavoriteRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bottom_item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteRVAdapter.ViewHolder holder, int position) {
        holder.onBind(likePersonList.get(position));
    }

    @Override
    public int getItemCount() {
        return likePersonList.size();
    }

    public void addItem(likePerson lp){
        likePersonList.add(lp);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_nickname;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_nickname = itemView.findViewById(R.id.bottom_item_nickname);
        }

        void onBind(likePerson likePerson){
            tv_nickname.setText(likePerson.getNickname());
        }
    }

}
