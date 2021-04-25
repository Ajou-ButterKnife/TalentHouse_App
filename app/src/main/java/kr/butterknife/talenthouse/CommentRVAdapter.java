package kr.butterknife.talenthouse;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CommentRVAdapter extends RecyclerView.Adapter<CommentRVAdapter.CommentRVHolder>{
    private ArrayList<CommentItem> comments;

    public CommentRVAdapter(ArrayList<CommentItem> comments) {
        this.comments = comments;
    }

    static class CommentRVHolder extends RecyclerView.ViewHolder {
        TextView writer, date, comment;

        public CommentRVHolder(@NonNull View itemView) {
            super(itemView);
            writer = itemView.findViewById(R.id.rvcomment_tv_writer);
            date = itemView.findViewById(R.id.rvcomment_tv_date);
            comment = itemView.findViewById(R.id.rvcomment_tv_comment);
        }
    }

    @NonNull
    @Override
    public CommentRVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_comment, parent, false);
        CommentRVHolder vh = new CommentRVHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentRVHolder holder, int position) {
        holder.writer.setText(comments.get(position).getWriterNickname());
        holder.date.setText(Util.INSTANCE.getDate2String(comments.get(position).getDate()));
        holder.comment.setText(comments.get(position).getComment());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void addNewComment(CommentItem newItem) {
        comments.add(newItem);
        notifyItemInserted(comments.size() - 1);
    }
}
