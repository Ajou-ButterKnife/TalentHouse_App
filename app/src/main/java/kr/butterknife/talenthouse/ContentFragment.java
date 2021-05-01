package kr.butterknife.talenthouse;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ContentFragment extends Fragment {

    PostItem item;
    ViewStub content;
    TextView title, date, writer, subject, addComment;
    EditText comment;
    RecyclerView commentRV;
    ArrayList<CommentItem> commentList;
    CommentRVAdapter rvAdapter;

    public ContentFragment(PostItem item) {
        this.item = item;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_content, container, false);

        content = view.findViewById(R.id.content_include);
        comment = view.findViewById(R.id.content_et_comment);
        commentRV = view.findViewById(R.id.content_rv_comment);
        addComment = view.findViewById(R.id.content_btn_addcomment);
        setIncludeLayout();

        commentList = new ArrayList<>();
        for(int i = 0; i < 20; i++)
            commentList.add(new CommentItem(LoginInfo.INSTANCE.getLoginInfo(getContext())[0], "writer" + (i + 1), new Date(), "This is comment. Comment's number is " + (i + 1)));

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
        else if(item.getImageUrl() != null) {

        }
        else if(item.getVideoUrl() != null) {

        }
        else {
            content.setLayoutResource(R.layout.item_rv_text);
            View inflated = content.inflate();
            title = inflated.findViewById(R.id.rvtext_tv_title);
            date = inflated.findViewById(R.id.rvtext_tv_date);
            writer = inflated.findViewById(R.id.rvtext_tv_writer);
            subject = inflated.findViewById(R.id.rvtext_tv_subject);
        }

        title.setText(item.getTitle());
//        date.setText(Util.INSTANCE.getDate2String(item.getUpdateTime()));
        date.setText(item.getUpdateTime());
        writer.setText(item.getWriterNickname());
        subject.setText(item.getDescription());
    }

    public void writeComment() {
        CommentItem newComment = new CommentItem(LoginInfo.INSTANCE.getLoginInfo(getContext())[0], "test", new Date(), comment.getText().toString());

        //network 작업

        comment.setText("");
        rvAdapter.addNewComment(newComment);
    }
}