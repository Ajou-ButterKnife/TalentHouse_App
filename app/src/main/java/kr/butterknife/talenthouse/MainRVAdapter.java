package kr.butterknife.talenthouse;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainRVAdapter extends RecyclerView.Adapter<MainRVAdapter.MyViewHolder> {
    private ArrayList<RVItem> arrayList;

    static class MyViewHolder extends RecyclerView.ViewHolder {
        protected TextView title;
        protected TextView writer;
        protected TextView date;
        protected TextView subject;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.rvtext_tv_title);
            writer = itemView.findViewById(R.id.rvtext_tv_writer);
            date = itemView.findViewById(R.id.rvtext_tv_date);
            subject = itemView.findViewById(R.id.rvtext_tv_subject);
        }
    }

    public MainRVAdapter(ArrayList<RVItem> list) {
        arrayList = list;
    }

    // TODO: 4/6/21 나중에 들어오는 타입에 따라서 뷰 여러개
    // 참고 : https://lakue.tistory.com/16
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_text, parent, false);

        MyViewHolder vh = new MyViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title.setText(arrayList.get(position).getTitle());
        holder.writer.setText(arrayList.get(position).getWriter());
        holder.date.setText(arrayList.get(position).getDate());
        holder.subject.setText(arrayList.get(position).getSubject());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}