package kr.butterknife.talenthouse;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import kr.butterknife.talenthouse.network.ButterKnifeApi;
import kr.butterknife.talenthouse.network.response.PostRes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends Fragment implements View.OnClickListener {

    private RecyclerView rv;
    private MainRVAdapter rvAdapter;
    private ArrayList<PostItem> posts;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        FloatingActionButton btnWrite = view.findViewById(R.id.main_fab_write);

        btnWrite.setOnClickListener(this);

        rv = view.findViewById(R.id.main_rv);
        posts = new ArrayList<>();

        rvAdapter = new MainRVAdapter(getContext(), posts);
        rvAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                ((MainActivity) getActivity()).replaceFragment(
                        new ContentFragment(posts.get(pos)),
                        "Content"
                );
            }
        });

        rvAdapter.initScrollListener(rv);
        rvAdapter.setOnItemReloadListener(() -> getPosts());

        rv.setAdapter(rvAdapter);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAdapter.doItemReload();

        return view;
    }

    public void getPosts(){
        new Runnable(){
            @Override
            public void run(){
                try{
                    ButterKnifeApi.INSTANCE.getRetrofitService().getPosts(rvAdapter.getPageNum()).enqueue(new Callback<PostRes>() {
                        @Override
                        public void onResponse(Call<PostRes> call, Response<PostRes> response) {
                            if(response.body() != null){
                                try{
                                    List<PostItem> postList = response.body().getData();
                                    for(PostItem p : postList){
                                        if(p.getVideoUrl() != null)
                                            posts.add(new PostItem(p.get_id(), p.getTitle(), p.getWriterNickname(), p.getWriterId(), p.getUpdateTime(), p.getDescription(), p.getVideoUrl(), p.getLikeCnt(), p.getCategory(), p.getComments()));
                                        else if(p.getImageUrl().size() != 0)
                                            posts.add(new PostItem(p.get_id(), p.getTitle(), p.getWriterNickname(), p.getWriterId(), p.getUpdateTime(), p.getDescription(), p.getImageUrl(), p.getLikeCnt(), p.getCategory(), p.getComments()));
                                        else
                                            posts.add(new PostItem(p.get_id(), p.getTitle(), p.getWriterNickname(), p.getWriterId(), p.getUpdateTime(), p.getDescription(), p.getLikeCnt(), p.getCategory(), p.getComments()));
                                    }
                                    rvAdapter.notifyDataSetChanged();
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<PostRes> call, Throwable t) {
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

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.main_fab_write:
                WriteFragment writeFragment = new WriteFragment();
                ((MainActivity)getActivity()).replaceFragment(writeFragment, "Write");
                break;
        }
    }
}