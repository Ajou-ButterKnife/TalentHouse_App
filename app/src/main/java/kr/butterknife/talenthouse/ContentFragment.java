package kr.butterknife.talenthouse;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class ContentFragment extends Fragment {

    RVItem item;
    View include;
    TextView title, date, writer, subject, addComment;
    EditText comment;
    RecyclerView commentRV;
    ArrayList<CommentItem> commentList;
    CommentRVAdapter rvAdapter;

    public ContentFragment(RVItem item) {
        this.item = item;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_content, container, false);

        include = view.findViewById(R.id.content_include);
        comment = view.findViewById(R.id.content_et_comment);
        commentRV = view.findViewById(R.id.content_rv_comment);
        addComment = view.findViewById(R.id.content_btn_addcomment);
        setIncludeLayout();

        commentList = new ArrayList<>();
        for(int i = 0; i < 20; i++)
            commentList.add(new CommentItem("writer" + (i + 1), "20210101", "This is comment. Comment's number is " + (i + 1)));

        rvAdapter = new CommentRVAdapter(commentList);

        commentRV.setLayoutManager(new LinearLayoutManager(getContext()));
        commentRV.setAdapter(rvAdapter);

        addComment.setOnClickListener((v) -> {
            writeComment();
        });

        return view;
    }

    public void setIncludeLayout() {
        if(item.getMp3Url() != null) {

        }
        else if(item.getMp3Url() != null) {

        }
        else if(item.getMp4Url() != null) {

        }
        title = include.findViewById(R.id.rvtext_tv_title);
        date = include.findViewById(R.id.rvtext_tv_date);
        writer = include.findViewById(R.id.rvtext_tv_writer);
        subject = include.findViewById(R.id.rvtext_tv_subject);

        title.setText(item.getTitle());
        date.setText(item.getDate());
        writer.setText(item.getWriter());
        subject.setText(item.getSubject());
    }

    public void writeComment() {
        CommentItem newComment = new CommentItem("test", "20200101", comment.getText().toString());

        //network 작업

        comment.setText("");
        rvAdapter.addNewComment(newComment);
    }
}