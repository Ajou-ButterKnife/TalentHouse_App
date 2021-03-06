package kr.butterknife.talenthouse.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import kr.butterknife.talenthouse.LoadingDialog;
import kr.butterknife.talenthouse.activity.MainActivity;
import kr.butterknife.talenthouse.adapter.MainRVAdapter;
import kr.butterknife.talenthouse.OnItemClickListener;
import kr.butterknife.talenthouse.PostItem;
import kr.butterknife.talenthouse.R;
import kr.butterknife.talenthouse.Util;
import kr.butterknife.talenthouse.network.ButterKnifeApi;
import kr.butterknife.talenthouse.network.response.PostRes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoardFragment extends Fragment {

    private RecyclerView rv;
    private MainRVAdapter rvAdapter;
    private ArrayList<PostItem> posts;
    private String category;
    private Button sortToTime;
    private Button sortToLike;
    private LinearLayoutManager linearLayoutManager;
    private Integer sortFlag = 1;


    public BoardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_board, container, false);

        rv = view.findViewById(R.id.board_rv);
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
        rvAdapter.setOnSettingListener((v, postId) -> {
            Util.INSTANCE.postSetting(requireContext(), v, postId, posts, (item) -> {
                ((MainActivity) getActivity()).replaceFragment(new WriteFragment(), "Write", item);
                return true;
            }, (idx) -> {
                posts.remove((int) idx);
                rvAdapter.notifyDataSetChanged();
                return true;
            });
        });

        rv.setAdapter(rvAdapter);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(linearLayoutManager);

        category = getArguments().getString("category");

        rvAdapter.doItemReload();

        sortToTime = (Button) view.findViewById(R.id.board_date_sort_btn);
        sortToLike = (Button) view.findViewById(R.id.board_like_sort_btn);

        sortToTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                sortFlag = 1;
                rvAdapter.setPage(0);
                posts.clear();
                rv.setAdapter(null);
                rv.setLayoutManager(null);
                rv.setAdapter(rvAdapter);
                rv.setLayoutManager(linearLayoutManager);
                rvAdapter.notifyDataSetChanged();
                rvAdapter.doItemReload();
            }
        });

        sortToLike.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                sortFlag = 2;
                rvAdapter.setPage(0);
                posts.clear();
                rv.setAdapter(null);
                rv.setLayoutManager(null);
                rv.setAdapter(rvAdapter);
                rv.setLayoutManager(linearLayoutManager);
                rvAdapter.notifyDataSetChanged();
                rvAdapter.doItemReload();
            }
        });
        return view;
    }

    public void getPosts(){
        new Runnable(){
            @Override
            public void run(){
                try{
                    LoadingDialog.INSTANCE.onLoadingDialog(getActivity());
                    ButterKnifeApi.INSTANCE.getRetrofitService().getBoardPosts(category, sortFlag, rvAdapter.getPage()).enqueue(new Callback<PostRes>() {
                        @Override
                        public void onResponse(Call<PostRes> call, Response<PostRes> response) {
                            if(response.body() != null){
                                try{
                                    List<PostItem> postList = response.body().getData();
                                    for(PostItem p : postList){
                                        if(p.getVideoUrl() != null)
                                            posts.add(new PostItem(p.get_id(), p.getTitle(), p.getWriterNickname(), p.getWriterId(), p.getUpdateTime(), p.getDescription(), p.getVideoUrl(), p.getLikeCnt(), p.getLikeIDs(), p.getCategory(), p.getComments(), p.getProfile()));
                                        else if(p.getImageUrl().size() != 0)
                                            posts.add(new PostItem(p.get_id(), p.getTitle(), p.getWriterNickname(), p.getWriterId(), p.getUpdateTime(), p.getDescription(), p.getImageUrl(), p.getLikeCnt(), p.getLikeIDs(), p.getCategory(), p.getComments(), p.getProfile()));
                                        else
                                            posts.add(new PostItem(p.get_id(), p.getTitle(), p.getWriterNickname(), p.getWriterId(), p.getUpdateTime(), p.getDescription(), p.getLikeCnt(), p.getLikeIDs(), p.getCategory(), p.getComments(), p.getProfile()));
                                        rvAdapter.notifyItemInserted(posts.size() - 1);
                                    }
                                    if(postList.size() == 0)
                                        rvAdapter.setPage(rvAdapter.getPage() - 1);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                            LoadingDialog.INSTANCE.offLoadingDialog();
                        }
                        @Override
                        public void onFailure(Call<PostRes> call, Throwable t) {
                            // ?????? ????????? ???????????? ????????? ?????? ??????
                            Log.d("err", "SERVER CONNECTION ERROR");
                            LoadingDialog.INSTANCE.offLoadingDialog();
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                    LoadingDialog.INSTANCE.offLoadingDialog();
                }
            }
        }.run();
    }
}