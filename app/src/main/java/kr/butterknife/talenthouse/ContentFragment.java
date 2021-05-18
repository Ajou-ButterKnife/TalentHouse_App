package kr.butterknife.talenthouse;

import android.net.Uri;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.ExoPlayerLibraryInfo;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import kr.butterknife.talenthouse.network.request.FavoriteUserIdReq;
import kr.butterknife.talenthouse.network.request.GetCommentReq;
import kr.butterknife.talenthouse.network.response.FavoritePostUserIdRes;
import kr.butterknife.talenthouse.network.response.LikeRes;
import me.relex.circleindicator.CircleIndicator;


import kr.butterknife.talenthouse.network.ButterKnifeApi;
import kr.butterknife.talenthouse.network.request.UploadCommentReq;
import kr.butterknife.talenthouse.network.request.UploadPostReq;
import kr.butterknife.talenthouse.network.response.CommentRes;
import kr.butterknife.talenthouse.network.response.CommonResponse;
import kr.butterknife.talenthouse.network.response.GetCommentsRes;
import kr.butterknife.talenthouse.network.response.PostRes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContentFragment extends Fragment {

    PostItem item;
    ViewStub content;
    TextView title, date, writer, subject, addComment, like;
    EditText comment;
    RecyclerView commentRV;
    ArrayList<CommentItem> commentList;
    CommentRVAdapter rvAdapter;
    PlayerView pv;
    PlayerControlView pcv;
    SimpleExoPlayer player;
    ImageContentPagerAdapter adapter;
    ViewPager viewPager;
    CircleIndicator indicator;
    Button likeBtn;

    BottomSheetDialog bottomSheetDialog;
    FavoriteRVAdapter bottomAdapter;

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
        rvAdapter = new CommentRVAdapter(commentList);
        commentRV.setLayoutManager(new LinearLayoutManager(getContext()));
        commentRV.setAdapter(rvAdapter);

        addComment.setOnClickListener((v) -> {
            writeComment();
        });
        getComments();
        return view;
    }

    public void getComments(){
        new Runnable(){
            @Override
            public void run(){
                try{
                    ButterKnifeApi.INSTANCE.getRetrofitService().getComments(new GetCommentReq(item.get_id())).enqueue(new Callback<GetCommentsRes>() {
                        @Override
                        public void onResponse(Call<GetCommentsRes> call, Response<GetCommentsRes> response) {
                            if(response.body() != null){
                                try{
                                    List<CommentItem> commentList = response.body().getData();
                                    for(CommentItem c : commentList){
                                        rvAdapter.addNewComment(c);
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<GetCommentsRes> call, Throwable t) {
                            // 서버 쪽으로 메시지를 보내지 못한 경우
                            Log.d("err", "SERVER CONNECTION ERROR");
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.run();
    }


    public void setIncludeLayout() {
        if(item.getImageUrl() != null) {
            content.setLayoutResource(R.layout.viewstub_content_image);
            View inflated = content.inflate();
            title = inflated.findViewById(R.id.content_image_title);
            date = inflated.findViewById(R.id.content_image_date);
            writer = inflated.findViewById(R.id.content_image_tv_writer);
            subject = inflated.findViewById(R.id.content_tv_subject);
            viewPager = inflated.findViewById(R.id.content_pager);
            adapter = new ImageContentPagerAdapter(getContext(), item.getImageUrl());
            viewPager.setAdapter(adapter);
            indicator = inflated.findViewById(R.id.content_indicator);
            indicator.setViewPager(viewPager);
            like = inflated.findViewById(R.id.content_tv_like);
            likeBtn = inflated.findViewById(R.id.content_btn_like);
        }
        else if(item.getVideoUrl() != null) {
            content.setLayoutResource(R.layout.viewstub_content_video);
            View inflated = content.inflate();
            title = inflated.findViewById(R.id.content_video_tv_title);
            date = inflated.findViewById(R.id.content_video_tv_date);
            writer = inflated.findViewById(R.id.content_video_tv_writer);
            subject = inflated.findViewById(R.id.content_video_tv_subject);
            pv = inflated.findViewById(R.id.content_video_player);
            pcv = inflated.findViewById(R.id.content_video_controller);
            like = inflated.findViewById(R.id.content_tv_like);
            likeBtn = inflated.findViewById(R.id.content_btn_like);
        }
        else {
            content.setLayoutResource(R.layout.item_rv_text);
            View inflated = content.inflate();
            title = inflated.findViewById(R.id.rvtext_tv_title);
            date = inflated.findViewById(R.id.rvtext_tv_date);
            writer = inflated.findViewById(R.id.rvtext_tv_writer);
            subject = inflated.findViewById(R.id.rvtext_tv_subject);
            like = inflated.findViewById(R.id.content_tv_like);
            likeBtn = inflated.findViewById(R.id.content_btn_like);
        }

        title.setText(item.getTitle());
        date.setText(Util.INSTANCE.unixTime2String(Long.parseLong(item.getUpdateTime())));
        writer.setText(item.getWriterNickname());
        subject.setText(item.getDescription());

        boolean check = false;
        for(String id : item.getLikeIDs()){
            if(id.equals(LoginInfo.INSTANCE.getLoginInfo(getContext())[0])){
                check = true;
                break;
            }
        }
        if(check)
            likeBtn.setText("좋아요 취소");
        else
            likeBtn.setText("좋아요");

        like.setText("좋아요 " + item.getLikeCnt() + "개");
        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Runnable(){
                    @Override
                    public void run() {
                        try{
                            ButterKnifeApi.INSTANCE.getRetrofitService().putLike(item.get_id(), LoginInfo.INSTANCE.getLoginInfo(getContext())[0]).enqueue(new Callback<LikeRes>() {
                                @Override
                                public void onResponse(Call<LikeRes> call, Response<LikeRes> response) {
                                    if(response.body() != null){
                                        if (response.body().getResult().equals("Plus")) {
                                            like.setText("좋아요 " + response.body().getLikeCnt() + "개");
                                            likeBtn.setText("좋아요 취소");
                                        }else if(response.body().getResult().equals("Minus")){
                                            like.setText("좋아요 " + response.body().getLikeCnt() + "개");
                                            likeBtn.setText("좋아요");
                                        }
                                    }
                                }
                                @Override
                                public void onFailure(Call<LikeRes> call, Throwable t) {
                                    // 서버 쪽으로 메시지를 보내지 못한 경우
                                    Log.d("err", "SERVER CONNECTION ERROR");
                                }
                            });
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }.run();
            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog = new BottomSheetDialog(getContext());
                View v = getLayoutInflater().inflate(R.layout.bottom_content, null);
                bottomSheetDialog.setContentView(v);

                bottomSheetDialog.show();

                RecyclerView recyclerView = v.findViewById(R.id.bottom_content_recyclerView);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(linearLayoutManager);

                bottomAdapter = new FavoriteRVAdapter();
                recyclerView.setAdapter(bottomAdapter);

                getFavoriteUser(item.get_id());

//                for(int i =0;i<5;i++){
//                    likePerson l = new likePerson("nickname   :   " + i);
//                    bottomAdapter.addItem(l);
//                }
//                bottomAdapter.notifyDataSetChanged();
            }

            public void getFavoriteUser(String postId){
                new Runnable(){
                    @Override
                    public void run() {
                        try{
                            ButterKnifeApi.INSTANCE.getRetrofitService().getPostFavoriteId(new FavoriteUserIdReq(postId)).enqueue(new Callback<FavoritePostUserIdRes>() {
                                @Override
                                public void onResponse(Call<FavoritePostUserIdRes> call, Response<FavoritePostUserIdRes> response) {
                                    if(response.body() != null){
                                        List<String> userId =  response.body().getData();
                                        for(String s : userId){
                                            likePerson l = new likePerson(s);
                                            bottomAdapter.addItem(l);
                                        }
                                        bottomAdapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onFailure(Call<FavoritePostUserIdRes> call, Throwable t) {
                                    // 서버쪽으로 아예 메시지를 보내지 못한 경우
                                    Log.d("ERR", "SERVER CONNECTION ERROR");
                                }
                            });
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }.run();
            }


        writer.setOnClickListener(v -> {
            ((MainActivity) getActivity()).setMyPageID(item.getWriterId());
            ((MainActivity) getActivity()).outsideMyPageClick();

        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if(item.getVideoUrl() != null) {
            player = new SimpleExoPlayer.Builder(getContext()).build();
            pv.setPlayer(player);
            pcv.setPlayer(player);

            DataSource.Factory factory = new DefaultDataSourceFactory(getContext(), "Ex98VideoAndExoPlayer");
            Uri videoUri = Uri.parse(item.getVideoUrl());
            ProgressiveMediaSource mediaSource= new ProgressiveMediaSource.Factory(factory).createMediaSource(videoUri);

            player.addMediaSource(mediaSource);
            player.prepare();

            player.setPlayWhenReady(false);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(item.getVideoUrl() != null) {
            pv.setPlayer(null);
            player.release();
            player = null;
        }
    }

    public void writeComment() {
        //network 작업
        postComment(item.get_id(), LoginInfo.INSTANCE.getLoginInfo(getContext())[0], LoginInfo.INSTANCE.getLoginInfo(getContext())[1], comment.getText().toString());
        comment.setText("");
    }
    public void postComment(String _id, String id, String nickname, String comment){
        new Runnable(){
            @Override
            public void run() {
                try {
                    ButterKnifeApi.INSTANCE.getRetrofitService().commentCreate(new UploadCommentReq(_id, id, nickname, comment)).enqueue(new Callback<CommentRes>() {

                        @Override
                        public void onResponse(Call<CommentRes> call, Response<CommentRes> response) {
                            // 정상 출력이 되면 아래 로그가 출력됨
                            if (response.body() != null) {
                                CommentRes result = response.body();
                                if (result.getResult().equals("Success")) {
                                    rvAdapter.addNewComment(result.getData());
                                } else {

                                }
                            }

                            // 정상 출력이 되지 않을 때 서버에서의 response
                            else {
                                Log.d("ERR", response.errorBody().toString());
                                Log.d("ERR", response.message());
                                Log.d("ERR", String.valueOf(response.code()));
                            }
                        }
                        @Override
                        public void onFailure(Call<CommentRes> call, Throwable t) {
                            // 서버쪽으로 아예 메시지를 보내지 못한 경우
                            Log.d("ERR", "SERVER CONNECTION ERROR");
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.run();

    }

}